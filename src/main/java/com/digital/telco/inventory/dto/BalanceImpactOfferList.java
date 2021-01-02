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
public class BalanceImpactOfferList {
	
	private String catalogItemId;

    private String mtx_sys_ver;

    private String offerVersion;

    private List<BalanceImpactUpdateList> balanceImpactUpdateList;

    private String mtx_ext_ver;

    private String offerResourceId;

    private String offerExternalId;

    private String catalogItemExternalId;

    private String flags;

    private String mtx_container_name;

    private String offerId;
}
