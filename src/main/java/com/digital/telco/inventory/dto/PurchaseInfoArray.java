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
public class PurchaseInfoArray {
	private String catalogItemId;

    private String mtx_sys_ver;

    private String offerType;

    private String mtx_ext_ver;

    private String resourceId;

    private List<BalanceImpactGroupList> balanceImpactGroupList;

    private String catalogItemExternalId;

    private String mtx_container_name;

    private String externalId;

    private List<RequiredBalanceArray> requiredBalanceArray;

    private String productOfferId;

    private String productOfferVersion;
}
