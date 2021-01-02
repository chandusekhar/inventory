package com.digital.telco.inventory.dto;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class MsisdnReviveDto {
    
	@Id
	@Column(name="_RESPONSE")
	String responseMsg;
	@Column(name="msisdn_id")
	Long msisdnId;
	@Column(name="msisdn_number")
	String msisdnNumber;
	@Column(name="msisdn_category_id")
	Long msisdnCategoryId;
	@Column(name="msisdn_category_name")
	String msisdnCatName;
	@Column(name="price")
	String price;
	@Column(name="expiry_time")
	LocalDateTime expiryTime;
	
}
