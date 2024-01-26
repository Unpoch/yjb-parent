package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.*;
import com.wz.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseUserService houseUserService;

    //分页及带条件查询的方法
    @RequestMapping
    public String index(Map<String, Object> map, HttpServletRequest request) {
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filters放在request域中
        map.put("filters", filters);
        //调用HouseService中分页及带条件查询的方法
        PageInfo<House> info = houseService.findPage(filters);
        //将PageInfo对象放在request域中
        map.put("page", info);
        //看房源管理的index页面，可知需要携带以下数据
        //将小区及数据字典中的数据放在request域中
        setRequestAttribute(map);
        return "house/index";
    }

    //去添加页面
    @RequestMapping("/create")
    public String goAddPage(Map<String, Object> map) {
        //create.html页面需要以下数据，因此需要携带
        //将小区及数据字典中的数据放在request域中
        setRequestAttribute(map);
        return "house/create";
    }


    //添加房源
    @RequestMapping("/save")
    public String save(House house) {
        //调用HouseService保存的方法
        houseService.insert(house);
        return "common/successPage";
    }

    //去修改页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id, Map<String, Object> map) {
        //调用HouseService中查询的方法
        House house = houseService.getById(id);
        //将house放在request域中
        map.put("house", house);
        setRequestAttribute(map);
        return "house/edit";
    }

    //更新
    @RequestMapping("/update")
    public String update(House house) {
        //调用HouseService更新的方法
        houseService.update(house);
        return "common/successPage";
    }

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        //调用HouseService删除的方法
        houseService.delete(id);
        return "redirect:/house";
    }

    //发布和取消发布
    @RequestMapping("/publish/{houseId}/{status}")
    public String publish(@PathVariable("houseId") Long houseId, @PathVariable("status") Integer status) {
        //调用HouseService中发布或取消发布的方法
        houseService.publish(houseId, status);
        //重定向到查询房源的方法
        return "redirect:/house";
    }

    //查看房源详情
    @RequestMapping("/{houseId}")
    public String show(@PathVariable("houseId") Long houseId, Map<String, Object> map) {
        //调用HouseService根据id查询房源的方法
        House house = houseService.getById(houseId);
        //将房源信息放在request域中
        map.put("house", house);
        //调用CommunityService根据小区id查询小区的方法
        Community community = communityService.getById(house.getCommunityId());
        //将小区信息放在request中
        map.put("community", community);
        //查询房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(houseId, 1);
        //房产图片
        List<HouseImage> houseImage2List = houseImageService.getHouseImageByHouseIdAndType(houseId, 2);
        //查询经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokersByHouseId(houseId);
        //查询房东
        List<HouseUser> houseUserList = houseUserService.getHouseUserByHouseId(houseId);
        //放到request域中
        map.put("houseImage1List",houseImage1List);
        map.put("houseImage2List",houseImage2List);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseUserList",houseUserList);
        return "house/show";
    }


    //提取
    //将所有小区及数据字典中的数据放在request域中的方法
    private void setRequestAttribute(Map<String, Object> map) {
        //获取所有的小区
        List<Community> communityList = communityService.findAll();
        //获取所有户型(在数据字典里hse_dict)
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        //获取楼层
        List<Dict> floorList = dictService.findListByDictCode("floor");
        //获取建筑结构
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        //获取朝向情况
        List<Dict> directionList = dictService.findListByDictCode("direction");
        //获取装修情况
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        //获取房屋用途
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        //将以上信息放到request域中
        map.put("communityList", communityList);
        map.put("houseTypeList", houseTypeList);
        map.put("floorList", floorList);
        map.put("buildStructureList", buildStructureList);
        map.put("directionList", directionList);
        map.put("decorationList", decorationList);
        map.put("houseUseList", houseUseList);
    }
}
