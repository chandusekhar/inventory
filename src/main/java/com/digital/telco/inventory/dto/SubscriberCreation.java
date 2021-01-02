package com.digital.telco.inventory.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriberCreation {

	private List<SubscriptionOfferArray> subscriptionOfferArray;

	private Subscription subscription;

	@JsonProperty(value = "ExecuteMode")
	private String ExecuteMode;

	private List<RoleArray> roleArray;

	private User user;
}
