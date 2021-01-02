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
public class BalanceImpactList {
	
	private List<BalanceImpactOfferList> balanceImpactOfferList;

    private String currentBalanceAmount;

    private String balanceEndTime;

    private String mtx_ext_ver;

    private String isMainBalance;

    private String balanceTemplateName;

    private String flags;

    private String mtx_container_name;

    private String quantityUnit;

    private String balanceClassId;

    private String balanceStartTime;

    private String balanceClassName;

    private String impactAmount;

    private String balanceOwnerId;

    private String debtBalanceType;

    private String mtx_sys_ver;

    private String isCycleHolding;

    private String isCost;

    private String isActualCurrency;

    private String balanceResourceId;

    private String balanceTemplateId;
    
    private String balanceIntervalId;
       
	
}
