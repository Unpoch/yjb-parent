package com.wz.dao;

import com.wz.entity.UserInfo;

public interface UserInfoDao extends BaseDao<UserInfo> {
    UserInfo getUserInfoByPhone(String phone);
}
