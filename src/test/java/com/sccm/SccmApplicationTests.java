package com.sccm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sccm.services.EmailService;

@SpringBootTest
class SccmApplicationTests {

	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest(){
		service.sendEmail("vedantkarlekar1@gmail.com", "Testing Email Service final configuration", "Hello from SCCM");
	}

}
