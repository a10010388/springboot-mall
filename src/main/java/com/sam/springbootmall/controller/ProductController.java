package com.sam.springbootmall.controller;

import com.sam.springbootmall.constant.ProductCategory;
import com.sam.springbootmall.dao.ProductParms;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.service.ProductService;
import com.sam.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("products")
    public ResponseEntity<Page<Product>> getProducts(
               @RequestParam(required = false) ProductCategory category,
               @RequestParam(required = false) String search,
               @RequestParam(defaultValue = "created_date") String orderBy,
               @RequestParam(defaultValue = "desc") String sort,
               @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
               @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductParms productParms = new ProductParms();
        productParms.setCategory(category);
        productParms.setSearch(search);
        productParms.setOrderBy(orderBy);
        productParms.setSort(sort);
        productParms.setLimit(limit);
        productParms.setOffset(offset);

        List<Product> productList = productService.getProducts(productParms);
        Integer total = productService.countProduct(productParms);
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {

        Product product = productService.getProductById(productId);
        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productService.updateProduct(productId,productRequest);
        Product updatedProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
    @DeleteMapping("products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
