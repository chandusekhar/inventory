package com.digital.telco.inventory.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "msisdn_category")
@Data
public class MSISDNCategory extends AuditableEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msisdn_category_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "msisdn_category_name", nullable = false)
	private String categoryName;
	
	@Column(name = "pattern")
	private String pattern;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "is_active", nullable = false)
	private boolean isActive;
	
	@Column(name = "price")
	private String price;
}
