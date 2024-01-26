package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.*;
import com.wz.result.Result;
import com.wz.service.*;
import com.wz.vo.HouseQueryVo;
import com.wz.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private UserFollowService userFollowService;

    //分页及带条件查询的方法
    @RequestMapping("/list/{pageName}/{pageSize}")
    public Result findPageList(@PathVariable("pageName") Integer pageName, @PathVariable("pageSize") Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo) {
        //调用HouseService中前端分页及带条件查询的方法
        PageInfo<HouseVo> pageInfo = houseService.findPageList(pageName, pageSize, houseQueryVo);
        return Result.ok(pageInfo);
    }

    //查询房源详情
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id, HttpSession session) {
        //调用HouseService中查询房源的方法
        House house = houseService.getById(id);
        //获取小区信息
        Community community = communityService.getById(house.getCommunityId());
        //获取房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(id, 1);
        //获取房源的经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokersByHouseId(id);
        //创建一个Map
        HashMap<Object, Object> map = new HashMap<>();
        //将房源信息，小区信息，房源图片，经纪人信息放到map中
        map.put("house", house);
        map.put("community", community);
        map.put("houseImage1List", houseImage1List);
        map.put("houseBrokerList", houseBrokerList);
        //查询是否关注房源
        //设置一个变量
        boolean isFollowed = false;
        //从Session域中获取UserInfo对象
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        if(userInfo != null) {
            //证明已经登录，调用UserFollowService中查看是否关注该房源的方法
            isFollowed = userFollowService.isFollowed(userInfo.getId(),id);
        }
        //将isFollowed放到map中
        map.put("isFollow",isFollowed);
        return Result.ok(map);
    }
}
