package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *店铺管理
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;


    /**
     * 商店管理页面（进入此页面，需判断是否传入相应的店铺id，否则跳转商店列表页面）
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){

        Map<String,Object> modelMap = new HashMap<String, Object>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId <=0){

            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                modelMap.put("redirect",true); //重定向页面地址
                modelMap.put("url","/o2oMaven/shopadmin/shoplist");
            }else {

                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }


    /**
     * 查询店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){

        Map<String,Object> modelMap = new HashMap<String, Object>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        try {

            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            //查询店铺列表
            ShopExecution se = shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            //列出店铺成功之后，将店铺放入session中作为权限验证依据，即该账号只能操作它自己的店铺
            request.getSession().setAttribute("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 根据shopId查询店铺信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        System.out.println("获取的shopId="+shopId);
        if (shopId > -1){
            try{
                Shop shop = shopService.getByShopId(shopId); //获取商铺信息
                List<Area> areaList = areaService.getAreaList(); //获取区域列表
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }




    /**
     * 获取店铺类别与店铺区域信息，返回给前端
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){

        Map<String,Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>(); //店铺类别列表
        List<Area> areaList = new ArrayList<>();//店铺区域
        try {
            //查询店铺类别列表与区域
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList(); //获取区域信息
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request){

        Map<String,Object> modelMap = new HashMap<String, Object>();

        //检查验证码是否正确
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //1..接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr"); //获取前端请求的json参数
        ObjectMapper mapper = new ObjectMapper(); //jackson 解析json
        Shop shop = null;

        try {

            shop = mapper.readValue(shopStr,Shop.class);//解析前端传递的json,对应实体类
        }catch (Exception e){

            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg =null;
        // CommonsMultipartResolver 图片上传解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)){ //有上传文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop !=null && shopImg !=null){

            //前端登录账号成功后，存入Session中，可以获取存入的user
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se; //返回结果状态
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                se = shopService.addShop(shop,imageHolder);
                if (se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    // 该用户可以操作的店铺列表
                    @SuppressWarnings("unchecked")
                   List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList ==null || shopList.size() ==0){ //如果是第一次，就创建
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                }else {

                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (ShopOperationException e){

                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());

            }catch (IOException e){

                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
    }



    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        //检查验证码是否正确
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //1..接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr"); //获取前端请求的json参数
        ObjectMapper mapper = new ObjectMapper(); //jackson 解析json
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr,Shop.class);//解析前端传递的json,对应实体类
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg =null;
        // CommonsMultipartResolver 图片上传解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)){ //有上传文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");

        }
        //2.修改店铺信息
        if (shop !=null && shop.getShopId() !=null){
            ShopExecution se; //返回结果状态
            try {
                if (shopImg == null){
                    se = shopService.modifyShop(shop,null);
                }else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    se = shopService.modifyShop(shop,imageHolder);
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (ShopOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }catch (IOException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
    }


}
