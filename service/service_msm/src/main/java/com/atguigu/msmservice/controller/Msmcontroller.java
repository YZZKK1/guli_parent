package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/edumsm/msm")
public class Msmcontroller {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    @GetMapping(value = "/send/{phone}")
    public R code(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) return R.ok();
        code="8888";

        boolean isSend = msmService.send(phone);
        if(isSend) {
            redisTemplate.opsForValue().set(phone, code,5,TimeUnit.MINUTES);

            return R.ok();
        } else {
            return R.error().message("发送短信失败"); }
    }
}
