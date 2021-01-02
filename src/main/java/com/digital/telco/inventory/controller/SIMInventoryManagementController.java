package com.digital.telco.inventory.controller;

import com.digital.telco.inventory.service.SIMInventoryService;
import com.digital.telco.utility.exception.BaseException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "inventory/sim")
@Validated
@Api(value = "SIM Related Operations")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.TRACE})
public class SIMInventoryManagementController {

    @Autowired
    SIMInventoryService simInventoryService;


    @GetMapping(value = "validate")
    public ResponseEntity<Void> validateICCID(@RequestParam("iccid") String iccid) throws BaseException {
        log.debug("SIMInventoryManagementController: Validate ICCID in Inventory is invoked");
        simInventoryService.validateICCID(iccid);
        log.debug("SIMInventoryManagementController: Validate ICCID in Inventory is completed and was successful");
        return ResponseEntity.ok().build();
    }
}
