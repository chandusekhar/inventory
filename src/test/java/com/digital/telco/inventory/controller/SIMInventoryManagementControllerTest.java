package com.digital.telco.inventory.controller;

import com.digital.telco.inventory.service.SIMInventoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SIMInventoryManagementController.class)
public class SIMInventoryManagementControllerTest {

    @InjectMocks
    private SIMInventoryManagementController simInventoryManagementController;

    @Mock
    private SIMInventoryService simInventoryService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(simInventoryManagementController).build();
    }

    public SIMInventoryManagementControllerTest() {
    }

    @Test
    public void testValidateICCID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/sim/validate").param("iccid", "123")
        ).andExpect(status().is(HttpStatus.OK.value()));
    }

}
