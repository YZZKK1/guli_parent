package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;


@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")//注意这里没有配置 @RestController @RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code);
        String accessTokenInfo;
        try {
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + accessTokenInfo);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }
        Gson gson = new Gson();
        HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
        String accessToken = (String) mapAccessToken.get("access_token");
        String openid = (String) mapAccessToken.get("openid");

        UcenterMember member = memberService.getOpenIdMember(openid);
        if(member == null){
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";


        String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

        String UserInfo;
        try {
            UserInfo = HttpClientUtils.get(userInfoUrl);
            System.out.println("resultUserInfo==========" + UserInfo);}
        catch (Exception e) {
            throw new GuliException(20001, "获取用户信息失败"); }


            HashMap mapUserInfo = gson.fromJson(UserInfo, HashMap.class);
            String nickname = (String) mapUserInfo.get("nickname");
            String headimgurl = (String) mapUserInfo.get("headimgurl");

                member = new UcenterMember();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                memberService.save(member);
        }

        String jwtToken = JwtUtils.getJwtToken(member.getId(),member.getNickname());

        return "redirect:http://localhost:3000?token="+jwtToken;



    }

    @GetMapping("login")
    public String getWxCode() {
        String baseUrl="https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s"+
                "&redirect_uri=%s"+
                "&response_type=code"+
                "&scope=snsapi_login"+
                "&state=%s"+
                "#wechat_redirect";

        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;;
        System.out.println(redirectUrl);//获取业务服务器重定向地址
        try {

        redirectUrl = URLEncoder.encode(redirectUrl, "utf-8"); //url编码
        } catch (Exception e) {
        }

        String state = "imhelen";
        System.out.println("state = " + state);


        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);


        return"redirect:"+url;

    }}





