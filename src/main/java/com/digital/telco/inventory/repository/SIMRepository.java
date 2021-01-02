package com.digital.telco.inventory.repository;


import com.digital.telco.inventory.entity.SIM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SIMRepository extends JpaRepository<SIM, Long> {

    Optional<SIM> findByIccid(String iccid);
}
