package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.controller.OrderController;
import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dao.UserDao;
import com.sam.springbootmall.dto.BuyItem;
import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.model.User;
import com.sam.springbootmall.service.OrderService;
import com.sam.springbootmall.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        User user = userDao.getUserById(userId);
        if (user == null) {
            logger.warn("userid not exist:{}",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }



        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            if (product == null) {
                logger.warn("product not exist:{}",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                logger.warn("product stock not enough:{}",buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProductId(),product.getStock() - buyItem.getQuantity());

            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId,totalAmount);
        logger.info("orderId:{}",orderId);
        orderDao.createOrderItem(orderId,orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(orderId);

        order.setOrderItemList(orderItemList);
        return order;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrderList(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);
        for(Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }
}
