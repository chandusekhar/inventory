package com.digital.telco.inventory.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.digital.telco.inventory.entity.MSISDNMnp;
import com.digital.telco.inventory.repository.*;
import com.digital.telco.workflow.common.dto.MsisdnMNPDTO;
import com.digital.telco.workflow.common.enums.MsisdnStatus;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.digital.telco.inventory.config.InventorySchedulerMqProducer;
import com.digital.telco.inventory.config.UsedMsisdnRabbitMqProducer;
import com.digital.telco.inventory.constant.ApplicationConstant;
import com.digital.telco.inventory.dto.CatalogInformation;
import com.digital.telco.inventory.dto.MSISDNDto;
import com.digital.telco.inventory.dto.MetadataList;
import com.digital.telco.inventory.dto.MsisdnConfirmDto;
import com.digital.telco.inventory.dto.MsisdnNumberResponse;
import com.digital.telco.inventory.dto.MsisdnReviveDto;
import com.digital.telco.inventory.dto.Results;
import com.digital.telco.inventory.dto.UserEntityDto;
import com.digital.telco.inventory.dto.VanityNumbersPriceInformation;
import com.digital.telco.inventory.entity.MSISDN;
import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.service.client.UserDetailsClient;
import com.digital.telco.inventory.service.impl.MsisdnServiceImpl;
import com.digital.telco.utility.exception.BaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test cases for {@link MsisdnServiceImpl} covering all possible
 * scenarios.
 */
@RunWith(MockitoJUnitRunner.class)
public class MsisdnServiceImplTest {

	@InjectMocks
	private MsisdnServiceImpl msisdnService;

	@Mock
	private MsisdnRepository msisdnRepository;

	@Mock
	private MsisdnDtoRepository msisdnDtoRepository;

	@Mock
	private MsisdnCategeoryRepository msisdnCategeoryRepository;

	@Mock
	private MsisdnConfirmRepository msisdnConfirmRepository;

	@Mock
	private UsedMsisdnRabbitMqProducer usedMsisdnRabbitMqProducer;

	@Mock
	private MessageSource messageSource;

	@Mock
	private MsisdnCommonService msisdnCommonService;

	@Mock
	private InventorySchedulerMqProducer schedulerMqProducer;

	@Mock
	private MsisdnReviveRepository msisdnReviveRepository;

	@Mock
	private UserMsisdnMappingRepository userMsisdnMappingRepository;

	@Mock
	private UserDetailsClient userDetailsClient;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private MsisdnMNPRepository msisdnMNPRepository;

