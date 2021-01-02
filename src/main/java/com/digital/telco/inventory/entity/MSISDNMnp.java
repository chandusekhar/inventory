package com.digital.telco.inventory.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "msisdn_mnp")
@Data
public class MSISDNMnp extends AuditableEntity {

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
