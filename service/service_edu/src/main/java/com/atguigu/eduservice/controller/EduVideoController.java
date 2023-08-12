package com.atguigu.eduservice.controller;


import com.alibaba.excel.util.StringUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-02-20
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {
    @Autowired
    private VodClient vodClient;

    @Autowired
    private EduVideoService videoService;
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }


    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        EduVideo eduVideo=videoService.getById(id);
        String videoSourceId=eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeAlyVideo(videoSourceId);

        }
        videoService.removeById(id);
        return R.ok();
    }

}