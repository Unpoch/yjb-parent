package com.wz.service;

import com.wz.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionsByRoleId(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);

    List<Permission> getMenuPermissionsByAdminId(Long adminId);

    List<Permission> findAllMenu();

    List<String> getPermissionCodesByAdminId(Long id);
}
