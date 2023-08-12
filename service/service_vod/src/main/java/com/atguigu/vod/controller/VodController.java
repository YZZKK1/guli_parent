package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.ConstantVodUtils;
import com.atguigu.vod.Utils.InitVodClient;
import com.atguigu.vod.service.VodService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eduvod/video")

public class VodController {
    @Autowired
    private VodService vodService;


    @PostMapping("uploadAlyiVideo")
    public R uploadAlyiVideo(MultipartFile file){
        String videoId=vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id){
        try{
            DefaultAcsClient  client=InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request=new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok();


        }catch(Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");


        }

    }
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();

    }

    @GetMapping("getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable String videoId) throws Exception {
        System.out.print(videoId);
//获取阿里云存储相关常量
        String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;
//初始化
        DefaultAcsClient client =InitVodClient.initVodClient(accessKeyId, accessKeySecret);
//请求
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

        request.setVideoId(videoId);
//响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
//得到播放凭证
        String playAuth = response.getPlayAuth();
//返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);}












}
