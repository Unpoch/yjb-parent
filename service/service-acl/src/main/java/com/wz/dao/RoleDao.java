package com.wz.dao;

import com.wz.entity.Role;

import java.util.List;

public interface RoleDao extends BaseDao<Role>{

    List<Role> findAll();
}
