package com.rakeshv.inventoryservice.controllers;

import java.util.List;
import java.util.Optional;

import com.rakeshv.inventoryservice.models.InventoryItem;
import com.rakeshv.inventoryservice.repository.InventoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * InverntoryController
 */
@RestController
@Slf4j
@RequestMapping("/api/inventory")
public class InverntoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/{code}")
    public ResponseEntity<InventoryItem> findInventoryByProductCode(@PathVariable("code") String code) {
        log.info("Finding the invenotry for product code : {}", code);
        Optional<InventoryItem> iOptional = inventoryRepository.findByProductCode(code);
        if (iOptional.isPresent()) {
            return new ResponseEntity<>(iOptional.get(), HttpStatus.OK);
        }

        log.info("didnt find any product in inventory with code {}", code);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getInventory() {
        log.info("Getting all inventory items");
        return new ResponseEntity<>(inventoryRepository.findAll(), HttpStatus.OK);
    }
}