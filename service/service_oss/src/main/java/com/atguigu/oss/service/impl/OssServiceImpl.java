package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
            // 创建OSSClient实例。

            // 创建存储空间。
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();
            String fileName=file.getOriginalFilename();
            String uuid= UUID.randomUUID().toString().replace("-","");
            //文件按照日期分类
            String datePath= new DateTime().toString("yyyy/MM/dd");
            fileName=datePath+"/"+uuid+fileName;
            ossClient.putObject(bucketName,fileName,inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
