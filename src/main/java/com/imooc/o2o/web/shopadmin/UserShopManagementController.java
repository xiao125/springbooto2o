package com.imooc.o2o.web.shopadmin;


import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.ProductSellDailyService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserShopManagementController {

    @Autowired
    private UserShopMapService userShopMapService;

    @Autowired
    private ProductSellDailyService productSellDailyService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listusershopmapbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listUserShopMapByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session中获取当前店铺的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize >-1) && (currentShop !=null) && (currentShop.getShopId() !=null)){
            UserShopMap userShopMapCondition = new UserShopMap();
            //传入查询条件
            userShopMapCondition.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request,"userName");
            if (userName !=null){
                // 若传入顾客名，则按照顾客名模糊查询
                PersonInfo customer = new PersonInfo();
                customer.setName(userName);
                userShopMapCondition.setUser(customer);
            }
            //分页获取该店铺下的顾客积分列表
            UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition,pageIndex,pageSize);
            modelMap.put("userShopMapList",ue.getUserShopMapList());
            modelMap.put("count",ue.getCount());
            modelMap.put("success",true);

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;

    }


    @RequestMapping(value = "/listproductselldailyinfobyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductSellDailyInfobyShop(HttpServletRequest request) throws ParseException {

        Map<String, Object> modelMap = new HashMap<>();
        //获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值效验，主要确保shopId不为空
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            ProductSellDaily productSellDaily = new ProductSellDaily();
            productSellDaily.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
        }

        return modelMap;

    }




}
