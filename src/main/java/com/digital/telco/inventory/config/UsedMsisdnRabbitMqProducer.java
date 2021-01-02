package com.digital.telco.inventory.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digital.telco.inventory.model.UserMsisdnDetails;
import com.digital.telco.notifications.constants.NotificationConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsedMsisdnRabbitMqProducer {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void sendDataToUserService(UserMsisdnDetails userMsisdnDetails) {
		rabbitTemplate.convertAndSend(NotificationConstants.MSISDN_EXCHANGE,
				NotificationConstants.USERDETAILS_MSISDN_ROUTINGKEY, userMsisdnDetails);
		log.info("Sending  msisdn message to user = " + userMsisdnDetails);
		
	}
}