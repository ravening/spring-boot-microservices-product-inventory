package com.rakeshv.catalogservice.service;

import java.util.Optional;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.rakeshv.catalogservice.models.ProductInventoryResponse;
import com.rakeshv.catalogservice.utils.MyThreadLocalsHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * InventoryServiceClient
 */
@Service
@Slf4j
public class InventoryServiceClient {
    private final RestTemplate restTemplate;
    private static final String INVENTORY_API_PATH = "http://inventory-service/api/";

    @Autowired
    public InventoryServiceClient(RestTemplate template) {
        this.restTemplate = template;
    }

    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode"//,
        // commandProperties = {
            // @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            // @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
        //  }
    )
    public Optional<ProductInventoryResponse> getProductInventoryByCode(String productCode) {
        log.info("CorreliationId: {}", MyThreadLocalsHolder.getCorrelationId());
        ResponseEntity<ProductInventoryResponse> itemtResponseEntity =
                restTemplate.getForEntity(INVENTORY_API_PATH + "inventory/{code}",
                                            ProductInventoryResponse.class,
                                            productCode);
        
        if (itemtResponseEntity.getStatusCode() == HttpStatus.OK) {
            Integer quantity = itemtResponseEntity.getBody().getAvailableQuantity();
            log.info("Available quantity is {}", quantity);
            return Optional.ofNullable(itemtResponseEntity.getBody());
        } else {
            log.error("Unable to get inventory for product code {}, status code : {}",
                        productCode, itemtResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    Optional<ProductInventoryResponse> getDefaultProductInventoryByCode(String productCode) {
        log.info("Returning default productInventoryByCode for product code {}", productCode);
        log.info("CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
        ProductInventoryResponse response = ProductInventoryResponse
                                                .builder()
                                                .availableQuantity(50)
                                                .productCode(productCode).build();

        return Optional.ofNullable(response);
    }
    
}