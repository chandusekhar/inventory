package com.digital.telco.inventory.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userdetails-service", url = "${userdetails.server.host}")
public interface UserDetailsClient {

	@PutMapping("${set.msisdn.inactive.url}")
	public Integer setMsisdnToInactive(@RequestParam("msisdn") String msisdn);

}