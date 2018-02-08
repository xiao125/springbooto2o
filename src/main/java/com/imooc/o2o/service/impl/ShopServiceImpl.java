package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

@Service
public class ShopServiceImpl implements ShopService{


    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {

        //将页码转换成行码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        //依据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        //依据相同的查询条件，返回店铺总数
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList !=null){
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return se;
    }



    /**
     * 注册店铺信息
     * @param shop 店铺
     * @param thumbnail 店铺图片与图片名称
     * @return
     * @throws ShopOperationException
     */

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {

        // 空值判断
        if (shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }

        try{

            // 给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            //添加店铺信息
          int effectedNum = shopDao.insertShop(shop);
          if (effectedNum <=0){

              throw new ShopOperationException("店铺创建失败");
          }else {

              if (thumbnail.getImage() !=null){

                  // 存储图片
                  try {
                      addShopImg(shop,thumbnail);

                  }catch (Exception e){
                      //使用RuntimeException 数据库可以事务回滚
                      throw new ShopOperationException("addShopImg error:" + e.getMessage());
                  }

                  // 更新店铺的图片地址
                  effectedNum = shopDao.updateShop(shop);
                  if (effectedNum <=0){
                      throw new ShopOperationException("更新图片地址失败");
                  }

                  //执行增加shopAuthMap操作
                  ShopAuthMap shopAuthMap = new ShopAuthMap();
                  shopAuthMap.setEmployee(shop.getOwner());
                  shopAuthMap.setShop(shop);
                  shopAuthMap.setTitle("店家");
                  shopAuthMap.setTitleFlag(0);
                  shopAuthMap.setCreateTime(new Date());
                  shopAuthMap.setLastEditTime(new Date());
                  shopAuthMap.setEnableStatus(1);
                  try{
                      effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                      if (effectedNum <=0){
                          throw new ShopOperationException("授权创建失败");
                      }
                  }catch (Exception e){
                      throw new ShopOperationException("insertShopAuthMap error:" +e.getMessage());
                  }
              }
          }

        }catch (Exception e){
            throw new ShopOperationException("addShop error:"+e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {

        if (shop ==null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else {

            // 1.判断是否需要处理图片
            try {
                if (thumbnail.getImage() !=null && thumbnail.getImageName() !=null
                        && !"".equals(thumbnail.getImageName())){

                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() !=null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg()); //删除图片
                    }
                    addShopImg(shop,thumbnail); //重新添加

                }

                //2.更新店铺信息

                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop); //更新店铺信息
                if (effectedNum <=0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    shop = shopDao.queryByShopId(shop.getShopId()); //通过shop id 查询店铺
                    return  new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modifyShop error:" +e.getMessage());
            }
        }


    }


    @Override
    public Shop getByShopId(Long shopId) {

        return shopDao.queryByShopId(shopId);
    }


    private void addShopImg(Shop shop, ImageHolder thumbnail){


        String dest = PathUtil.getShopImagePath(shop.getShopId()); //项目图片的子路径
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);  // 获取shop图片目录的相对值路径

        shop.setShopImg(shopImgAddr); //数据库保存图片的相对路径

    }








}
