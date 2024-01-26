package com.wz.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Admin;
import com.wz.service.AdminService;
import com.wz.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component  //这个类也需要创建对象，因此加入一个普通组件注解
public class UserDetailService implements UserDetailsService {
//将该类当成一个Controller，处理登陆的请求

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    //登陆时，SpringSecurity会自动调用该方法，并将用户名传入到该方法中
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用AdminService中根据用户名查询Admin对象的方法
        Admin admin = adminService.getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        /*
        给用户授权
            权限有两种标识方式：
                1.通过角色的方式表示，例如：ROLE_ADMIN
                2.直接设置权限，例如：Delete、Query、Update  (自己编辑的状态码)
         */
        // 调用PermissionService获取当前权限码的方法(code)
        List<String> permissionCodes = permissionService.getPermissionCodesByAdminId(admin.getId());
        //遍历得到每一个权限码
        //创建一个用于授权的集合
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String code : permissionCodes) {
            //创建GrantedAuthority对象
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(code);
            //将SimpleGrantedAuthority对象放到grantedAuthorities集合中
            grantedAuthorities.add(simpleGrantedAuthority);
        }
        //admin对象是数据库中查询出来的，密码是密文
        // return new User(username, admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        return new User(username, admin.getPassword(), grantedAuthorities);
    }
}
