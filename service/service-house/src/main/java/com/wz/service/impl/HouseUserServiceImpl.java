package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.HouseUserDao;
import com.wz.entity.HouseUser;
import com.wz.service.HouseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = HouseUserService.class)
@Transactional
public class HouseUserServiceImpl extends BaseServiceImpl<HouseUser> implements HouseUserService {


    @Autowired
    private HouseUserDao houseUserDao;

    @Override
    public List<HouseUser> getHouseUserByHouseId(Long houseId) {
        return houseUserDao.getHouseUserByHouseId(houseId);
    }

    @Override
    protected BaseDao<HouseUser> getEntityDao() {
        return houseUserDao;
    }
}
