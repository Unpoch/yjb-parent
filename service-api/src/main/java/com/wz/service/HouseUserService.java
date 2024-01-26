package com.wz.service;

import com.wz.entity.HouseUser;

import java.util.List;

public interface HouseUserService extends BaseService<HouseUser> {

    //根据房源id查询该房源的房东
    List<HouseUser> getHouseUserByHouseId(Long houseId);
}
