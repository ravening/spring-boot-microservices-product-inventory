package com.rakeshv.inventoryservice.repository;

import java.util.Optional;

import com.rakeshv.inventoryservice.models.InventoryItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * InventoryRepository
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductCode(String productCode);
    
}