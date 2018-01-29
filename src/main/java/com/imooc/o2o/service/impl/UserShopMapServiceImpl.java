package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;
import com.imooc.o2o.exceptions.UserShopMapOperationException;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {

    @Autowired
    private UserShopMapDao userShopMapDao;


    @Override
    public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {

        //空值判断
        if (userShopMapCondition !=null && pageIndex !=-1 && pageSize !=-1){
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            //
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition,pageIndex,
                    pageSize);
            //返回总数
            int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);

            UserShopMapExecution ue = new UserShopMapExecution();
            ue.setUserShopMapList(userShopMapList);
            ue.setCount(count);

            return ue;
        }else {

            return null;

        }
    }

    @Override
    public UserShopMap getUserShopMap(long userId, long shopId) {
        return userShopMapDao.queryUserShopMap(userId,shopId);
    }
}
