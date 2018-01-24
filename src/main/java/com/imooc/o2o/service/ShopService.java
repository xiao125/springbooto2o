package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

/**
 * Created by Administrator on 2017/11/29.
 */
public interface ShopService {


    /**
     * 根据shopCondition分页返回相应店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
     ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);



    /**
     * 注册店铺信息，包括图片处理
     * @param shop
     * @param
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;


    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @return
     * @throws ShopOperationException
     */
    ShopExecution  modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;


    /**
     * 通过店铺Id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(Long shopId);


}
