package com.imooc.o2o.service;

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

}
