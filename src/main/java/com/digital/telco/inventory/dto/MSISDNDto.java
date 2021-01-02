package com.digital.telco.inventory.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class MSISDNDto {

	@Id
	@Column(name = "msisdn_id")
	Long msisdnId;
	
	@Column(name = "msisdn_number")
	String msisdnNumber;

	
	@Column(name = "msisdn_category_id")
	Long msisdnCategoryId;
	
	@Column(name = "stage")
	int stage;
	
	@Column(name = "search_criteria")
	String searchCriteria;
	
	@Column(name = "msisdn_category_name")
	String msisdnCategoryName;
	
	@Column(name = "price")
	String msisdnPrice;
	
	
}
