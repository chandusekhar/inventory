package com.digital.telco.inventory.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserMsisdnDetails implements Serializable {

String msisdnNumber;
String registrationId;
Long categeoryId;
String categeory;
String msisdnStatus;
	
	
}
