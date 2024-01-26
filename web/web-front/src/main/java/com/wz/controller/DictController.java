package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.Dict;
import com.wz.result.Result;
import com.wz.service.DictService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController // = Controller + ResponseBody
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    //为了处理异步请求(Axios){具体看index.html的fetchDictData()里的异步请求},要求返回Json格式的字符串
    //根据编码获取所有子节点
    @RequestMapping("/findListByDictCode/{dictCode}")
    public Result findListByDictCode(@PathVariable("dictCode") String dictCode) {
        //调用DictService根据编码获取子节点的方法
        List<Dict> listByDictCode = dictService.findListByDictCode(dictCode);
        return Result.ok(listByDictCode);
    }

    //根据父id查询是所有子节点的方法
    @RequestMapping("/findListByParentId/{areaId}")
    public Result findListByParentId(@PathVariable("areaId") Long areaId) {
        //调用DictService根据父id获取子节点的方法
        List<Dict> listByParentId = dictService.findListByParentId(areaId);
        return Result.ok(listByParentId);
    }

}
