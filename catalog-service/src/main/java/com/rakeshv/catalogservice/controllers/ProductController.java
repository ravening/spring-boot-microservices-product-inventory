package com.rakeshv.catalogservice.controllers;

import java.util.List;

import com.rakeshv.catalogservice.exception.ProductNotFoundException;
import com.rakeshv.catalogservice.models.Product;
import com.rakeshv.catalogservice.service.ProductService;
import com.rakeshv.catalogservice.service.ProductServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ProductController
 */
@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {
    private final ProductServiceImpl productService;
    @Autowired
    public ProductController(ProductServiceImpl serviceImpl) {
        this.productService = serviceImpl;
    }

    @GetMapping(value="")
    public List<Product> allProducts() {
        return productService.findAllProducts();
    }
    
    @GetMapping("/{code}")
    public Product getProductByCode(@PathVariable String code) {
        log.info("Searching for the product in catalog service with code {}", code);
        return productService.findProductByCode(code)
            .orElseThrow(() ->
            new ProductNotFoundException("Product with code " + code + " doesnt exists"));
    }
}