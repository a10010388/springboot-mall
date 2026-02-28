package com.sam.springbootmall.dao;

import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
