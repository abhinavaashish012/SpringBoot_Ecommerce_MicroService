package com.ecommerce.inventoryservice.service;


import com.ecommerce.inventoryservice.dto.InventoryResponse;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true )
    public List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException {
        log.info("Wait Started.........");
        Thread.sleep(10000);
        log.info("Wait Ended.........");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
    }
}
