package com.digital.telco.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class UserEntityDto {

	private String phoneNo;

	private String initialStatus;
	
	private String newStatus;
	
	private String emailId;


}
