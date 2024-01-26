package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.UserFollowDao;
import com.wz.entity.UserFollow;
import com.wz.service.DictService;
import com.wz.service.UserFollowService;
import com.wz.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Reference
    private DictService dictService;

    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    @Override
    public void follow(Long id, Long houseId) {
        //创建一个UserFollow对象
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(id);
        userFollow.setHouseId(houseId);
        //调用UserFollowDao中添加的方法
        userFollowDao.insert(userFollow);
    }

    @Override
    public boolean isFollowed(Long userId, Long houseId) {
        //调用UserFollowDao中查询是否关注该房源的方法
        Integer count = userFollowDao.getCountByUserIdAndHouseId(userId, houseId);
        return count > 0;
    }

    @Override
    public PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用UserFollowDao中分页的方法
        Page<UserFollowVo> page = userFollowDao.findPageList(userId);
        //遍历page
        for (UserFollowVo userFollowVo : page) {
            //获取房屋的类型
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            //获取楼层
            String floorName = dictService.getNameById(userFollowVo.getFloorId());
            //获取朝向
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            //赋值
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(floorName);
            userFollowVo.setDirectionName(directionName);
        }
        return new PageInfo<>(page,5);
    }

    @Override
    public void cancelFollowed(Long id) {
        //调用UserFollowDao中删除的方法
        userFollowDao.delete(id);
    }
}
