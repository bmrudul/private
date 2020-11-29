package com.prokarma.publisher;

import com.prokarma.publisher.controller.PublisherController;
import com.prokarma.publisher.converters.CustomerMaskConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PublisherApplicationTests {

	@Autowired
	PublisherController publisherController;

	@Test
	@DisplayName("Check spring context is created or not.")
	void contextLoads() {
		assertNotNull(publisherController);
	}
}
