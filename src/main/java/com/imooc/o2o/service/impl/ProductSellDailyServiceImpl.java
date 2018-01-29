package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductSellDailyDao;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {

        log.info("Quartz Running!");
        //统计在tb_user_product_map 里面产生销量的每个店铺的各件商品的日销量
        productSellDailyDao.insertDefaultProductSellDaily();
        //统计余下的商品的日销量，全部置为0（为了迎合echarts的数据请求）
        productSellDailyDao.insertProductSellDaily();

    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {

        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }
}
