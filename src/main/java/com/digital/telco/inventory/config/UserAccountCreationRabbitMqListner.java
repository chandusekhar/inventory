package com.digital.telco.inventory.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digital.telco.inventory.dto.UserEntityDto;
import com.digital.telco.inventory.service.MsisdnService;
import com.digital.telco.notifications.constants.NotificationConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserAccountCreationRabbitMqListner {

	@Autowired
	MsisdnService msisdnService;
	
	@RabbitListener(queues = NotificationConstants.USERDETAILS_ACCOUNT_CREATION_QUEUE)
	public void recievedMessage(UserEntityDto userEntityDto) {
    log.info("user account creation listner queue invoked with details {}  " , userEntityDto);
		try {
		msisdnService.changeMsisdnStatus(userEntityDto);	
		}
		catch(Exception e) {
			log.error("error occured");
			log.error(e.getMessage());
		}
		
	}
}
