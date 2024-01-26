package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Permission;
import com.wz.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    //去首页
    @RequestMapping
    public String index(Map<String, Object> map) {
        //调用PermissionService中获取所有权限菜单数据的方法
        List<Permission> list = permissionService.findAllMenu();
        //将list放到request域中
        map.put("list", list);
        return "permission/index";
    }

    //去新增菜单的页面
    @RequestMapping("/create") //带来的数据有parentId,parentName,type，封装成Permission(POJO入参)
    public String goAddPage(Map<String, Object> map, Permission permission) {
        //将数据放到request域中
        map.put("permission", permission);
        return "permission/create";
    }

    //添加菜单
    @RequestMapping("/save")
    public String save(Permission permission) {
        //调用PermissionService中添加的方法
        permissionService.insert(permission);
        //去成功页面
        return "common/successPage";
    }

    //去修改页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //数据回显
        //调用PermissionService中根据id查询权限的方法
        Permission permission = permissionService.getById(id);
        //将Permission对象放到request域中
        map.put("permission", permission);
        return "permission/edit";
    }

    //修改
    @RequestMapping("/update")
    public String update(Permission permission) {
        //调用PermissionService中修改的方法
        permissionService.update(permission);
        //去成功页面
        return "common/successPage";
    }

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        //调用PermissionService中删除的方法
        permissionService.delete(id);
        //重定向(发请求到刷新数据)
        return "redirect:/permission";
    }
}
