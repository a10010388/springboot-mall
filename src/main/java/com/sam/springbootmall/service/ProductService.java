package com.sam.springbootmall.service;

import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
