package com.prokarma.publisher;

import com.prokarma.publisher.controller.PublisherController;
import static org.junit.jupiter.api.Assertions.*;

import com.prokarma.publisher.model.CustomerRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@SpringBootTest
class PublisherApplicationTests {

	@Autowired
	private PublisherController publisherController;

	private static Validator validator;

	@BeforeAll
	public static void setup(){
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("Check spring context is created or not.")
	void contextLoads() {
		assertNotNull(publisherController);
	}

	@Test
	void testContactSuccess() {
		// I'd name the test to something like
		// invalidEmailShouldFailValidation()

		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setCustomerNumber("C00");
		Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
		assertNull(
				violations.stream()
						.filter(x-> x.getPropertyPath().toString().equals("customerNumber"))
						.findFirst()
						.orElse(null)
		);
	}

	@Test
	public void shouldDetectInvalidName() {
		//given too short name:
		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setCustomerNumber("customer_request");

		//when:
		Set<ConstraintViolation<CustomerRequest>> violations
				= validator.validate(customerRequest);

		//then:
		assertEquals(violations.size(), 1);

		ConstraintViolation<CustomerRequest> violation
				= violations.iterator().next();
		assertEquals("size must be between 3 and 3",
				violation.getMessage());
		assertEquals("name", violation.getPropertyPath().toString());
		assertEquals("a", violation.getInvalidValue());
	}

}
