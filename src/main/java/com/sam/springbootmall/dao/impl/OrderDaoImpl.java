package com.sam.springbootmall.dao.impl;

import com.sam.springbootmall.controller.OrderController;
import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.rowmaaper.OrderItemRowMapper;
import com.sam.springbootmall.rowmaaper.OrderRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static  final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "insert into `order` (user_id,total_amount,created_date,last_modified_date)" +
                " values (:userId, :totalAmount, :createdDate, :lastModifiedDate)";
        Map<String,Object> map = new HashMap<>();
        logger.info("service userId:"+userId);
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        Date date = new Date();
        map.put("createdDate", date);
        map.put("lastModifiedDate", date);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource( map ), keyHolder);
        int orderId = keyHolder.getKey().intValue();
        return orderId;
    }

    @Override
    public void createOrderItem(Integer orderId, List<OrderItem> orderItemList) {
        String sql = " insert into order_item(order_id,product_id,quantity,amount) " +
                " values (:orderId,:productId,:quantity,:amount)";
        MapSqlParameterSource [] mapSqlParameterSource = new MapSqlParameterSource[orderItemList.size()];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource();
            mapSqlParameterSource[i].addValue("orderId", orderId);
            mapSqlParameterSource[i].addValue("productId", orderItem.getProductId());
            mapSqlParameterSource[i].addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSource[i].addValue("amount", orderItem.getAmount());
        }
        namedParameterJdbcTemplate.batchUpdate(sql,mapSqlParameterSource);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "select order_id,user_id,total_amount," +
                "created_date,last_modified_date from `order` " +
                "where order_id = :orderId";
        Map<String,Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<Order> orderList = namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());
        if(orderList.isEmpty()){
            return null;
        }else{
            return orderList.get(0);
        }

    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
        String sql = "select oi.order_item_id,oi.order_id,oi.product_id,oi.quantity," +
                "oi.amount,p.product_name,p.image_url " +
                " from order_item as oi " +
                " left join product as p ON oi.product_id = p.product_id " +
                " where oi.order_id =:orderId";
        Map<String,Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql,map,new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "select count(*) from `order` where 1=1 ";
        Map<String,Object> map = new HashMap<>();

        sql = addFilterSql(sql,map,orderQueryParams);
        Integer total = namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);
        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "select order_id, user_id,total_amount ,created_date,last_modified_date" +
                " from `order` where 1=1 ";
        Map<String,Object> map = new HashMap<>();

        sql = addFilterSql(sql,map,orderQueryParams);

        sql = sql + " ORDER BY created_date DESC ";

        sql = sql + " LIMIT :limit OFFSET :offset ";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());
        List<Order> orderList = namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());
        return orderList;

    }

    private String addFilterSql(String sql,Map<String,Object> map,OrderQueryParams orderQueryParams){
        if(orderQueryParams!=null){
            sql = sql + "and user_id = :userId ";
            map.put("userId", orderQueryParams.getUserId());
        }
        return sql;
    }


}
