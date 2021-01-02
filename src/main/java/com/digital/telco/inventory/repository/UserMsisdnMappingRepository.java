package com.digital.telco.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.telco.inventory.entity.UserMsisdnMapping;

public interface UserMsisdnMappingRepository extends JpaRepository<UserMsisdnMapping, Long> {

	@Modifying
	//@Query(value = "update UserMsisdnMapping t set t.isDisplay= :isDisplay WHERE t.msisdn.msisdnNumber = :msisdnNumber and t.isDisplay = false")
	@Query(value = "update msisdn_user_mapping set is_display= :isDisplay where msisdn_id = (select msisdn_id from msisdn where msisdn_number= :msisdnNumber) and is_display=false",nativeQuery = true)
	void changeStatusForMsisdnNumber(@Param("isDisplay") boolean isDisplay,@Param("msisdnNumber") String msisdnNumber);
	
	
}
