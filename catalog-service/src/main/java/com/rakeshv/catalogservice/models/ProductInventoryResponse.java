package com.rakeshv.catalogservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductInventoryResponse
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryResponse {
    private String productCode;
    private Integer availableQuantity = 0;    
}