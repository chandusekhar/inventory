package com.digital.telco.inventory.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class InventorySchedulerDTO implements Serializable {
	private String name;
	private String group;
	Map<String,Object> jobData;
	public InventorySchedulerDTO(String name, String group, Map<String, Object> jobData) {
		super();
		this.name = name;
		this.group = group;
		this.jobData = jobData;
	}
	public InventorySchedulerDTO() {
		super();
	}
}
