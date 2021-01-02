package com.digital.telco.inventory.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.digital.telco.inventory.dto.MsisdnNumberResponse;
import com.digital.telco.inventory.dto.VanityNumbersPriceInformation;
import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.service.MsisdnCommonService;
import com.digital.telco.inventory.service.MsisdnService;
import com.digital.telco.inventory.service.MsisdnServiceImplTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InventoryManagementController.class)
public class InventoryManagementControllerTest {

	@InjectMocks
	private InventoryManagementController inventoryManagementController;

	@Mock
	private MsisdnService msisdnService;

	@Mock
	private MsisdnCommonService msisdnCommonService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(inventoryManagementController).build();
	}

	public InventoryManagementControllerTest() {
	}

	@Test
	public void testGenerateMsisdnNumber() throws Exception {
		List<MsisdnNumberResponse> msisdnNumberResponseList = mockMsisdnResponseList();
		when(msisdnService.generateMsisdnNumber(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(msisdnNumberResponseList);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/msisdnnumber").param("registrationId", "123")
				.param("categoryIds", "1,2,3").param("source", "App-Android"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testGenerateOwnMsisdnNumber() throws Exception {
		List<MsisdnNumberResponse> msisdnNumberResponseList = mockMsisdnResponseList();
		when(msisdnService.generateOwnMsisdnNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(msisdnNumberResponseList);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/choosemsisdnnumber").param("registrationId", "123")
				.param("pattern", "123").param("categoryIds", "1,2,3").param("source", "App-Android")
				.param("count", "1")).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testConfirmMsisdnNumber() throws Exception {
		MsisdnNumberResponse msisdnNumberResponse = mockMsisdnResponse();
		when(msisdnService.confirmMsisdnNumber(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(msisdnNumberResponse);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/confirmmsisdnnumber").param("registrationId", "123")
				.param("msisdnId", "1").param("categoryId", "1").param("categoryName", "GOLD")
				.param("msisdnNumber", "1").param("source", "app")).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testReviveMsisdnInfo() throws Exception {
		MsisdnNumberResponse msisdnNumberResponse = mockMsisdnResponse();
		when(msisdnService.reviveMsisdnInfo(Mockito.anyString())).thenReturn(msisdnNumberResponse);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/revivemsisdn").param("registrationId", "123"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testCancelMsisdnInfo() throws Exception {
		Mockito.doNothing().when(msisdnService).cancelMsisdnInfo(Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/cancelmsisdn").param("registrationId", "123"))
				.andExpect(status().is(HttpStatus.NO_CONTENT.value()));
	}

	@Test
	public void testGetAllCategeroies() throws Exception {
		List<MSISDNCategory> msisdnCategoryList = new ArrayList<MSISDNCategory>();
		when(msisdnCommonService.getAllCategeories()).thenReturn(msisdnCategoryList);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/categories"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testGetAllStatus() throws Exception {
		List<MSISDNStatus> msisdnStatusList = new ArrayList<MSISDNStatus>();
		when(msisdnCommonService.getAllStatus()).thenReturn(msisdnStatusList);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/status")).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testUpdateMSISDNStatus() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.put("/inventory/msisdn/updatestatus")
				.content(mapper.writeValueAsBytes(MsisdnServiceImplTest.getUserEntityDto()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testGetVanityNumberPrice() throws Exception {
		List<VanityNumbersPriceInformation> priceList = new ArrayList<VanityNumbersPriceInformation>();
		when(msisdnService.getVanityNumberPrice()).thenReturn(priceList);
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/vanity/price").param("registrationId", "123"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void TestGetMsisdnCategory() throws Exception {
		when(msisdnService.getMsisdnStatus("1234")).thenReturn("successBody");
		mockMvc.perform(MockMvcRequestBuilders.get("/inventory/1234/category"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	List<MsisdnNumberResponse> mockMsisdnResponseList() {
		List<MsisdnNumberResponse> msisdnNumberResponseList = new ArrayList<MsisdnNumberResponse>();
		MsisdnNumberResponse msisdnNumberResponse = new MsisdnNumberResponse();
		msisdnNumberResponse.setCategory(1L);
		msisdnNumberResponse.setMsisdnId(1L);
		msisdnNumberResponse.setRegistrationId("123");
		msisdnNumberResponseList.add(msisdnNumberResponse);
		return msisdnNumberResponseList;
	}

	MsisdnNumberResponse mockMsisdnResponse() {
		MsisdnNumberResponse msisdnNumberResponse = new MsisdnNumberResponse();
		msisdnNumberResponse.setCategory(1L);
		msisdnNumberResponse.setMsisdnId(1L);
		msisdnNumberResponse.setRegistrationId("123");
		return msisdnNumberResponse;
	}
}
