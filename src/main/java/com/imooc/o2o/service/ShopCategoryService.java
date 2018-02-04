package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopCategoryExecution;
import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

/**
 * //店铺类别列表
 */
public interface ShopCategoryService {

    public static final String SCLISTKEY ="shopcategorylist";

    /**
     * 根据查询条件获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);


    /**
     * 添加店铺类别，并存储店铺类别图片
     * @param shopCategory
     * @param thumbnail
     * @return
     */
    ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);


    /**
     * 修改店铺类别
     * @param shopCategory
     * @param thumbnail
     * @return
     */
    ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory,ImageHolder thumbnail);


    /**
     * 根据Id返回店铺类别信息
     * @param shopCategoryId
     * @return
     */
    ShopCategory getShopCategoryById(Long shopCategoryId);



}
