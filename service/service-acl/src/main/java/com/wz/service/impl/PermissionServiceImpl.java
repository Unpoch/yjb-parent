package com.wz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.BaseDao;
import com.wz.dao.PermissionDao;
import com.wz.dao.RolePermissionDao;
import com.wz.entity.Permission;
import com.wz.helper.PermissionHelper;
import com.wz.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionsByRoleId(Long roleId) {
        //获取所有的权限
        List<Permission> permissionList = permissionDao.findAll();
        //根据角色id查询已分配的权限id
        List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleId(roleId);
        //创建返回的list
        List<Map<String, Object>> returnList = new ArrayList<>();
        //遍历所有的权限
        for (Permission permission : permissionList) {
            //map的格式 { id:1, pId:0, name:"随意勾选 1", checked:true},
            //创建一个map
            Map<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            //判断当前权限的id是否存在permissionIds中
            if (permissionIds.contains(permission.getId())) {
                //证明该权限是已经分配的权限
                map.put("checked", true);
            }
            //将map放到list中
            returnList.add(map);
        }
        return returnList;
    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //调用RolePermissionDao中根据角色id删除已分配权限的方法
        rolePermissionDao.deletePermissionIdsByRoleId(roleId);
        //遍历权限id
        for (Long permissionId : permissionIds) {
            if (permissionId != null) {
                //调用RolePermissionDao中保存权限id和角色id的方法
                rolePermissionDao.addRoleIdAndPermissionId(roleId, permissionId);
            }
        }
    }

    @Override
    public List<Permission> getMenuPermissionsByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        //判断是否是系统管理员
        if (adminId == 1) {
            //证明是系统管理员，获取所有的权限
            permissionList = permissionDao.findAll();
        } else {
            //调用PermissionDao中根据用户id查询所有权限菜单的方法
            permissionList = permissionDao.getMenuPermissionsByAdminId(adminId);
        }
        //通过PermissionHelper工具类将List转换为树形结构
        List<Permission> treeList = PermissionHelper.bulid(permissionList);
        return treeList;
    }

    @Override
    public List<Permission> findAllMenu() {
        //调用PermissionDao获取权限列表
        List<Permission> permissionList = permissionDao.findAll();
        if (permissionList.isEmpty()) return null;
        //将permissionList转换为树形结构返回
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    @Override
    public List<String> getPermissionCodesByAdminId(Long id) {
        List<String> permissionCodes = null;
        if (id == 1) {
            //系统管理员
            permissionCodes = permissionDao.getAllPermissionCodes();
        }else {
            //根据用户id查询权限码
            permissionCodes = permissionDao.getPermissionCodesByAdminId(id);
        }
        return permissionCodes;
    }
}
