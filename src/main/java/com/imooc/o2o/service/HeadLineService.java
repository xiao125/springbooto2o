package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */
public interface HeadLineService {

    public static final String HLIISTEKY = "headlinelist";


    /**
     * 根据传入的条件返回指定的头条列表
     * @param headLineCondition
     * @return
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;


}
