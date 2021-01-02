package com.digital.telco.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.telco.inventory.dto.MsisdnReviveDto;

public interface MsisdnReviveRepository extends JpaRepository<MsisdnReviveDto, Long> {

	@Query(value = "CALL revive_last_selected_msisdn(:registrationId,:statusAvailable,:statusPicked,:expiryDate);", nativeQuery = true)
	MsisdnReviveDto reviveMsisdn(@Param("registrationId") String registrationId,
			@Param("statusAvailable") Long statusAvailable, @Param("statusPicked") Long statusPicked,
			@Param("expiryDate") int expiryDate);

}


