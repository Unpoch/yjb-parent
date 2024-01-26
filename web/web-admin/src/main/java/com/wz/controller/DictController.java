package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Dict;
import com.wz.result.Result;
import com.wz.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    //去展示数据字典的页面
    @RequestMapping
    public String index() {
        return "dict/index";
    }

    //获取数据字典中的数据
    @ResponseBody //为了直接想数据响应过去，渲染
    @RequestMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id) {
        //调用DictService中查询数据字典中数据的方法
        List<Map<String,Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }


    //根据父id获取所有子节点
    @ResponseBody
    @RequestMapping("/findListByParentId/{areaId}")
    //community下的请求 /dict/findListByParentId/，即选择了一个区域后
    //显示该区域下的板块，那么就需要根据父id查询所有子节点的方法
    public Result findListByParentId(@PathVariable("areaId") Long areaId) {
        //调用dictService中根据父id查询所有子节点的方法
        List<Dict> listByParentId = dictService.findListByParentId(areaId);
        return Result.ok(listByParentId);
    }


}
