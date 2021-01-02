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
public class MsisdnConfirmDto {

	@Id
	@Column(name = "expiry_time")
	private LocalDateTime expiryTime;

	@Column(name = "OUT_ERROR_CODE")
	private String errorCode;
	
	@Column(name = "OUT_ERROR_TEXT")
	private String errorText;

	@Column(name = "msisdn_user_mapping_id")
	private Long userMappingId;
	
	
}
