package com.wz.service;

import com.wz.entity.House;
import com.wz.vo.HouseQueryVo;
import com.wz.vo.HouseVo;
import com.github.pagehelper.PageInfo;

public interface HouseService extends BaseService<House> {
    //发布或取消发布
    void publish(Long houseId, Integer status);

    //前端分页及带条件查询的方法
    PageInfo<HouseVo> findPageList(Integer pageName, Integer pageSize, HouseQueryVo houseQueryVo);
}
