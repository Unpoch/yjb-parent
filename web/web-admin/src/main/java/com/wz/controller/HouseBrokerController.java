package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Admin;
import com.wz.entity.HouseBroker;
import com.wz.service.AdminService;
import com.wz.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

    @Reference
    private AdminService adminService;

    @Reference
    private HouseBrokerService houseBrokerService;

    //去添加经纪人的页面
    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Map<String, Object> map) {
        //将房源的id放在request域中
        map.put("houseId", houseId);
        //调用AdminService获取所有用户的方法
        List<Admin> adminList = adminService.findAll();
        //将所有用户放到request域中
        map.put("adminList", adminList);
        return "houseBroker/create";
    }


    //保存经纪人
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker) {
        //调用AdminService中方法，查询经纪人的完整信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());
        //调用HouseBrokerService中保存的方法
        houseBrokerService.insert(houseBroker);
        //去成功页面
        return "common/successPage";
    }


    //去修改页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //调用HouseBrokerService中根据id查询经纪人的方法
        HouseBroker houseBroker = houseBrokerService.getById(id);
        map.put("houseBroker", houseBroker);
        //调用AdminService获取所有用户的方法
        List<Admin> adminList = adminService.findAll();
        //将所有用户放到request域中
        map.put("adminList", adminList);
        return "houseBroker/edit";
    }

    //更新经纪人
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker) {
        //调用AdminService中方法，查询经纪人的完整信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());
        //调用HouseBrokerService中更新的方法
        houseBrokerService.update(houseBroker);
        return "common/successPage";
    }

    //删除经纪人
    @RequestMapping("/delete/{houseId}/{brokerId}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("brokerId") Long brokerId) {
        //调用HouseBrokerService删除的方法
        houseBrokerService.delete(brokerId);
        //重定向到查询房源详情的方法(houseId就是用于重定向的)
        return "redirect:/house/" + houseId;//请求发到HouseController
    }
}
