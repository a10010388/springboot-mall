package com.sam.springbootmall.dao.impl;

import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dao.ProductParms;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.rowmaaper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductParms productParms) {
        String sql = "select product_id,product_name, category," +
                "image_url, price, stock, description, created_date,last_modified_date " +
                " from product  " +
                " where 1 = 1" ;
        Map<String, Object> params = new HashMap<>();
        if(productParms.getCategory()!=null){
            sql = sql + " and category = :productCategory";
            params.put("productCategory",productParms.getCategory().name());
        }
        if(productParms.getSearch()!=null){
            sql = sql + " and product_name like :search";
            params.put("search","%"+productParms.getSearch()+"%");
        }

        sql = sql + " order by " +productParms.getOrderBy() + " " +productParms.getSort();

        List<Product> productList = namedParameterJdbcTemplate.query(sql, params, new ProductRowMapper());

        return productList;
    }


    @Override
    public Product getProductById(Integer productId) {
        String sql = "select product_id,product_name, category, " +
                "image_url, price, stock, description, created_date," +
                " last_modified_date " +
                "from product where product_id = :productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if(!productList.isEmpty()){
            return productList.get(0);
        }else {
            return null;
        }

    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "insert into product (product_name, category, image_url, price, stock," +
                " description, created_date, last_modified_date) " +
                "values (:productName, :category, :imageUrl, :price, " +
                ":stock, :description, :createdDate, :lastModifiedDate)";
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "update product set product_name = :productName ," +
                "category = :category,image_url = :imageUrl," +
                "price = :price,stock = :stock,description = :description," +
                "last_modified_date = :lastModifiedDate " +
                "where product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate",new Date());
        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public void deleteProductById(Integer productId) {
    String sql = "delete product from product where product_id = :productId";
    Map<String, Object> map = new HashMap<>();
    map.put("productId", productId);
    namedParameterJdbcTemplate.update(sql,map);

    }


}
