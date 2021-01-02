package com.digital.telco.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.digital.telco.inventory.entity.MSISDN;

public interface MsisdnRepository extends JpaRepository<MSISDN, Long> {

	@Query(value = "select * from msisdn m WHERE m.msisdn_category_id in(:categoryId) and"
			+ " m.msisdn_status=:status and m.msisdn_id not in "
			+ "(select mu.msisdn_id from msisdn_user_mapping mu where mu.app_registration_id "
			+ "= :registrationId) limit :count", nativeQuery = true)
	List<MSISDN> getMsisdnNumber(@Param("categoryId") List<Long> categoryId, @Param("registrationId") String registrationId,@Param("count") Integer count,@Param("status") Long status);


	@Modifying
	@Query(value = "update MSISDN t set t.status.id= :status WHERE t.msisdnNumber in(:msisdnNumberList)")
	void accquireLockOnGeneratedNumber(@Param("msisdnNumberList") List<String> msisdnNumberList,
			@Param("status") Long status);

	@Modifying
	@Query(value = "update MSISDN t set t.status.id= :status WHERE t.id in(:msisdnUserMappingIdList)")
	void accquireUnLockOnGeneratedNumber(@Param("msisdnUserMappingIdList") List<Long> msisdnUserMappingIdList,
			@Param("status") Long status);
	

	@Query(value = "select * from msisdn m WHERE  m.msisdn_id in "
			+ "(select mu.msisdn_id from msisdn_user_mapping mu where mu.app_registration_id= :registrationId ) ", nativeQuery = true)
	List<MSISDN> checkAvailbility(@Param("registrationId") String registrationId);

	@Query(value = "select * from msisdn m where m.msisdn_status=:status and m.msisdn_number like %:pattern% "
			+ " and m.msisdn_id not in "
			+ "(select mu.msisdn_id from msisdn_user_mapping mu where mu.app_registration_id "
			+ "= :registrationId) limit :count", nativeQuery = true)
	List<MSISDN> generateMsisdnNumber(@Param("pattern")String pattern, @Param("registrationId")String registrationId,@Param("count") Integer count,@Param("status") Long status);

	@Modifying
	@Query(value = "update MSISDN t set t.status.id= :statusAvailable WHERE t.msisdnNumber in(:msisdnNumberList)")
	void releaseNumbers(List<String> msisdnList, String registrationId, Long statusAvailable);

	//@Query(value = "select msisdn_id from msisdn m WHERE  m.msisdn_number= :msisdnNumber ", nativeQuery = true)
	Optional<MSISDN> findByMsisdnNumber(@Param("msisdnNumber") String msisdnNumber);
	
	
	@Modifying
	@Query(value = "update MSISDN t set t.status.id= :newStatusId WHERE t.msisdnNumber = :msisdnNumber and t.status.id=:oldStatusId")
	void changeStatusForMsisdnNumber(@Param("oldStatusId") Long oldStatusId,
			@Param("newStatusId") Long newStatusId,@Param("msisdnNumber") String msisdnNumber);
	
	
	@Query(value = "select t from MSISDN t where t.msisdnNumber=:msisdn")
	Optional<MSISDN> getCategoryForMsisdn(@Param("msisdn") String msisdn);
	
	
}
