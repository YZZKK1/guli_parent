package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.atguigu.commonutils.JwtUtils.getMemberIdByJwtToken;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-07
 */
@CrossOrigin
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        String orderNo = orderService.createOrder(courseId, getMemberIdByJwtToken(request));
        return R.ok().data("orderId", orderNo);


    }

    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<Order>();
        wrapper.eq("order_no", orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);

    }
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,
                               @PathVariable String memberId) { //订单状态是1表示支付成功
        int count = orderService.count(new QueryWrapper<Order>().eq("member_id",
                memberId).eq("course_id", courseId).eq("status", 1));
        if(count>0) {
            return true;
        } else {
            return false;
        } }
}

