package com.sam.springbootmall.controller;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.service.OrderService;
import com.sam.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    private static  final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        logger.info("userid:"+userId);
        Integer orderId = orderService.createOrder(userId,createOrderRequest);
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);
        orderQueryParams.setUserId(userId);
        List<Order> orderList = orderService.getOrderList(orderQueryParams);

        Integer count = orderService.countOrder(orderQueryParams);

        Page<Order> page = new Page();
        page.setTotal(count);
        page.setLimit(limit);
        page.setOffset(offset);
        page.setResults(orderList);
        return ResponseEntity.ok(page);

    }

}
