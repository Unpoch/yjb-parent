package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.AdminRoleDao;
import com.wz.dao.BaseDao;
import com.wz.dao.RoleDao;
import com.wz.entity.Role;
import com.wz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Map<String, Object> findRolesByAdminId(Long adminId) {
        //获取所有的角色
        List<Role> roleList = roleDao.findAll();
        //根据用户id 获取用户已拥有的角色的角色id(需要查acl_admin_role表)
        List<Long> roleIds = adminRoleDao.findRoleIdsByAdminId(adminId);
        //创建两个list(已拥有角色的和为拥有角色的) noAssginRoleList，assginRoleList
        List<Role> assginRoleList = new ArrayList<>();//已有角色
        List<Role> noAssginRoleList = new ArrayList<>();//未有角色
        //遍历所有角色
        for (Role role : roleList) {
            //判断当前角色id在集合roleIds中
            if (roleIds.contains(role.getId())) {//在
                assginRoleList.add(role);
            } else {//不在
                noAssginRoleList.add(role);
            }
        }
        //作为map返回
        Map<String, Object> map = new HashMap<>();
        map.put("assginRoleList", assginRoleList);
        map.put("noAssginRoleList", noAssginRoleList);
        return map;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //先根据用户id将已分配的角色删除
        adminRoleDao.deleteRoleIdsByAdminId(adminId);
        //遍历所有的角色id
        for (Long roleId : roleIds) {
            if(roleId != null) {
                //角色id和用户id插入到数据库中
                adminRoleDao.addRoleIdAndAdminId(roleId,adminId);
            }
        }
    }


    @Override
    protected BaseDao<Role> getEntityDao() {
        return this.roleDao;
    }
}
