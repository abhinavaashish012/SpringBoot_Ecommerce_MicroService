package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{skuCode}")
    public boolean isInStock(@PathVariable String skuCode){
            return inventoryService.isInStock(skuCode);
    }
}
