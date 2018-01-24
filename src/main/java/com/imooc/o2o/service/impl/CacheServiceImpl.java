package com.imooc.o2o.service.impl;

import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Administrator on 2018/1/1.
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private JedisUtil.Keys jedisKeys;


    public void removeFromCache(String keyPrefix) {

        Set<String> keySet = jedisKeys.keys(keyPrefix+"*");
        for (String key : keySet){
            jedisKeys.del(key);
        }
    }


}
