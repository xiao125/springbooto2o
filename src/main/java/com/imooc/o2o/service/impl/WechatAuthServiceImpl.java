package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.exceptions.WechatAuthOperationException;
import com.imooc.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/27.
 */
@Service
public class WechatAuthServiceImpl  implements WechatAuthService {

    private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);

    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Autowired
    private PersonInfoDao personInfoDao;



    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }


    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {

           if (wechatAuth == null || wechatAuth.getOpenId() == null){
               return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
           }

           try {

               //设置创建时间
               wechatAuth.setCreateTime(new Date());
               //
               if (wechatAuth.getPersonInfo() !=null && wechatAuth.getPersonInfo().getUserId() == null){

                   try {

                       wechatAuth.getPersonInfo().setCreateTime(new Date());
                       wechatAuth.getPersonInfo().setEnableStatus(1);
                       PersonInfo personInfo = wechatAuth.getPersonInfo();
                       //添加用户
                       int effectedNum = personInfoDao.insertPersonInfo(personInfo);

                       if (effectedNum <=0){
                           throw new WechatAuthOperationException("添加用户信息失败");
                       }
                   }catch (Exception e){
                       log.error("insertPersonInfo error:" + e.toString());
                       throw new WechatAuthOperationException("insertPersonInfo error: " + e.getMessage());
                   }
               }

               //添加对应本平台的微信账号
               int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
               if (effectedNum <=0){
                   throw new WechatAuthOperationException("帐号创建失败");
               }else {

                   return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
               }

           }catch (Exception e){
               log.error("insertWechatAuth error:" + e.toString());
               throw new WechatAuthOperationException("insertWechatAuth error: " + e.getMessage());

           }

    }
}
