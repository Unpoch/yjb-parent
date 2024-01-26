package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Community;
import com.wz.entity.Dict;
import com.wz.service.CommunityService;
import com.wz.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    //分页及带条件查询的方法
    @RequestMapping
    public String index(Map<String, Object> map, HttpServletRequest request) {
        //去首页，查询所有数据，分页展示
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filters放在request域中
        map.put("filters", filters);
        //调用CommunityService中分页的方法
        PageInfo<Community> pageInfo = communityService.findPage(filters);
        //将pageInfo放到request域中
        map.put("page", pageInfo);
        //根据编码获取北京所有的区
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        //将北京所有的区放在request域中
        map.put("areaList", areaList);
        return "community/index";
    }


    //去添加小区的页面
    @RequestMapping("/create")
    public String goAddPage(Map<String, Object> map) {
        //根据编码获取北京所有的区
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        //将北京所有的区放在request域中
        map.put("areaList", areaList);
        //北京所有的区这个数据响应过去
        return "community/create";
    }

    //添加小区
    @RequestMapping("/save")
    public String save(Community community) {
        //调用CommunityService添加的方法
        communityService.insert(community);
        //去成功页面
        return "common/successPage";
    }

    //去修改小区的页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //根据编码获取北京所有的区
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        //将北京所有的区放在request域中
        map.put("areaList", areaList);
        //调用CommunityService查询的方法
        Community community = communityService.getById(id);
        //将小区放到request域中（修改页面数据回显）
        map.put("community", community);
        return "community/edit";
    }

    //更新(修改)
    @RequestMapping("/update")
    public String update(Community community) {
        //调用CommunityService中更新的方法
        communityService.update(community);
        return "common/successPage";
    }

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        //调用CommunityService中删除的方法
        communityService.delete(id);
        //重定向，刷新页面，重新显示更新后的数据
        return "redirect:/community";
    }
}
