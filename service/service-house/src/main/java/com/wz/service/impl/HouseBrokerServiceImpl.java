package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.HouseBrokerDao;
import com.wz.entity.HouseBroker;
import com.wz.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = HouseBrokerService.class)
@Transactional
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {


    @Autowired
    private HouseBrokerDao houseBrokerDao;

    @Override
    public List<HouseBroker> getHouseBrokersByHouseId(Long houseId) {
        return houseBrokerDao.getHouseBrokersByHouseId(houseId);
    }

    @Override
    protected BaseDao<HouseBroker> getEntityDao() {
        return houseBrokerDao;
    }
}
