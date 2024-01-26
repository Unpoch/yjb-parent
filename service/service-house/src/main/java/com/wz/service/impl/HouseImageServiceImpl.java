package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.HouseImageDao;
import com.wz.entity.HouseImage;
import com.wz.service.HouseImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = HouseImageService.class)
@Transactional
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {

    @Autowired
    private HouseImageDao houseImageDao;

    @Override
    public List<HouseImage> getHouseImageByHouseIdAndType(Long houseId, Integer type) {
        return houseImageDao.getHouseImageByHouseIdAndType(houseId,type);
    }

    @Override
    protected BaseDao<HouseImage> getEntityDao() {
        return houseImageDao;
    }
}
