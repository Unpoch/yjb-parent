package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.UserInfo;
import com.wz.result.Result;
import com.wz.result.ResultCodeEnum;
import com.wz.service.UserInfoService;
import com.wz.util.MD5;
import com.wz.vo.LoginVo;
import com.wz.vo.RegisterVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    //发送验证码
    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone, HttpServletRequest request) {
        //设置验证码为8888
        String code = "8888";
        //将验证码放到session域中
        request.getSession().setAttribute("code", code);
        //将验证码响应到前端
        return Result.ok(code);
    }


    //注册
    @RequestMapping("/register")
    //@RequestBody RegisterVo registerVo 将请求体转换为对象
    public Result register(@RequestBody RegisterVo registerVo, HttpSession session) {
        //获取手机号，密码，昵称和验证码
        String phone = registerVo.getPhone();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();
        //验空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickName) || StringUtils.isEmpty(code)) {
            //返回参数错误的一个消息
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        //从Session中获取验证码
        String sessionCode = (String) session.getAttribute("code");
        //判断验证码是否正确
        if (!code.equals(sessionCode)) {
            //返回验证码错误的消息
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //调用UserInfoService中的方法判断该手机号是否已经注册
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
        if (userInfo != null) {
            //返回手机号已经注册的消息
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //可以注册，创建一个UserInfo对象，插入到数据库中
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setPhone(phone);
        userInfo1.setNickName(nickName);
        userInfo1.setPassword(MD5.encrypt(password));//加密
        userInfo1.setStatus(1);//正常状态
        //调用UserInfoService中插入用户的方法
        userInfoService.insert(userInfo1);
        return Result.ok();
    }

    //登录
    @RequestMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpSession session) {
        //获取手机号和密码
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();
        //验空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        //根据手机号查询用户信息
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
        if (userInfo == null) {
            //账号不正确
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //验证密码是否正确
        if (!MD5.encrypt(password).equals(userInfo.getPassword())) {
            //密码不正确
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //判断用户是否被锁定
        if (userInfo.getStatus() == 0) {
            //用户被锁定
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //登录成功
        //将用户信息放到Session域中(目的是为了能在后台判断用户是否是登录的)
        session.setAttribute("user", userInfo);
        //创建一个Map,map必须存放设置nickName的key，值是用户的昵称
        //login.html页面要根据用户信息显示用户的昵称
        HashMap<Object, Object> map = new HashMap<>();
        map.put("nickName", userInfo.getNickName());
        map.put("phone", phone);
        return Result.ok(map);
    }

    //登出
    @RequestMapping("/logout")
    public Result logout(HttpSession session) {
        //将session中的UserInfo对象移除
        session.removeAttribute("user");
        return Result.ok();
    }
}
