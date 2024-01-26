package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.UserInfo;
import com.wz.result.Result;
import com.wz.service.UserFollowService;
import com.wz.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    //关注房源
    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpSession session) {
        //获取UserInfo对象
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        //调用UserFollowService中关注房源的方法
        userFollowService.follow(userInfo.getId(), houseId);
        return Result.ok();
    }

    //查询我的关注房源
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result myFollow(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize, HttpSession session) {
        //从session域中获取userInfo对象
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        //调用UserFollowService中分页查询的方法
        PageInfo<UserFollowVo> pageInfo = userFollowService.findPageList(pageNum, pageSize, userInfo.getId());
        return Result.ok(pageInfo);
    }

    //取消关注
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollowed(@PathVariable("id") Long id) {
        //调用UserFollowService中取消关注的方法
        userFollowService.cancelFollowed(id);
        return Result.ok();
    }
}
