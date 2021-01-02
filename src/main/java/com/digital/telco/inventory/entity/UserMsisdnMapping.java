package com.digital.telco.inventory.entity;

import java.time.LocalDateTime;
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
@Table(name = "msisdn_user_mapping")
@Data
public class UserMsisdnMapping extends AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msisdn_user_mapping_id")
	private Long msisdnUserMappingId;

	@Column(name = "app_registration_id")
	private String registrationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "msisdn_id")
	private MSISDN msisdn;

	@Column(name = "expiry_time")
	private LocalDateTime expiryTime;

	@Column(name = "source")
	private String source;

	@Column(name = "is_display")
	private boolean isDisplay;

}
