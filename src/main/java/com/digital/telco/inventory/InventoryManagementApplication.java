package com.digital.telco.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableFeignClients("com")
@SpringBootApplication
@EnableJpaAuditing
@ComponentScan("com")
@EnableCaching
@EnableScheduling
public class InventoryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}

}
