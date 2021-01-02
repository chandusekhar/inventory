package com.digital.telco.inventory.service;

import com.digital.telco.inventory.constant.ApplicationConstant;
import com.digital.telco.inventory.dto.*;
import com.digital.telco.inventory.entity.*;
import com.digital.telco.inventory.repository.SIMRepository;
import com.digital.telco.inventory.service.impl.SIMInventoryServiceImpl;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.workflow.common.dto.MsisdnMNPDTO;
import com.digital.telco.workflow.common.enums.MsisdnStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test cases for {@link SIMInventoryServiceImpl} covering all possible
 * scenarios.
 */
@RunWith(MockitoJUnitRunner.class)
public class SIMInventoryServiceImplTest {

    @InjectMocks
    private SIMInventoryServiceImpl simInventoryService;

    @Mock
    private SIMRepository simRepository;

    @Test
    public void testValidateICCID() throws BaseException {

        when(simRepository.findByIccid(any(String.class))).thenReturn(Optional.of(new SIM()));
        try {
            simInventoryService.validateICCID("8996420088800000000");
        } catch (BaseException exception) {
            fail("This should not have failed");
        }
    }

    @Test(expected = BaseException.class)
    public void testValidateICCIDException() throws BaseException {

        when(simRepository.findByIccid(any(String.class))).thenReturn(Optional.empty());
        simInventoryService.validateICCID("8996420088800000000");
    }


}
