package com.digital.telco.inventory.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.telco.inventory.entity.MSISDNCategory;

public interface MsisdnCategeoryRepository extends JpaRepository<MSISDNCategory, Long> {

	@Modifying
	@Transactional
	@Query(value = "update MSISDNCategory t set t.price= :price WHERE t.categoryName=:categoryName")
	void updatePriceForVanityNumbers(@Param("categoryName") String categoryName,
			@Param("price") String price);		
	
}
