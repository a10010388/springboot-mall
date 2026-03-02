package com.sam.springbootmall.service;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrderList(OrderQueryParams orderQueryParams);
}
