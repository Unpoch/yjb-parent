package com.wz;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiniuTest {

    /*
     * 测试向七牛云上传文件
     *
     * Zone.zone2() -> 华南
     */
    @Test
    public void testUpload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        //去个人中心，密钥管理中将AK和SK拷贝过来
        String accessKey = "OqsvP5opPIaEqSx3C-bM95kLrp-QqQhF4TWvZ3yP";//AK
        String secretKey = "hsTLt84YA-DoDNTH8eKiLmW_1AvQh5lODS-_njI4";//SK
        //空间的名字
        String bucket = "shf-files";
        //设置本地的文件的路径
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
        String localFilePath = "D:\\imgaes\\preview.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //设置文件名字
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }

    }
}
