package com.digital.telco.inventory.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsisdnNumberResponse {

String reviveMsisdnMsg;	
String msisdnNumber;
Long category;
String price;
String registrationId;
String searchCriteria;
Integer stage;
Long msisdnId;
LocalDateTime expiryTime;
String categoryName;		
}
