package com.digital.telco.inventory.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.repository.MsisdnCategeoryRepository;
import com.digital.telco.inventory.repository.MsisdnStatusRepository;
import com.digital.telco.inventory.service.impl.MsisdnCommonServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class MsisdnCommonServiceImplTest {

	@InjectMocks
	private MsisdnCommonServiceImpl msisdnCommonService;

	@Mock
	private MsisdnStatusRepository msisdnStatusRepo;

	@Mock
	private MsisdnCategeoryRepository msisdnCategeoryRepo;

	@Test
	public void testGetAllStatus() {
		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(1L);
		msisdnStatusList.add(msisdnStatus);
		when(msisdnStatusRepo.findAll()).thenReturn(msisdnStatusList);
		assertTrue(msisdnCommonService.getAllStatus() instanceof List);
	}

	@Test
	public void testGetAllCategories() {
		List<MSISDNCategory> msisdnCategoryList = new ArrayList<>();
		MSISDNCategory msisdnCategory = new MSISDNCategory();
		msisdnCategory.setId(1L);
		msisdnCategoryList.add(msisdnCategory);
		when(msisdnCategeoryRepo.findAll()).thenReturn(msisdnCategoryList);
		assertTrue(msisdnCommonService.getAllCategeories() instanceof List);
	}

}
