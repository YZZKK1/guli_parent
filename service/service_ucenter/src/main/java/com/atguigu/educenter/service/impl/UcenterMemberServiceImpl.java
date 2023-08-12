package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001,"error");
        }
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember momember = baseMapper.selectOne(wrapper);
        if(momember == null) {
            throw new GuliException(20001,"error");
        }
        if(!MD5.encrypt(password).equals(momember.getPassword())){
            throw new GuliException(20001,"error");
        }
        if(momember.getIsDisabled()) {
            throw new GuliException(20001,"error");
        }
        String token = JwtUtils.getJwtToken(momember.getId(), momember.getNickname());
        return token;

    }

    @Override
    public void register(RegisterVo registerVo) {
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"error");}
        String mobleCode =redisTemplate.opsForValue().get(mobile);
            if(!code.equals(mobleCode)) {
                throw new GuliException(20001,"error");
            }
        Integer count = baseMapper.selectCount(new
        QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(count.intValue() > 0) {
            throw new GuliException(20001,"error");
        }
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);



        }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember>wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member=baseMapper.selectOne(wrapper);
        return member;

    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }


}
