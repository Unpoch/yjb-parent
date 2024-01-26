package com.wz.service;

import com.wz.entity.UserInfo;

public interface UserInfoService extends BaseService<UserInfo> {
    UserInfo getUserInfoByPhone(String phone);
}
