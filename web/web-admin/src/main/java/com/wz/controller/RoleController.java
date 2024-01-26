package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Role;
import com.wz.service.PermissionService;
import com.wz.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    public static final String SUCCESS_PAGE = "common/successPage";

    @Reference  //远程调用，所以是@Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    // @RequestMapping
    // public String index(Map<String, Object> map) {
    //     //调用RoleService
    //     List<Role> roleList = roleService.findAll();
    //     //将所有的角色放在request域中
    //     map.put("list", roleList);
    //     //去渲染数据的页面
    //     return "role/index";
    // }

    //分页及带条件查询的方法
    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String index(Map<String, Object> map, HttpServletRequest request) {
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filters放在request域中
        map.put("filters", filters);
        //调用roleService中分页及带条件查询的方法
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        //将pageInfo放在request域中
        map.put("page", pageInfo);
        return "role/index";
    }



    //去添加角色的页面
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String goAddPage() {
        return "role/create";
    }

    //添加角色
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/save")
    public String save(Role role) {
        //调用RoleService中添加的方法
        roleService.insert(role);
        //重定向到查询所有角色的方法
        // return "redirect:/role";

        //去common下的成功页面
        return SUCCESS_PAGE;
    }


    //删除角色
    @PreAuthorize("hasAuthority('role.delete')")
    @RequestMapping("/delete/{roleId}")
    public String delete(@PathVariable("roleId") Long roleId) {
        //调用roleService删除方法
        roleService.delete(roleId);
        //重定向到查询所有角色的方法
        return "redirect:/role";
    }

    //去修改页面的方法
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/edit/{roleId}")
    public String goEditPage(@PathVariable("roleId") Long roleId, Map<String, Object> map) {
        //调用RoleService中根据id查询的方法
        Role role = roleService.getById(roleId);
        //将查询到的角色放在Request域中
        map.put("role", role);
        //去修改页面
        return "role/edit";
    }


    //更新角色
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/update")
    public String update(Role role) {
        //调用RoleService中update方法
        roleService.update(role);
        //去common下的successPage页面
        return SUCCESS_PAGE;
    }

    //去分配权限的页面
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignShow/{roleId}")
    public String goAssignShowPage(@PathVariable("roleId") Long roleId, Map<String, Object> map) {
        //将角色id放到request域中
        map.put("roleId", roleId);
        //调用PermissionService中根据角色id获取权限的方法
        List<Map<String, Object>> zNodes = permissionService.findPermissionsByRoleId(roleId);
        //将zNodes放到request域中
        map.put("zNodes", zNodes);
        return "role/assignShow";
    }

    //分配权限
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,@RequestParam("permissionIds") Long[] permissionIds) {
        //调用PermissionService中分配权限的方法(先删除所有已分配的权限，然后根据permissionIds分配权限)
        permissionService.assignPermission(roleId,permissionIds);
        return "common/successPage";
    }

}
