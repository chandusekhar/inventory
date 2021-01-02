package com.digital.telco.inventory.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digital.telco.inventory.dto.InventorySchedulerDTO;
import com.digital.telco.notifications.constants.NotificationConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventorySchedulerMqProducer {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void setUnlockJob(InventorySchedulerDTO schedulerData) {
		rabbitTemplate.convertAndSend(NotificationConstants.MSISDN_EXCHANGE,
				NotificationConstants.INVENTORY_SCHEDULER_ROUTINGKEY, schedulerData);
		log.info("Sending  msisdn message to user = " + schedulerData);

	}
	
}
