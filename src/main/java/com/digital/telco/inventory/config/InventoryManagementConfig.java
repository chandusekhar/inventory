package com.digital.telco.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class InventoryManagementConfig {
	ApiInfo apiInfo = null;
	 @Bean
	    public Docket productApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()                 .apis(RequestHandlerSelectors.basePackage("com.digital.telco.inventory.controller"))
	                .paths(PathSelectors.any())
	                .build();
}
	 
	    
	 
	  @Bean
	    public MethodValidationPostProcessor methodValidationPostProcessor() {
	        return new MethodValidationPostProcessor();
	    }
}