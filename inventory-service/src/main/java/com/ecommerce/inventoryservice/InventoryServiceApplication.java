package com.ecommerce.inventoryservice;

import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory i1 = new Inventory();
			i1.setSkuCode("iphone_13");
			i1.setQuantity(100);

			Inventory i2 = new Inventory();
			i2.setSkuCode("iphone_13_red");
			i2.setQuantity(0);

			inventoryRepository.save(i1);
			inventoryRepository.save(i2);

		};
	}

}