	@Test
	public void testGenerateMsisdnNumber() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		String searchCriteria = "-";
		List<MSISDNDto> msisdnDtoList = msisdnDtolist();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnDtoRepository.getSuggestedMsisdn(searchCriteria, 1L, "1,2,3", 1)).thenReturn(msisdnDtoList);
		assertTrue(msisdnService.generateMsisdnNumber("1,2,3", 1) instanceof List);
		verify(msisdnCommonService, times(1)).getAllStatus();
		verify(msisdnDtoRepository, times(1)).getSuggestedMsisdn(searchCriteria, 1L, "1,2,3", 1);

	}

	@Test(expected = BaseException.class)
	public void testGenerateMsisdnNumber_EmptyList() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.generateMsisdnNumber("4", 1);
		verify(msisdnCommonService, times(1)).getAllStatus();

	}

	@Test(expected = BaseException.class)
	public void testGenerateMsisdnNumber_IncorrectStatus() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(1L);
		msisdnStatus.setStatusName(ApplicationConstant.STATUS_RESERVED_NAME);
		msisdnStatusList.add(msisdnStatus);
		msisdnService.generateMsisdnNumber("4", 1);

	}

	@Test
	public void testChooseMsisdnNumber() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		String searchCriteria = "555";
		List<MSISDNDto> msisdnDtoList = msisdnDtolist();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnDtoRepository.getSuggestedMsisdn(searchCriteria, 1L, "1,2,3,4", 1)).thenReturn(msisdnDtoList);
		assertTrue(msisdnService.generateOwnMsisdnNumber("555", "1,2,3,4", 1) instanceof List);
		verify(msisdnDtoRepository, times(1)).getSuggestedMsisdn(searchCriteria, 1L, "1,2,3,4", 1);

	}

	@Test(expected = BaseException.class)
	public void testChooseMsisdnNumber_EmptyList() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.generateOwnMsisdnNumber("555", "1,2,3,4", 1);

	}

	@Test(expected = BaseException.class)
	public void testChooseMsisdnNumberInvalidStatus() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(1L);
		msisdnStatus.setStatusName(ApplicationConstant.STATUS_RESERVED_NAME);
		msisdnStatusList.add(msisdnStatus);
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.generateOwnMsisdnNumber("555", "1,2,3,4", 1);

	}

	@Test
	public void testConfirmMsisdnNumber() throws BaseException {
		ReflectionTestUtils.setField(msisdnService, "reservedTime", "60");
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		String registrationId = "12345";
		MsisdnConfirmDto msisdnConfirmDto = new MsisdnConfirmDto();
		msisdnConfirmDto.setExpiryTime(LocalDateTime.now());
		msisdnConfirmDto.setUserMappingId(1L);
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnConfirmRepository.confirmMsisdn(registrationId, 345L, 1L, 6L, 60, "app"))
				.thenReturn(msisdnConfirmDto);
		assertTrue(msisdnService.confirmMsisdnNumber(registrationId, 345L, "9871234567", 2L, "GOLD",
				"app") instanceof MsisdnNumberResponse);
		verify(msisdnConfirmRepository, times(1)).confirmMsisdn(registrationId, 345L, 1L, 6L, 60, "app");

	}

	@Test(expected = BaseException.class)
	public void testConfirmMsisdnNumberInvalidStatus() throws BaseException {
		ReflectionTestUtils.setField(msisdnService, "reservedTime", "60");
		msisdnService.confirmMsisdnNumber("1", 345L, "9871234567", 2L, "GOLD", "app");
	}

	@Test(expected = BaseException.class)
	public void testConfirmMsisdnNumberNumber_AlreadyMapped() throws BaseException {
		ReflectionTestUtils.setField(msisdnService, "reservedTime", "60");
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		String registrationId = "12345";
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnConfirmRepository.confirmMsisdn(registrationId, 345L, 1L, 6L, 60, "app")).thenReturn(null);
		msisdnService.confirmMsisdnNumber(registrationId, 345L, "9871234567", 2L, "GOLD", "app");
	}

	@Test
	public void testReviveMsisdnNumber() throws BaseException {
		ReflectionTestUtils.setField(msisdnService, "reservedTime", "60");
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		MsisdnReviveDto msisdnReviveDto = new MsisdnReviveDto();
		msisdnReviveDto.setExpiryTime(LocalDateTime.now());
		msisdnReviveDto.setMsisdnCategoryId(2L);
		msisdnReviveDto.setMsisdnNumber("999909567");
		msisdnReviveDto.setResponseMsg(ApplicationConstant.REACQUIRED);
		String registrationId = "12345";
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnReviveRepository.reviveMsisdn(registrationId, 1L, 6L, 60)).thenReturn(msisdnReviveDto);
		assertTrue(msisdnService.reviveMsisdnInfo(registrationId) instanceof MsisdnNumberResponse);
		verify(msisdnReviveRepository, times(1)).reviveMsisdn(registrationId, 1L, 6L, 60);
		verify(msisdnCommonService, times(1)).getAllStatus();

	}

	@Test
	public void testCancelMsisdnNumber() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		doNothing().when(msisdnDtoRepository).releaseReservedMSISDN("345", 1L, true);
		msisdnService.cancelMsisdnInfo("345");
		verify(msisdnDtoRepository, times(1)).releaseReservedMSISDN("345", 1L, true);
		verify(msisdnCommonService, times(1)).getAllStatus();

	}

	@Test
	public void testChangeMsisdnStatus() throws BaseException {
		UserEntityDto userEntityDto = new UserEntityDto();
		userEntityDto.setPhoneNo("9560377500");
		userEntityDto.setInitialStatus("PICKED1");
		userEntityDto.setNewStatus("PICKED2");
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.changeMsisdnStatus(userEntityDto);
		verify(msisdnCommonService, times(1)).getAllStatus();

	}

	@Test(expected = BaseException.class)
	public void testChangeMsisdnStatus_Exception() throws BaseException {
		UserEntityDto userEntityDto = new UserEntityDto();
		userEntityDto.setPhoneNo("9560377500");
		userEntityDto.setInitialStatus("PICKED1");
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		Mockito.doNothing().when(userMsisdnMappingRepository).changeStatusForMsisdnNumber(true, "9560377500");
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.changeMsisdnStatus(userEntityDto);
		verify(msisdnCommonService, times(1)).getAllStatus();
	}

	@Test
	public void testReleaseMsisdn() throws BaseException {
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		doNothing().when(msisdnRepository).changeStatusForMsisdnNumber(5L, 1L, "9560377500");
		msisdnService.releaseNumber("9560377500", "xyz@abc.com");
		verify(msisdnRepository, times(1)).changeStatusForMsisdnNumber(5L, 1L, "9560377500");
		verify(msisdnCommonService, times(1)).getAllStatus();
	}

	@Test(expected = BaseException.class)
	public void testReleaseMsisdn_Exception() throws BaseException {
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(5L);
		msisdnStatus.setStatusName(ApplicationConstant.STATUS_PICKED2);
		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		msisdnStatusList.add(msisdnStatus);
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.releaseNumber("9560377500", "xyz@abc.com");

	}

	@Test
	public void testReleaseMsisdnNumbers() throws BaseException {
		List<String> msisdnList = msisdnList();
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(1L);
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		MSISDN msisdn = new MSISDN();
		msisdn.setId(1L);
		msisdn.setStatus(msisdnStatus);
		msisdn.setMsisdnNumber("9560377500");
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		when(msisdnRepository.findById(1L)).thenReturn(Optional.of(msisdn));
		doNothing().when(msisdnDtoRepository).releaseReservedMSISDN(Mockito.anyString(), Mockito.anyLong(),
				Mockito.anyBoolean());
		msisdnService.releaseNumbers(msisdnList, "xyz", 1L, 1L);
		verify(msisdnRepository, times(1)).findById(1L);

	}

	@Test(expected = BaseException.class)
	public void testReleaseMsisdnNUmbers_Exception() throws BaseException {
		List<String> msisdnList = msisdnList();
		long currentStatus = ApplicationConstant.STATUS_AVAILABLE;
		List<MSISDNStatus> msisdnStatusList = msisdnStatusList();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		msisdnService.releaseNumbers(msisdnList, "xyz", currentStatus, null);

	}

	@Test
	public void testGetPriceDetails() throws BaseException {
		MetadataList list = new MetadataList();
		List<VanityNumbersPriceInformation> priceList = new ArrayList<VanityNumbersPriceInformation>();
		list.setValue("Platinum,5000;Gold,4000;Silver,3000;Bronze,2000;Normal,0;");
		assertTrue(msisdnService.getPriceDetails(list, priceList) instanceof List);

	}

	@Test
	public void testGetVanityNumberPriceDetails() throws BaseException, JsonMappingException, JsonProcessingException {
		CatalogInformation catalogInformation = new CatalogInformation();
		Results results = new Results();
		MetadataList metadataList = new MetadataList();
		metadataList.setName(ApplicationConstant.VanityNumberPrice);
		metadataList.setValue("categoryName,data,voice,sms,price,suggestion,display;");
		results.setMetadataList(Lists.newArrayList(metadataList));
		catalogInformation.setResults(Lists.newArrayList(results));
		VanityNumbersPriceInformation information = new VanityNumbersPriceInformation();
		information.setCategoryName("NewNumber");
		information.setCategoryPrice("20000");

		ResponseEntity<String> res = new ResponseEntity<String>("SuccessBody", HttpStatus.OK);
		ReflectionTestUtils.setField(msisdnService, "matrixxCatalogUrl", "http://mockTestUrl");
		when(restTemplate.exchange(ArgumentMatchers.anyString(), any(HttpMethod.class),
				any(HttpEntity.class), ArgumentMatchers.<Class<String>>any())).thenReturn(res);

		when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.<Class<CatalogInformation>>any()))
				.thenReturn(catalogInformation);
		List<VanityNumbersPriceInformation> vanityNumList = msisdnService.getVanityNumberPrice();
		assertTrue(vanityNumList instanceof List);
		assertEquals("categoryName", vanityNumList.get(0).getCategoryName());
	}

	@Test(expected = BaseException.class)
	public void testGetVanityNumberPriceDetails_Exception()
			throws BaseException, JsonMappingException, JsonProcessingException {
		CatalogInformation catalogInformation = null;
		VanityNumbersPriceInformation information = new VanityNumbersPriceInformation();
		information.setCategoryName("NewNumber");
		information.setCategoryPrice("20000");

		ResponseEntity<String> res = new ResponseEntity<String>("SuccessBody", HttpStatus.OK);
		ReflectionTestUtils.setField(msisdnService, "matrixxCatalogUrl", "http://mockTestUrl");
		when(restTemplate.exchange(ArgumentMatchers.anyString(), any(HttpMethod.class),
				any(HttpEntity.class), ArgumentMatchers.<Class<String>>any())).thenReturn(res);
		when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.<Class<CatalogInformation>>any()))
				.thenReturn(catalogInformation);
		msisdnService.getVanityNumberPrice();

	}

	@Test
	public void testGetMsisdnStatus() throws BaseException {
		String msisdn = "1234";
		MSISDN msisdnInformation = new MSISDN();
		msisdnInformation.setId(1L);
		MSISDNCategory category = new MSISDNCategory();
		category.setCategoryName("test");
		msisdnInformation.setCategory(category);
		Mockito.when(msisdnRepository.getCategoryForMsisdn(Mockito.anyString()))
				.thenReturn(Optional.of(msisdnInformation));
		assertEquals("test", msisdnService.getMsisdnStatus(msisdn));
		verify(msisdnRepository, times(1)).getCategoryForMsisdn("1234");

	}

	@Test
	public void testUpdateMSISDNStatus() throws BaseException {
		UserEntityDto userEntityDto = getUserEntityDto();
		when(msisdnCommonService.getAllStatus()).thenReturn(getMsisdnStatuses("ORDERED", "PURCHASED"));
		msisdnService.updateMSISDNStatus(userEntityDto);
		verify(msisdnCommonService, times(1)).getAllStatus();

	}


	@Test(expected = BaseException.class)
	public void testAddMSISDNInMNPInventoryException() throws BaseException {

		MsisdnMNPDTO requestDTO = new MsisdnMNPDTO();
		requestDTO.setMsisdnStatus(MsisdnStatus.PURCHASED.name());
		requestDTO.setMsisdnNumber("8912309123");
		when(msisdnMNPRepository.findByMsisdnNumber(anyString())).thenReturn(Optional.of(new MSISDNMnp()));
		msisdnService.addMNPInInventory(requestDTO);
	}

	@Test
	public void testAddMSISDNInMNPInventory() throws BaseException {

		MsisdnMNPDTO requestDTO = new MsisdnMNPDTO();
		requestDTO.setMsisdnStatus(MsisdnStatus.PURCHASED.name());
		requestDTO.setMsisdnNumber("8912309123");
		when(msisdnMNPRepository.findByMsisdnNumber(anyString())).thenReturn(Optional.empty());
		when(msisdnCommonService.getAllStatus()).thenReturn(getMsisdnStatuses( "PURCHASED","ORDERED"));
		msisdnService.addMNPInInventory(requestDTO);
		verify(msisdnMNPRepository,times(1)).save(any(MSISDNMnp.class));
	}

	private List<MSISDNStatus> getMsisdnStatuses(String initialStatus, String newStatus) {
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(5L);
		msisdnStatus.setStatusName(initialStatus);
		MSISDNStatus msisdnStatus1 = new MSISDNStatus();
		msisdnStatus1.setId(6L);
		msisdnStatus1.setStatusName(newStatus);

		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		msisdnStatusList.add(msisdnStatus);
		msisdnStatusList.add(msisdnStatus1);
		return msisdnStatusList;
	}

	public static UserEntityDto getUserEntityDto() {
		UserEntityDto userEntityDto = new UserEntityDto();
		userEntityDto.setEmailId("test@nagarro.com");
		userEntityDto.setInitialStatus("ORDERED");
		userEntityDto.setNewStatus("PURCHASED");
		userEntityDto.setPhoneNo("987XXXXX909");
		return userEntityDto;
	}

	private List<MSISDNStatus> msisdnStatusList() {
		List<MSISDNStatus> msisdnStatusList = new ArrayList<>();
		MSISDNStatus msisdnStatus = new MSISDNStatus();
		msisdnStatus.setId(1L);
		msisdnStatus.setStatusName(ApplicationConstant.STATUS_AVAILABLE_NAME);

		MSISDNStatus msisdnStatus1 = new MSISDNStatus();
		msisdnStatus1.setId(6L);
		msisdnStatus1.setStatusName(ApplicationConstant.STATUS_RESERVED_NAME);
		MSISDNStatus msisdnStatus2 = new MSISDNStatus();
		msisdnStatus2.setId(5L);
		msisdnStatus2.setStatusName(ApplicationConstant.STATUS_PICKED2);
		MSISDNStatus msisdnStatus3 = new MSISDNStatus();
		msisdnStatus3.setId(1L);
		msisdnStatus3.setStatusName(ApplicationConstant.STATUS_LOCKED_NAME);
		msisdnStatusList.add(msisdnStatus);
		msisdnStatusList.add(msisdnStatus1);
		msisdnStatusList.add(msisdnStatus2);
		msisdnStatusList.add(msisdnStatus3);
		return msisdnStatusList;
	}

	private List<MSISDNDto> msisdnDtolist() {
		MSISDNDto msisdnto = new MSISDNDto();
		msisdnto.setMsisdnCategoryId(1L);
		msisdnto.setMsisdnId(1234L);
		msisdnto.setSearchCriteria("-");
		msisdnto.setStage(1);
		msisdnto.setMsisdnNumber("9876453876");
		List<MSISDNDto> msisdnDtoList = new ArrayList<>();
		msisdnDtoList.add(msisdnto);
		return msisdnDtoList;
	}

	private List<String> msisdnList() {
		List<String> msisdnList = new ArrayList<>();
		msisdnList.add("9560377500");
		msisdnList.add("9560377501");
		msisdnList.add("9560377502");
		return msisdnList;
	}
}
