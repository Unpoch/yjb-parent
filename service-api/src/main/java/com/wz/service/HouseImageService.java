package com.wz.service;

import com.wz.entity.HouseImage;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {

    //根据房源id和图片类型查询房源或房产图片(传1查房源，传2查房产)
    List<HouseImage> getHouseImageByHouseIdAndType(Long houseId, Integer type);
}
