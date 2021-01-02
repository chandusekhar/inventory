package com.digital.telco.inventory.service;

import java.util.List;

import com.digital.telco.inventory.dto.MsisdnNumberResponse;
import com.digital.telco.inventory.dto.UserEntityDto;
import com.digital.telco.inventory.dto.VanityNumbersPriceInformation;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.workflow.common.dto.MsisdnMNPDTO;

public interface MsisdnService {

    List<MsisdnNumberResponse> generateMsisdnNumber(String categeoryIds, Integer count) throws BaseException;

    List<MsisdnNumberResponse> generateOwnMsisdnNumber(String pattern, String categeoryIds, Integer count) throws BaseException;

    MsisdnNumberResponse confirmMsisdnNumber(String registrationId, Long msisdnId, String msisdnNumber, Long categeoryId, String categeoryName, String source) throws BaseException;

    void releaseNumbers(List<String> msisdnList, String registrationId, Long statusAvailable, Long msisdnId) throws BaseException;

    MsisdnNumberResponse reviveMsisdnInfo(String registrationId);

    void cancelMsisdnInfo(String registrationId);

    void changeMsisdnStatus(UserEntityDto userEntityDto) throws BaseException;

    void releaseNumber(String msisdn, String email) throws BaseException;

    List<VanityNumbersPriceInformation> getVanityNumberPrice() throws BaseException;

    String getMsisdnStatus(String msisdn) throws BaseException;

    void updateMSISDNStatus(UserEntityDto userEntityDto) throws BaseException;

    void addMNPInInventory(MsisdnMNPDTO mnpdto) throws BaseException;


}
