package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Admin;
import com.wz.entity.Permission;
import com.wz.service.AdminService;
import com.wz.service.PermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;


    //去首页
    // @RequestMapping("/")
    // public String index() {
    //     return "frame/index";
    // }

    //去首页
    @RequestMapping("/")
    public String index(Map<String, Object> map) {
        //设置默认用户id
        // Long userId = 1L;//超级管理员
        //调用AdminService中查询的方法
        // Admin admin = adminService.getById(userId);

        //通过SpringSecurity获取User对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //调用AdminService中根据用户名获取Admin对象的方法
        Admin admin = adminService.getAdminByUsername(user.getUsername());
        //调用PermissionService中根据用户id获取其用户权限的方法
        List<Permission> permissionList = permissionService.getMenuPermissionsByAdminId(admin.getId());
        //将用户权限菜单放到request域中
        map.put("admin", admin);
        map.put("permissionList", permissionList);
        return "frame/index";
    }

    //去主页面main
    @RequestMapping("/main")
    public String main() {
        return "frame/main";
    }


    //去登陆页面
    @RequestMapping("/login")
    public String login() {
        return "frame/login";
    }

    //去没有权限提示页面
    @RequestMapping("/auth")
    public String auth() {
        return "frame/auth";
    }

}