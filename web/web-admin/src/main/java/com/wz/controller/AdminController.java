package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Admin;
import com.wz.service.AdminService;
import com.wz.service.RoleService;
import com.wz.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    public static final String SUCCESS_PAGE = "common/successPage";

    @Reference  //远程调用，所以是@Reference
    private AdminService adminService;

    @Reference
    private RoleService roleService;

    @Autowired  //在配置类WebSecurityConfig中用@Bean注解配置了一个密码加密器(也就是在IOC容器中)，因此这里可以注入
    private PasswordEncoder passwordEncoder;

    //分页及带条件的查询
    @RequestMapping
    public String findPage(Map<String, Object> map, HttpServletRequest request) {
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filters放在request域中
        map.put("filters", filters);
        //调用AdminService中分页的方法
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //将pageInfo对象放在request域中
        map.put("page", pageInfo);
        return "admin/index";
    }

    //去添加用户的页面
    @RequestMapping("/create")
    public String goAddPage() {
        return "admin/create";
    }

    //保存用户
    @RequestMapping("/save")
    public String save(Admin admin) {
        //对admin对象中密码进行加密(数据库中的密码都是密文保存)
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        //调用AdminService中保存的方法
        adminService.insert(admin);
        //去成功页面
        return SUCCESS_PAGE;
    }

    //删除用户
    @RequestMapping("/delete/{adminId}")
    public String delete(@PathVariable("adminId") Long adminId) {
        //调用AdminService中删除的方法
        adminService.delete(adminId);
        //重定向 -> 再查一遍
        return "redirect:/admin";
    }

    //去更新页面
    @RequestMapping("/edit/{adminId}")
    public String goEditPage(@PathVariable("adminId") Long adminId, Map<String, Object> map) {
        //调用AdminService中根据id查询一个对象的方法
        Admin admin = adminService.getById(adminId);
        //将Admin对象放在Request域,为了在edit.html页面做数据回显(响应数据)
        map.put("admin", admin);
        return "admin/edit";
    }

    //更新用户
    @RequestMapping("/update")
    public String update(Admin admin) {
        //调用AdminService中更新的方法
        adminService.update(admin);
        return SUCCESS_PAGE;
    }

    //去上传头像的页面
    @RequestMapping("/uploadShow/{id}")
    public String goUploadPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //将用户的id放到request域中
        map.put("id", id);
        return "admin/upload";
    }

    //上传头像
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        try {
            //调用AdminService中根据id查询用户的方法
            Admin admin = adminService.getById(id);
            //获取字节数组
            byte[] bytes = file.getBytes();
            //通过UUID随机生成一个文件名
            String fileName = UUID.randomUUID().toString();
            //通过QiniuUtils将文件上传到七牛云
            QiniuUtils.upload2Qiniu(bytes, fileName);
            //给用户设置头像地址
            admin.setHeadUrl("http://s0wyzv3co.hn-bkt.clouddn.com/" + fileName);
            //调用AdminService中更新的方法
            adminService.update(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //去成功页面
        return "common/successPage";
    }

    //去分配角色的页面
    @RequestMapping("/assignShow/{adminId}")
    public String goAssignShowPage(@PathVariable("adminId") Long adminId, ModelMap modelMap) {
        //assignShow.html页面需要数据：adminId noAssginRoleList assginRoleList
        //将用户的id放到request域中
        modelMap.addAttribute("adminId", adminId);
        //调用RoleService中根据用户id查询用户角色的方法
        Map<String, Object> rolesByAdminId = roleService.findRolesByAdminId(adminId);
        //这里的key有两个noAssginRoleList，assginRoleList，对应的value是list集合
        //将map放到request域中
        modelMap.addAllAttributes(rolesByAdminId);
        return "admin/assignShow";
    }

    //分配角色
    @RequestMapping("/assignRole")
    public String assignRole(Long adminId, Long[] roleIds) {
        //调用RoleService中分配角色的方法(先删除所分配的角色，再从roleIds中添加所有分配的角色)
        roleService.assignRole(adminId, roleIds);
        return "common/successPage";
    }
}
