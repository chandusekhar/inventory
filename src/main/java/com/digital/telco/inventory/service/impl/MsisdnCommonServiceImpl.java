package com.digital.telco.inventory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.repository.MsisdnCategeoryRepository;
import com.digital.telco.inventory.repository.MsisdnStatusRepository;
import com.digital.telco.inventory.service.MsisdnCommonService;

@Service
public class MsisdnCommonServiceImpl implements MsisdnCommonService {

	
	@Autowired
	MsisdnStatusRepository msisdnStatusRepository;

	@Autowired
	MsisdnCategeoryRepository msisdnCategeoryRepository;

	@Cacheable(value="status")
	public List<MSISDNStatus> getAllStatus(){
		
		return msisdnStatusRepository.findAll();
		
		
	}
	@Cacheable(value="categories")
	public List<MSISDNCategory> getAllCategeories(){
		
		return msisdnCategeoryRepository.findAll();
		
		
	}
}
