package com.wz.dao;

import com.wz.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageDao extends BaseDao<HouseImage> {

    //根据房源id和图片类型查询房源或房产图片(传1查房源，传2查房产)
    List<HouseImage> getHouseImageByHouseIdAndType(@Param("houseId") Long houseId, @Param("type") Integer type);
}
