package com.imooc.o2o.service;

/**
 * Created by Administrator on 2018/1/1.
 */
public interface CacheService {


    /**
     * 依据key前缀删除匹配该模式下的所有key-value 如传入：shopcategory，则shopcategory_allfirstlevel等
     * 以shopcategory打头的key_value都会被清空
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);

}
