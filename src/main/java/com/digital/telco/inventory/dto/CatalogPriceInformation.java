package com.digital.telco.inventory.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogPriceInformation {

	   private String mtx_sys_ver;

	    private List<PurchaseInfoArray> purchaseInfoArray;

	    private String result;

	    private String resultText;

	    private String _resultText;

	    private String mtx_ext_ver;

	    private String routeId;

	    private String mtx_container_name;

	    private String _resultCode;

	    private String subscriptionObjectId;

	    private String _resultType;

	    private String objectId;

	
}
