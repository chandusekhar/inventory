package com.digital.telco.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "msisdn")
@Data
public class MSISDN extends AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msisdn_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "msisdn_number", nullable = false)
	private String msisdnNumber;
	
	@Column(name = "msisdn_service_provider_id", nullable = false)
	private Long serviceProvider;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="msisdn_category_id")
	private MSISDNCategory category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="msisdn_status")
	private MSISDNStatus status;
	
	
}
