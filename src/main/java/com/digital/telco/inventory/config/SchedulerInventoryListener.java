package com.digital.telco.inventory.config;

import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digital.telco.inventory.dto.InventorySchedulerDTO;
import com.digital.telco.inventory.service.MsisdnService;
import com.digital.telco.notifications.constants.NotificationConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchedulerInventoryListener {

	@Autowired
	MsisdnService msisdnService;

	@RabbitListener(queues = NotificationConstants.SCHEDULER_INVENTORY_QUEUE)
	public void recievedMessage(InventorySchedulerDTO inventorySchedulerDTO) {

		try {
		String jobName = inventorySchedulerDTO.getName();
		switch (jobName) {
		case ("MSISDN_UNLOCK"):
			log.info("Message recieved for releasing msisdn from scheduler :" + inventorySchedulerDTO);
			List<String> msisdnList = (List<String>) inventorySchedulerDTO.getJobData().get("msisdnList");
			String registrationId = (String) inventorySchedulerDTO.getJobData().get("registrationId");
			Integer status = (Integer) inventorySchedulerDTO.getJobData().get("status");
			Integer msisdnId = (Integer) (inventorySchedulerDTO.getJobData().get("msisdnId"));
			if ((Objects.isNull(msisdnList) || msisdnList.isEmpty()) || Objects.isNull(registrationId)
					|| Objects.isNull(status) || Objects.isNull(msisdnId)) {
				log.error("Error while processing releasing msisdn {}", inventorySchedulerDTO);
				// to do save in a error log table or create a job to handle this.
			} else {
				msisdnService.releaseNumbers(msisdnList, registrationId, Long.valueOf(status), Long.valueOf(msisdnId));
			}
			break;

		case ("MSISDN_UNLOCK_UNORDERED"):
			String msisdn = (String) inventorySchedulerDTO.getJobData().get("msisdn");
			String email = (String) inventorySchedulerDTO.getJobData().get("email");
			if (Objects.isNull(msisdn) ||  Objects.isNull(email)) {
				log.error("Error while processing releasing msisdn {}", inventorySchedulerDTO);
				// to do save in a error log table or create a job to handle this.
			} else {
				msisdnService.releaseNumber(msisdn, email);
			}
			break;

		}
		}
		catch(Exception e) {
			log.error("error occured while processing the request");
			log.error(e.getMessage());
			
		}
	}
}
