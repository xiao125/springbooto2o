package com.imooc.o2o.service;

import com.imooc.o2o.dto.AreaExecution;
import com.imooc.o2o.entity.Area;

import java.util.List;

/**
 * Created by Administrator on 2017/11/26.
 */
public interface AreaService {

    public static final String AREALISTKEY= "arealist";

    /**
     * 获取区域列表，优先从缓存获取
     * @return
     */
    List<Area> getAreaList();

    /**
     * 增加区域信息
     * @param area
     * @return
     */
    AreaExecution addArea(Area area);


    /**
     * 修改区域信息
     * @param area
     * @return
     */
    AreaExecution modifyArea(Area area);


}
