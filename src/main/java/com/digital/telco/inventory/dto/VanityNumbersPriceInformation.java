package com.digital.telco.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VanityNumbersPriceInformation {

String categoryName;
String categoryPrice;
	
}
