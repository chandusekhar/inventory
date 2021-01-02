package com.digital.telco.inventory.service;

import java.util.List;
import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNStatus;

public interface MsisdnCommonService {

	public List<MSISDNStatus> getAllStatus();

	public List<MSISDNCategory> getAllCategeories();

}
