package com.digital.telco.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.telco.inventory.dto.MsisdnConfirmDto;

public interface MsisdnConfirmRepository extends JpaRepository<MsisdnConfirmDto, Long> {

	@Query(value = "CALL update_selected_msisdn(:registrationId,:msisdnId,:statusAvailable,:statusPicked,:expiryDate,:source);", nativeQuery = true)
	MsisdnConfirmDto confirmMsisdn(@Param("registrationId") String registrationId, @Param("msisdnId") Long msisdnId,
			@Param("statusAvailable") Long statusAvailable, @Param("statusPicked") Long statusPicked,
			@Param("expiryDate") int expiryDate,@Param("source") String source);

	@Modifying
	@Query(value = "CALL update_discarded_and_selected_msisdn(:registrationId,:msisdnId,:statusAvailable,:statusPicked,:expiryDate,:source);", nativeQuery = true)
	void releaseMsisdn(@Param("registrationId") String registrationId, @Param("msisdnId") Long msisdnId,
			@Param("statusAvailable") Long statusAvailable, @Param("statusPicked") Long statusPicked,
			@Param("expiryDate") int expiryDate,@Param("source") String source);


	
} 
