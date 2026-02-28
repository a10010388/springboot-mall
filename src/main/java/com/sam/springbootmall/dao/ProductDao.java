package com.sam.springbootmall.dao;

import com.sam.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

}
