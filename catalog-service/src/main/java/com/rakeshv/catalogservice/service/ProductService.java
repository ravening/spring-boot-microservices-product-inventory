package com.rakeshv.catalogservice.service;

import java.util.List;
import java.util.Optional;

import com.rakeshv.catalogservice.models.Product;

/**
 * ProductService
 */
public interface ProductService {

    public List<Product> findAllProducts();
    public Optional<Product> findProductByCode(String code);
}