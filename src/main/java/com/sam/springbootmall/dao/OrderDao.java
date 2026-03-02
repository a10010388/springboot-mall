package com.sam.springbootmall.dao;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId,Integer totalAmount);

    void createOrderItem(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemByOrderId(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

}
