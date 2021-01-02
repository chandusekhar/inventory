package com.digital.telco.inventory.service;

import com.digital.telco.utility.exception.BaseException;

public interface SIMInventoryService {

    void validateICCID(String iccidNumber) throws BaseException;

}
