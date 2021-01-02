package com.digital.telco.inventory.controller;

import com.digital.telco.inventory.dto.MsisdnNumberResponse;
import com.digital.telco.inventory.dto.UserEntityDto;
import com.digital.telco.inventory.dto.VanityNumbersPriceInformation;
import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.service.MsisdnCommonService;
import com.digital.telco.inventory.service.MsisdnService;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.workflow.common.dto.MsisdnMNPDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "inventory")
@Validated
@Api(value = " Msisdn Number Operation")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.TRACE})
public class InventoryManagementController {

    @Autowired
    MsisdnService msisdnService;

    @Autowired
    MsisdnCommonService msisdnCommonService;

    @GetMapping(value = "msisdnnumber")
    public ResponseEntity<List<MsisdnNumberResponse>> generateMsisdnNumber(
            @RequestParam("categoryIds") String categoryIds,
            @RequestParam(value = "count", defaultValue = "1") Integer count) throws BaseException {

        List<MsisdnNumberResponse> msisdnNumberResponseList = msisdnService.generateMsisdnNumber(categoryIds, count);
        return new ResponseEntity<>(msisdnNumberResponseList, HttpStatus.OK);
    }

    @GetMapping(value = "choosemsisdnnumber")
    public ResponseEntity<List<MsisdnNumberResponse>> generateOwnMsisdnNumber(
            @Valid @RequestParam(value = "pattern") @Size(min = 1, max = 6, message = "pattern should be between 1 to 6") String pattern,
            @RequestParam("categoryIds") String categoryIds,
            @RequestParam(value = "count", defaultValue = "200") Integer count) throws BaseException {

        List<MsisdnNumberResponse> msisdnNumberResponseList = msisdnService.generateOwnMsisdnNumber(pattern,
                categoryIds, count);
        return new ResponseEntity<>(msisdnNumberResponseList, HttpStatus.OK);
    }

    @GetMapping(value = "confirmmsisdnnumber")
    public ResponseEntity<MsisdnNumberResponse> confirmMsisdnNumber(
            @RequestParam("registrationId") String registrationId, @RequestParam("msisdnId") Long msisdnId,
            @RequestParam("msisdnNumber") String msisdnNumber, @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName, @RequestParam("source") String source)
            throws BaseException {
        MsisdnNumberResponse msisdnNumberResponse = msisdnService.confirmMsisdnNumber(registrationId, msisdnId,
                msisdnNumber, categoryId, categoryName, source);
        return new ResponseEntity<>(msisdnNumberResponse, HttpStatus.OK);
    }

    @GetMapping(value = "categories")
    public ResponseEntity<List<MSISDNCategory>> getAllCategeroies() {
        List<MSISDNCategory> msisdnCategoryList = msisdnCommonService.getAllCategeories();
        return new ResponseEntity<>(msisdnCategoryList, HttpStatus.OK);
    }

    @GetMapping(value = "status")
    public ResponseEntity<List<MSISDNStatus>> getAllStatus() {
        List<MSISDNStatus> msisdnStatusList = msisdnCommonService.getAllStatus();
        return new ResponseEntity<>(msisdnStatusList, HttpStatus.OK);
    }

    @GetMapping(value = "revivemsisdn")
    public ResponseEntity<MsisdnNumberResponse> reviveMsisdnInfo(
            @RequestParam("registrationId") String registrationId) {
        MsisdnNumberResponse msisdnNumberResponse = msisdnService.reviveMsisdnInfo(registrationId);

        return new ResponseEntity<>(msisdnNumberResponse, HttpStatus.OK);
    }

    @GetMapping(value = "cancelmsisdn")
    public ResponseEntity<Void> cancelMsisdn(@RequestParam("registrationId") String registrationId) {
        msisdnService.cancelMsisdnInfo(registrationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "vanity/price")
    public ResponseEntity<List<VanityNumbersPriceInformation>> getVanityNumberPrice(@RequestParam("registrationId") String registrationId) throws BaseException {
        List<VanityNumbersPriceInformation> vanityNumbersPriceInformation = msisdnService.getVanityNumberPrice();
        return new ResponseEntity<>(vanityNumbersPriceInformation, HttpStatus.OK);
    }


    @GetMapping(value = "{msisdn}/category")
    public ResponseEntity<String> getMsisdnCategory(@PathVariable("msisdn") String msisdn) throws BaseException {
        String msisdnStatus = msisdnService.getMsisdnStatus(msisdn);
        return new ResponseEntity<>(msisdnStatus, HttpStatus.OK);
    }

    @PutMapping(value = "msisdn/updatestatus")
    public ResponseEntity<Void> updateMSISDNStatus(@RequestBody UserEntityDto updateStatusDto) throws BaseException {
        log.info("Update Status msisdn request has been received ");
        msisdnService.updateMSISDNStatus(updateStatusDto);
        log.info("Update Status msisdn is completed ");
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "msisdn/mnp")
    public ResponseEntity<Void> addMNPInInventory(@RequestBody MsisdnMNPDTO mnpDto) throws BaseException {
        log.debug("InventoryManagementController: Add MNP MSISDN in Inventory is invoked");
        msisdnService.addMNPInInventory(mnpDto);
        log.info("InventoryManagementController: Add MNP MSISDN in Inventory is completed");
        return ResponseEntity.ok().build();
    }
}
