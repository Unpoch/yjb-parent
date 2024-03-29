package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.entity.HouseImage;
import com.wz.result.Result;
import com.wz.service.HouseImageService;
import com.wz.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    @Reference
    private HouseImageService houseImageService;

    //去上传图片的页面
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String goUploadPage(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Map<String, Object> map) {
        //将房源id和图片的类型放到request域中
        map.put("houseId", houseId);
        map.put("type", type);
        return "house/upload";
    }


    //上传房源或房产图片
    @ResponseBody
    @RequestMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] files) {

        //获取字节数组
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    //获取字节数组
                    byte[] bytes = file.getBytes();
                    //获取图片名字
                    String originalFilename = file.getOriginalFilename();
                    //通过uuid随机生成一个字符串上传到七牛云的图片的名字
                    String newFileName = UUID.randomUUID().toString();
                    //通过QiniuUtil工具类上传图片到七牛云
                    QiniuUtils.upload2Qiniu(bytes, newFileName);
                    //创建HouseImage对象
                    HouseImage houseImage = new HouseImage();
                    houseImage.setHouseId(houseId);
                    houseImage.setType(type);
                    houseImage.setImageName(originalFilename);
                    //设置图片路径：http://七牛云域名/随机生成的图片名字
                    houseImage.setImageUrl("http://s0wyzv3co.hn-bkt.clouddn.com/" + newFileName);
                    //调用HouseImageService中保存的方法
                    houseImageService.insert(houseImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    //删除房源或房产图片
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("id") Long id) {
        //调用HouseImageService中删除的方法
        houseImageService.delete(id);
        //重定向到查询房源详情的方法
        return "redirect:/house/" + houseId;
    }
}
