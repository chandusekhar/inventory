package com.digital.telco.inventory.service.impl;

import com.digital.telco.inventory.entity.SIM;
import com.digital.telco.inventory.repository.SIMRepository;
import com.digital.telco.inventory.service.SIMInventoryService;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.utility.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SIMInventoryServiceImpl implements SIMInventoryService {

    @Autowired
    SIMRepository simRepository;

    /**
     * This method validates the ICCID number in SIM inventory
     *
     * @param iccidNumber, the user provided iccid number
     *
     * @throws BaseException, if ICCID does exists in inventory or is already linked to some other MSISDN.
     */
    @Override
    public void validateICCID(String iccidNumber) throws BaseException {
        log.debug("SIMInventoryServiceImpl:validateICCID is called to validate ICCID {}", iccidNumber);
        simRepository.findByIccid(iccidNumber).orElseThrow(() ->
                new BaseException("ICCID does not match with any SIM in SIM inventory",
                        ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND)
        );
        log.debug("SIMInventoryServiceImpl:validateICCID is completed without error to validate ICCID {}", iccidNumber);
    }
}
