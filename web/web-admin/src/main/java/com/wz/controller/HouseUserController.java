package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.HouseUser;
import com.wz.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {

    @Reference
    private HouseUserService houseUserService;

    //去添加房东的页面
    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Map<String, Object> map) {
        //将房源id放到request域中
        map.put("houseId", houseId);
        return "houseUser/create";
    }

    //添加房东
    @RequestMapping("/save")
    public String save(HouseUser houseUser) {
        //调用HouseUserService中添加的方法
        houseUserService.insert(houseUser);
        return "common/successPage";
    }

    //去修改页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //调用HouseUserService中根据id查询的方法
        HouseUser houseUser = houseUserService.getById(id);
        map.put("houseUser", houseUser);
        return "houseUser/edit";
    }

    //更新
    @RequestMapping("/update")
    public String update(HouseUser houseUser) {
        //调用HouseUserService更新的方法
        houseUserService.update(houseUser);
        return "common/successPage";
    }

    //删除
    @RequestMapping("/delete/{houseId}/{houseUserId}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("houseUserId") Long houseUserId) {
        //调用HouseUserService删除的方法
        houseUserService.delete(houseUserId);
        //重定向到查询房源详情的方法
        return "redirect:/house/" + houseId;
    }
}
