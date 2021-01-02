package com.digital.telco.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.telco.inventory.dto.MSISDNDto;

public interface MsisdnDtoRepository  extends JpaRepository<MSISDNDto, Long>{

	@Query(value = "CALL get_suggested_msisdn(:searchCritera,:statusAvailable,:categeory,:count);", nativeQuery = true)
	List<MSISDNDto> getSuggestedMsisdn(@Param("searchCritera") String searchCriteria , @Param("statusAvailable") Long statusAvailable,
			@Param("categeory") String categeory,@Param("count") int count);
	

	
	
	@Modifying
	@Query(value ="CALL discard_mapped_msisdn(:registrationId,:statusAvailable,:statusReserved);", nativeQuery = true)
	void releaseReservedMSISDN(@Param("registrationId") String registrationId,@Param("statusAvailable") Long statusAvailable,@Param("statusReserved") 
	boolean statusReserved);
}
