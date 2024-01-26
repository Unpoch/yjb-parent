package com.wz.service;

import com.wz.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerService extends BaseService<HouseBroker> {

    //根据房源id查询该房源的经纪人
    List<HouseBroker> getHouseBrokersByHouseId(Long houseId);
}
