package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.CommunityDao;
import com.wz.dao.DictDao;
import com.wz.entity.Community;
import com.wz.service.CommunityService;
import com.wz.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommunityService.class)
@Transactional
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private DictDao dictDao;

    //重写分页的方法，目的是为小区中区域和板块赋值
    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);
        //调用CommunityDao中分页及带条件查询的方法
        Page<Community> page = communityDao.findPage(filters);
        //遍历page对象
        for (Community community : page) {
            //根据区域的id获取区域的名字
            String areaName  = dictDao.getNameById(community.getAreaId());
            //根据板块的id获取板块的名字
            String plateName = dictDao.getNameById(community.getPlateId());
            //给community对象的区域和板块赋值
            community.setAreaName(areaName);
            community.setPlateName(plateName);
        }
        return new PageInfo<>(page, 10);
    }

    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    }

    //重写该方法是为了详情中，小区的区域和板块的名字显示
    @Override
    public Community getById(Serializable id) {
        Community community = communityDao.getById(id);
        //根据区域的id获取区域的名字
        String areaName  = dictDao.getNameById(community.getAreaId());
        //根据板块的id获取板块的名字
        String plateName = dictDao.getNameById(community.getPlateId());
        //给community对象的区域和板块赋值
        community.setAreaName(areaName);
        community.setPlateName(plateName);
        return community;
    }
}
