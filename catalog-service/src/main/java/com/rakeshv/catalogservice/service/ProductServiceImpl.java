package com.rakeshv.catalogservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.rakeshv.catalogservice.models.Product;
import com.rakeshv.catalogservice.models.ProductInventoryResponse;
import com.rakeshv.catalogservice.repository.ProductRepository;
import com.rakeshv.catalogservice.utils.MyThreadLocalsHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * ProductServiceImpl
 */
@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private static final String INVENTORY_API_PATH = "http://inventory-service/api/";

    @Autowired
    public ProductServiceImpl(ProductRepository repository, RestTemplate template) {
        this.productRepository = repository;
        this.restTemplate = template;
    }

    @Override
    public List<Product> findAllProducts() {
        log.info("Fetching all the products");
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findProductByCode(String code) {
        log.info("Searching for the product with code {}", code);
        Optional<Product> productOptional = productRepository.findByCode(code);
        if (productOptional.isPresent()) {
            String correlationId = UUID.randomUUID().toString();
            MyThreadLocalsHolder.setCorrelationId(correlationId);
            log.info("Before CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
            log.info("Fetching the inventory details for product code : {}", code);
            ResponseEntity<ProductInventoryResponse> itemResponseEntity =
                restTemplate.getForEntity(INVENTORY_API_PATH + "inventory/{code}",
                                            ProductInventoryResponse.class, code);
            log.info("response is {}", itemResponseEntity.getBody());
            log.info("Status is {}", itemResponseEntity.getStatusCode());
            if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
                Integer quantity = itemResponseEntity.getBody().getAvailableQuantity();
                log.info("Available quantity is {}", quantity);
            } else {
                log.error("Unable to get inventory level for product code {}, status code {}", code, itemResponseEntity.getStatusCode());
            }
            log.info("After CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
        } else {
            log.error("Didnt find any product in catalog service with code {}", code);
        }

        return productOptional;
    }
}