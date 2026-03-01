package com.sam.springbootmall.service;

import com.sam.springbootmall.dao.ProductParms;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(ProductParms  productParms);

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);

    Integer countProduct(ProductParms productParms);
}
