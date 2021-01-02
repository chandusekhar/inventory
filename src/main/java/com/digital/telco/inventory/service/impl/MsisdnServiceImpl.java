package com.digital.telco.inventory.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.digital.telco.inventory.entity.MSISDNCategory;
import com.digital.telco.inventory.entity.MSISDNMnp;
import com.digital.telco.inventory.repository.*;
import com.digital.telco.workflow.common.dto.MsisdnMNPDTO;
import com.digital.telco.workflow.common.enums.MsisdnStatus;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.digital.telco.inventory.config.InventorySchedulerMqProducer;
import com.digital.telco.inventory.config.UsedMsisdnRabbitMqProducer;
import com.digital.telco.inventory.constant.ApplicationConstant;
import com.digital.telco.inventory.dto.CatalogInformation;
import com.digital.telco.inventory.dto.InventorySchedulerDTO;
import com.digital.telco.inventory.dto.MSISDNDto;
import com.digital.telco.inventory.dto.MetadataList;
import com.digital.telco.inventory.dto.MsisdnConfirmDto;
import com.digital.telco.inventory.dto.MsisdnNumberResponse;
import com.digital.telco.inventory.dto.MsisdnReviveDto;
import com.digital.telco.inventory.dto.Results;
import com.digital.telco.inventory.dto.UserEntityDto;
import com.digital.telco.inventory.dto.VanityNumbersPriceInformation;
import com.digital.telco.inventory.entity.MSISDN;
import com.digital.telco.inventory.entity.MSISDNStatus;
import com.digital.telco.inventory.model.UserMsisdnDetails;
import com.digital.telco.inventory.service.MsisdnCommonService;
import com.digital.telco.inventory.service.MsisdnService;
import com.digital.telco.inventory.service.client.UserDetailsClient;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.utility.exception.ErrorCodes;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsisdnServiceImpl implements MsisdnService {

	@Autowired
	MsisdnRepository msisdnRepository;

	@Autowired
	MsisdnDtoRepository msisdnDtoRepository;

	@Autowired
	MsisdnConfirmRepository msisdnConfirmRepository;

	@Autowired
	MsisdnReviveRepository msisdnReviveRepository;

	@Autowired
	UsedMsisdnRabbitMqProducer rabbitMqProducer;

	@Autowired
	MessageSource messageSource;

	@Autowired
	MsisdnCommonService msisdnCommonService;

	@Autowired
	UserMsisdnMappingRepository userMsisdnMappingRepository;

	@Autowired
	InventorySchedulerMqProducer schedulerMqProducer;

	@Autowired
	MsisdnMNPRepository msisdnMNPRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${matrixx.adaptor.catalog.url}")
	String matrixxCatalogUrl;

	@Value("${msisdn.reserved.expiry.time}")
	String reservedTime;

	@Autowired
	MsisdnCategeoryRepository msisdnCategeoryRepository;

	@Autowired
	UserDetailsClient userDetailsClient;

	@Override
	public List<MsisdnNumberResponse> generateMsisdnNumber(String categeoryIds, Integer count) throws BaseException {
		List<MSISDNDto> msisdnSuggestedList;

		List<Long> msisdnStatusIds = getMsisdnStatus(Arrays.asList(ApplicationConstant.STATUS_AVAILABLE_NAME));
		log.info("msisdn status ids list is {}", msisdnStatusIds);
		if (msisdnStatusIds.size() == 1) {
			msisdnSuggestedList = msisdnDtoRepository.getSuggestedMsisdn(ApplicationConstant.SEARCH_CRITERIA_BLANK,
					msisdnStatusIds.get(0), categeoryIds, count);

		} else {
			String errorMessage = messageSource.getMessage(ApplicationConstant.INVALID_STATUS, null,
					LocaleContextHolder.getLocale());
			throw new BaseException(errorMessage, ErrorCodes.INVALID_STATUS, HttpStatus.NOT_FOUND);
		}
		if (msisdnSuggestedList.isEmpty()) {
			String errorMessage = messageSource.getMessage("number.not.found", null, LocaleContextHolder.getLocale());
			throw new BaseException(errorMessage, ErrorCodes.NUMBER_NOT_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		return generateResponse(msisdnSuggestedList);

	}

	private List<Long> getMsisdnStatus(List<String> reqStatus) {
		List<MSISDNStatus> msisdnStatusList = msisdnCommonService.getAllStatus();
		Map<String, Long> statusMap = msisdnStatusList.stream()
				.filter(msisdnStatus -> reqStatus.contains(msisdnStatus.getStatusName().toUpperCase()))
				.collect(Collectors.toMap(MSISDNStatus::getStatusName, MSISDNStatus::getId));
		List<Long> statusList = new ArrayList<>();
		Map<String, Long> upperCaseMap = statusMap.entrySet().stream()
				.collect(Collectors.toMap(keyMapper -> keyMapper.getKey().toUpperCase(), Map.Entry::getValue));
		for (int i = 0; i < reqStatus.size(); i++) {
			if (Objects.nonNull(upperCaseMap.get(reqStatus.get(i)))) {
				statusList.add(upperCaseMap.get(reqStatus.get(i)));
			}
		}
		return statusList;
	}

	private List<MsisdnNumberResponse> generateResponse(List<MSISDNDto> msisdnSuggestedList) {
		List<MsisdnNumberResponse> msisdnNumberResponseList = new ArrayList<>();
		msisdnSuggestedList.stream().forEach(msisdnDto -> {
			MsisdnNumberResponse msisdnNumberResponse = new MsisdnNumberResponse();
			msisdnNumberResponse.setCategory(msisdnDto.getMsisdnCategoryId());
			msisdnNumberResponse.setPrice(msisdnDto.getMsisdnPrice());
			msisdnNumberResponse.setMsisdnNumber(msisdnDto.getMsisdnNumber());
			msisdnNumberResponse.setMsisdnId(msisdnDto.getMsisdnId());
			msisdnNumberResponse.setSearchCriteria(msisdnDto.getSearchCriteria());
			msisdnNumberResponse.setStage(msisdnDto.getStage());
			msisdnNumberResponse.setCategoryName(msisdnDto.getMsisdnCategoryName());
			msisdnNumberResponseList.add(msisdnNumberResponse);

		});

		return msisdnNumberResponseList;
	}

	@Override
	public List<MsisdnNumberResponse> generateOwnMsisdnNumber(String pattern, String categeoryIds, Integer count)
			throws BaseException {
		List<MSISDNDto> msisdnSuggestedList;
		List<Long> msisdnStatusIds = getMsisdnStatus(Arrays.asList(ApplicationConstant.STATUS_AVAILABLE_NAME));
		log.info(ApplicationConstant.COMMONLOG, msisdnStatusIds);
		if (msisdnStatusIds.size() == 1) {

			msisdnSuggestedList = msisdnDtoRepository.getSuggestedMsisdn(pattern, ApplicationConstant.STATUS_AVAILABLE,
					categeoryIds, count);

		} else {
			String errorMessage = messageSource.getMessage(ApplicationConstant.INVALID_STATUS, null,
					LocaleContextHolder.getLocale());
			throw new BaseException(errorMessage, ErrorCodes.INVALID_STATUS, HttpStatus.NOT_FOUND);
		}
		if (Objects.isNull(msisdnSuggestedList) || msisdnSuggestedList.isEmpty()) {
			String errorMessage = messageSource.getMessage("number.not.found", null, LocaleContextHolder.getLocale());
			throw new BaseException(errorMessage, ErrorCodes.NUMBER_NOT_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		return generateResponse(msisdnSuggestedList);

	}

	@Override
	public MsisdnNumberResponse confirmMsisdnNumber(String registrationId, Long msisdnId, String msisdnNumber,
			Long categeoryId, String categeoryName, String source) throws BaseException {

		MsisdnConfirmDto msisdnConfirmDto = null;
		List<Long> msisdnStatusIds = getMsisdnStatus(
				Arrays.asList(ApplicationConstant.STATUS_AVAILABLE_NAME, ApplicationConstant.STATUS_RESERVED_NAME));
		log.info(ApplicationConstant.COMMONLOG, msisdnStatusIds);
		if (msisdnStatusIds.size() == 2) {

			msisdnConfirmDto = msisdnConfirmRepository.confirmMsisdn(registrationId, msisdnId, msisdnStatusIds.get(0),
					msisdnStatusIds.get(1), Integer.valueOf(reservedTime), source);
			if (Objects.nonNull(msisdnConfirmDto) && Objects.nonNull(msisdnConfirmDto.getUserMappingId())) {
				log.info("expiry time for reserved number is {} ", msisdnConfirmDto.getExpiryTime());
				log.info("sending reserved option to Mq");
				sendResponseToMq(registrationId, msisdnNumber, categeoryId, categeoryName);
			} else {
				log.info("expiry time for reserved number is  null");
				String errorMessage = messageSource.getMessage("number.already.mapped", null,
						LocaleContextHolder.getLocale());
				throw new BaseException(errorMessage, ErrorCodes.NUMBER_ALREADY_MAPPED, HttpStatus.CONFLICT);
			}

		} else {
			String errorMessage = messageSource.getMessage(ApplicationConstant.INVALID_STATUS, null,
					LocaleContextHolder.getLocale());
			throw new BaseException(errorMessage, ErrorCodes.INVALID_STATUS, HttpStatus.NOT_FOUND);
		}
		MsisdnNumberResponse msisdnResponse = new MsisdnNumberResponse();
		msisdnResponse.setCategory(categeoryId);
		msisdnResponse.setExpiryTime(msisdnConfirmDto.getExpiryTime());
		msisdnResponse.setMsisdnId(msisdnId);
		msisdnResponse.setRegistrationId(registrationId);
		msisdnResponse.setMsisdnNumber(msisdnNumber);
		msisdnResponse.setCategoryName(categeoryName);

		List<String> msisdnList = new ArrayList<>();
		msisdnList.add(msisdnNumber);
		scheduleJob(registrationId, msisdnList, msisdnStatusIds.get(1), msisdnId);
		return msisdnResponse;

	}

	private void sendResponseToMq(String registrationId, String msisdnNumber, Long categeoryId, String categeoryName) {
		UserMsisdnDetails userMsisdnDetails = new UserMsisdnDetails();
		userMsisdnDetails.setCategeory(categeoryName);
		userMsisdnDetails.setCategeoryId(categeoryId);
		userMsisdnDetails.setMsisdnNumber(msisdnNumber);
		userMsisdnDetails.setMsisdnStatus(ApplicationConstant.STATUS_RESERVED_NAME);
		userMsisdnDetails.setRegistrationId(registrationId);
		rabbitMqProducer.sendDataToUserService(userMsisdnDetails);
	}

	@Override
	@Transactional
	public void releaseNumbers(List<String> msisdnList, String registrationId, Long currentStatus, Long msisdnId)
			throws BaseException {

		List<String> reqStatusList = new ArrayList<>();
		reqStatusList.add(ApplicationConstant.STATUS_LOCKED_NAME);
		reqStatusList.add(ApplicationConstant.STATUS_RESERVED_NAME);
		reqStatusList.add(ApplicationConstant.STATUS_AVAILABLE_NAME);

		List<Long> msisdnStatusIds = getMsisdnStatus(reqStatusList);
		log.info("getMsisdnStatus output" + msisdnStatusIds);
		MSISDN msisdnQuery = msisdnRepository.findById(msisdnId)
				.orElseThrow(() -> new BaseException("No msisdn found", ErrorCodes.NOT_FOUND, HttpStatus.BAD_REQUEST));
		if (currentStatus.equals(msisdnQuery.getStatus().getId())) {
			String msisdn = msisdnList.get(0);// as this would be only for one nuomber
			log.info("Release number {} with status Reserved", msisdn);

			if (null != msisdnId) {
				msisdnDtoRepository.releaseReservedMSISDN(registrationId, msisdnStatusIds.get(2), false);
				log.info("Scheduler execution completed");
				log.info("send request to msisdn inactive start ", msisdn);
				Optional<MSISDN> msisdnObj = msisdnRepository.findByMsisdnNumber(msisdn);
				if (msisdnObj.isPresent() && msisdnObj.get().getStatus().getStatusName()
						.equalsIgnoreCase(ApplicationConstant.STATUS_AVAILABLE_NAME)) {
					userDetailsClient.setMsisdnToInactive(msisdn);
					log.info("send request to msisdn inactive completed ", msisdn);
				}

			} else {
				log.error("Error while releasing {} from Reserved to Available,msisdn number not found", msisdn);
			}

		} else {
			log.info("No condition is satisfied.");
		}

	}

	private void scheduleJob(String registrationId, List<String> msisdnList, Long currentStatus, Long msisdnId) {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put(ApplicationConstant.STATUS, currentStatus);
		jobData.put(ApplicationConstant.REGISTRATIONID, registrationId);
		jobData.put(ApplicationConstant.MSISDN_LIST, msisdnList);
		jobData.put(ApplicationConstant.MSISDN_ID, msisdnId);
		schedulerMqProducer.setUnlockJob(new InventorySchedulerDTO(ApplicationConstant.MSISDN_UNLOCK_JOB,
				ApplicationConstant.JOB_GROUP_DEFAULT, jobData));

	}

	@Override
	public MsisdnNumberResponse reviveMsisdnInfo(String registrationId) {
		MsisdnReviveDto msisdnReviveDto = null;
		List<Long> msisdnStatusIds = getMsisdnStatus(
				Arrays.asList(ApplicationConstant.STATUS_AVAILABLE_NAME, ApplicationConstant.STATUS_RESERVED_NAME));
		log.info(ApplicationConstant.COMMONLOG, msisdnStatusIds);
		if (msisdnStatusIds.size() == 2) {

			msisdnReviveDto = msisdnReviveRepository.reviveMsisdn(registrationId, msisdnStatusIds.get(0),
					msisdnStatusIds.get(1), Integer.valueOf(reservedTime));

			MsisdnNumberResponse msisdnResponse = new MsisdnNumberResponse();
			msisdnResponse.setCategory(msisdnReviveDto.getMsisdnCategoryId());
			msisdnResponse.setExpiryTime(msisdnReviveDto.getExpiryTime());
			msisdnResponse.setMsisdnId(msisdnReviveDto.getMsisdnId());
			msisdnResponse.setRegistrationId(registrationId);
			msisdnResponse.setMsisdnNumber(msisdnReviveDto.getMsisdnNumber());
			msisdnResponse.setCategoryName(msisdnReviveDto.getMsisdnCatName());
			msisdnResponse.setCategoryName(msisdnReviveDto.getMsisdnCatName());
			msisdnResponse.setPrice(msisdnReviveDto.getPrice());
			msisdnResponse.setReviveMsisdnMsg(msisdnReviveDto.getResponseMsg());

			if (msisdnReviveDto.getResponseMsg().equalsIgnoreCase(ApplicationConstant.EXPIRY_EXTENDED)
					|| msisdnReviveDto.getResponseMsg().equalsIgnoreCase(ApplicationConstant.REACQUIRED)) {

				scheduleJob(registrationId, Arrays.asList(msisdnReviveDto.getMsisdnNumber()), msisdnStatusIds.get(1),
						msisdnReviveDto.getMsisdnId());

			}
			return msisdnResponse;
		}

		return null;
	}

	@Override
	@Transactional
	public void cancelMsisdnInfo(String registrationId) {
		List<Long> msisdnStatusIds = getMsisdnStatus(Arrays.asList(ApplicationConstant.STATUS_AVAILABLE_NAME));
		msisdnDtoRepository.releaseReservedMSISDN(registrationId, msisdnStatusIds.get(0), true);

	}

	@Override
	@Transactional(rollbackOn = BaseException.class)
	public void changeMsisdnStatus(UserEntityDto userEntityDto) throws BaseException {
		userMsisdnMappingRepository.changeStatusForMsisdnNumber(true, userEntityDto.getPhoneNo());
		String msisdnNumber = userEntityDto.getPhoneNo();
		updateMSISDNStatusInDB(msisdnNumber, userEntityDto.getInitialStatus(), userEntityDto.getNewStatus());
		scheduleJob(userEntityDto);

	}

	private void scheduleJob(UserEntityDto userEntityDto) {

		Map<String, Object> jobData = new HashMap<>();
		jobData.put(ApplicationConstant.MSISDN, userEntityDto.getPhoneNo());
		jobData.put(ApplicationConstant.EMAIL, userEntityDto.getEmailId());
		schedulerMqProducer.setUnlockJob(new InventorySchedulerDTO(ApplicationConstant.MSISDN_UNLOCK_UNORDERED,
				ApplicationConstant.JOB_GROUP_DEFAULT, jobData));

	}

	@Override
	@Transactional
	public void releaseNumber(String msisdn, String email) throws BaseException {

		updateMSISDNStatusInDB(msisdn, ApplicationConstant.STATUS_PICKED2, ApplicationConstant.STATUS_AVAILABLE_NAME);
		// feign client call to userservice for making the msisdn number inactive
		Optional<MSISDN> msisdnObj = msisdnRepository.findByMsisdnNumber(msisdn);
		if (msisdnObj.isPresent() && msisdnObj.get().getStatus().getStatusName()
				.equalsIgnoreCase(ApplicationConstant.STATUS_AVAILABLE_NAME)) {
			userDetailsClient.setMsisdnToInactive(msisdn);
			log.info("send request to msisdn inactive completed ", msisdn);
		}

	}

	@Override
	@Scheduled(fixedRate = 86400000)
	public List<VanityNumbersPriceInformation> getVanityNumberPrice() throws BaseException {

		log.info("in vanity price update schedular");
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		List<VanityNumbersPriceInformation> vanityPriceInformation = null;
		CatalogInformation catalog = null;
		ResponseEntity<String> catalogInformation = null;
		try {
			log.info("going to send catalog fetch information to matrixx for url {} ", matrixxCatalogUrl);
			catalogInformation = restTemplate.exchange(matrixxCatalogUrl, HttpMethod.GET,
					new HttpEntity<>(null, header), String.class);
			log.info("catalog information from matrixx is {} ", catalogInformation);
			catalog = objectMapper.readValue(catalogInformation.getBody(), CatalogInformation.class);
			vanityPriceInformation = getVanityNumberPrice(catalog);
			updatePriceForVanityNumbers(vanityPriceInformation);
		} catch (Exception e) {

			throw new BaseException(e.getMessage(), ErrorCodes.MATRIX_CONNECTION_ERROR, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return vanityPriceInformation;
	}

	@Transactional
	private void updatePriceForVanityNumbers(List<VanityNumbersPriceInformation> vanityPriceInformationList) {

		if (Objects.nonNull(vanityPriceInformationList) && !vanityPriceInformationList.isEmpty()) {
			for (VanityNumbersPriceInformation vanityNumbersPriceInformation : vanityPriceInformationList) {
				msisdnCategeoryRepository.updatePriceForVanityNumbers(vanityNumbersPriceInformation.getCategoryName(),
						vanityNumbersPriceInformation.getCategoryPrice());
			}

		}
	}

	public List<VanityNumbersPriceInformation> getVanityNumberPrice(CatalogInformation catalog) {

		List<VanityNumbersPriceInformation> vanityPriceList = null;
		for (Results results : catalog.getResults()) {

			if (Objects.nonNull(results.getMetadataList())) {
				for (MetadataList metaList : results.getMetadataList()) {
					if (Objects.nonNull(metaList.getName())
							&& metaList.getName().equalsIgnoreCase(ApplicationConstant.VanityNumberPrice)) {
						vanityPriceList = new ArrayList<>();
						getPriceDetails(metaList, vanityPriceList);
					}
				}
			}
		}
		return vanityPriceList;
	}

	public List<VanityNumbersPriceInformation> getPriceDetails(MetadataList metaList,
			List<VanityNumbersPriceInformation> vanityPriceList) {

		String vanityPrice = metaList.getValue();
		String[] vanityPriceCombination = vanityPrice.split(";");
		for (String vanityCombination : vanityPriceCombination) {
			VanityNumbersPriceInformation vanityNumbersPriceInformation = new VanityNumbersPriceInformation();
			String[] combinationStr = vanityCombination.split(",");
			vanityNumbersPriceInformation.setCategoryName(combinationStr[0]);
			vanityNumbersPriceInformation.setCategoryPrice(combinationStr[1] + " " + ApplicationConstant.IQD);
			vanityPriceList.add(vanityNumbersPriceInformation);
		}

		return vanityPriceList;
	}

	@Override
	public String getMsisdnStatus(String msisdn) throws BaseException {
		// TODO Auto-generated method stub
		String msisdnCategory = null;
		Optional<MSISDN> msisdnInformation = msisdnRepository.getCategoryForMsisdn(msisdn);

		if (msisdnInformation.isPresent()) {
			msisdnCategory = msisdnInformation.get().getCategory().getCategoryName();
		}

		return msisdnCategory;
	}

	/**
	 * It updates the MSISDN status in the system. The provided status must be valid
	 * status.
	 *
	 * @param userEntityDto the request body with the required details
	 *
	 * @throws BaseException if the provided status are valid.
	 */
	@Override
	@Transactional
	public void updateMSISDNStatus(UserEntityDto userEntityDto) throws BaseException {
		log.debug("MSISDN status updated called is intercepted in Service");
		updateMSISDNStatusInDB(userEntityDto.getPhoneNo(), userEntityDto.getInitialStatus(),
				userEntityDto.getNewStatus());
		log.debug("MSISDN status updated called is completed in Service");
	}

	/**
	 * This method adds MNP number in the Inventory.
	 *
	 * @param mnpDto The request body
	 *
	 * @throws BaseException if any exception occurs
	 */
	@Override
	public void addMNPInInventory(MsisdnMNPDTO mnpDto) throws BaseException {

		Optional<MSISDNMnp> mnpOptional = msisdnMNPRepository.findByMsisdnNumber(mnpDto.getMsisdnNumber());

		if (mnpOptional.isPresent()) {
			log.error("{} is already added in MNP Inventory", mnpDto.getMsisdnNumber());
			throw new BaseException("NUMBER_ALREADY_ADDED_IN_INVENTORY", ErrorCodes.ALREADY_EXISTS,
					HttpStatus.BAD_REQUEST);
		} else {
			List<Long> msisdnStatusIds = getMsisdnStatus(Lists.newArrayList(MsisdnStatus.PURCHASED.name()));
			MSISDNMnp mnp = new MSISDNMnp();
			mnp.setMsisdnNumber(mnpDto.getMsisdnNumber());
			mnp.setServiceProvider(2L);

			MSISDNStatus status = new MSISDNStatus();
			status.setId(msisdnStatusIds.get(0));
			mnp.setStatus(status);

			MSISDNCategory category = new MSISDNCategory();
			category.setId(1L);
			mnp.setCategory(category);

			msisdnMNPRepository.save(mnp);
		}
	}

	private void updateMSISDNStatusInDB(String msisdn, String initialStatus, String newStatus) throws BaseException {
		List<Long> msisdnStatusIds = getMsisdnStatus(Arrays.asList(initialStatus, newStatus));
		if (msisdnStatusIds.size() == 2) {
			log.debug("Provided MSISDN statuses are valid");
			msisdnRepository.changeStatusForMsisdnNumber(msisdnStatusIds.get(0), msisdnStatusIds.get(1), msisdn);
		} else {
			String errorMessage = messageSource.getMessage(ApplicationConstant.INVALID_STATUS, null,
					LocaleContextHolder.getLocale());
			log.error("Provided MSISDN statuses did not match the status list");
			throw new BaseException(errorMessage, ErrorCodes.INVALID_STATUS, HttpStatus.NOT_FOUND);
		}
	}
}
