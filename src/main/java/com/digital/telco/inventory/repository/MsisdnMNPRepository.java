package com.digital.telco.inventory.repository;

import com.digital.telco.inventory.entity.MSISDNMnp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MsisdnMNPRepository extends JpaRepository<MSISDNMnp, Long> {


    Optional<MSISDNMnp> findByMsisdnNumber(String msisdnNumber);
}
