package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */
@RestController
@CrossOrigin
@RequestMapping("/educenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @PostMapping("login")
    public R login(@RequestBody UcenterMember member) {
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }
    @GetMapping("getMemberInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember member = memberService.getById(memberId);
            return R.ok().data("userInfo", member);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"error");
        } }

    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
//根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(id);
        UcenterMemberOrder memeber = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,memeber);
        return memeber;
    }
    @GetMapping(value = "countRegister/{day}")
    public R countRegister(
            @PathVariable String day){
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister", count);
    }





}

