package com.digital.telco.inventory.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequiredBalanceArray {
	private String mtx_sys_ver;

	private String classId;

	private String mtx_ext_ver;

	private String resourceId;

	private String mtx_container_name;

	private String isPrivate;

	private String templateId;
}
