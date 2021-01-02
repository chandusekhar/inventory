package com.digital.telco.inventory.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sim")
@Data
public class SIM extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "imsi", nullable = false, unique = true)
    private String imsi;

    @Column(name = "iccid", nullable = false, unique = true)
    private String iccid;

    @Column(name = "puk_1", nullable = false)
    private String puk1;

    @Column(name = "puk_2", nullable = false)
    private String puk2;

    @Column(name = "security_code", nullable = false)
    private String securityCode;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(name = "available_till")
    private LocalDateTime availableTill;

    @Column(name = "sim_state")
    private String state;

    @Column(name = "sim_type")
    private String simType;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "vendor_ref")
    private String vendorRef;

}
