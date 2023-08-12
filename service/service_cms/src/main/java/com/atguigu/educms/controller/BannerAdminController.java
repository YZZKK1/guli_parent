package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-01
 */
@RestController
@CrossOrigin
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {
    @Autowired
    private CrmBannerService bannerService;
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page, @PathVariable long limit){
      Page<CrmBanner>pageBanner=new Page<>(page,limit);
      bannerService.page(pageBanner,null);
      return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }
    @PostMapping("addBanner")
    public R save(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();}

    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }


}

