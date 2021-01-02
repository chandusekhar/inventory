package com.digital.telco.inventory.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceImpactUpdateList {

	private String mtx_sys_ver;

    private String amount;

    private String mtx_ext_ver;

    private String mtx_container_name;

    private String nonProratedAmount;

    private String updateType;
}
