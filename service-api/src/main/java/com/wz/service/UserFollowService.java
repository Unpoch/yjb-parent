package com.wz.service;

import com.wz.entity.UserFollow;
import com.wz.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    //关注房源
    void follow(Long id, Long houseId);

    //查看是否关注该房源
    boolean isFollowed(Long id, Long id1);

    //分页查询我关注的房源
    PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long id);

    //取消关注房源
    void cancelFollowed(Long id);
}
