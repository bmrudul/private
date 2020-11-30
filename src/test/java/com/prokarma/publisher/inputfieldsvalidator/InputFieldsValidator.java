package com.prokarma.publisher.inputfieldsvalidator;

import com.prokarma.publisher.model.Address;
import com.prokarma.publisher.model.CustomerRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class InputFieldsValidator {

    private static Validator validator;

    private static CustomerRequest customerRequest;

    @BeforeAll
    static void setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        customerRequest = createCustomerWithValidInputFields();
    }

    @Test
    void shouldDetectMaxSizeViolationOfCustomerNumber() {
        customerRequest.setCustomerNumber("MaxSizeViolation");

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
        assertEquals(1, violations.size());

        ConstraintViolation<CustomerRequest> violation = violations.iterator().next();
        assertEquals("CustomerNumber is invalid. CustomerNumber must contain only alphabets with max length 10.",
                violation.getMessage());
        assertEquals("customerNumber", violation.getPropertyPath().toString());
    }

    @Test
    void shouldDetectNullViolationOfCustomerNumber() {
        customerRequest.setCustomerNumber(null);

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
        assertEquals(1, violations.size());

        ConstraintViolation<CustomerRequest> violation = violations.iterator().next();
        assertEquals("CustomerNumber must not null.",
                violation.getMessage());
        assertEquals("customerNumber", violation.getPropertyPath().toString());
    }

    static CustomerRequest createCustomerWithValidInputFields() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustomerNumber("C000001");
        customerRequest.setBirthdate("10-12-2020");
        customerRequest.setCountry("India");
        customerRequest.setCountryCode("IN");
        customerRequest.setCustomerStatus(CustomerRequest.CustomerStatusEnum.OPEN);
        customerRequest.setEmail("customer@gmail.com");
        customerRequest.setFirstName("Firstnamevalid");
        customerRequest.setLastName("Lastnamevalid");
        customerRequest.setMobileNumber("9696969696");
        customerRequest.setAddress(createAddressWithValidInputFields());
        return customerRequest;
    }

    static List<Address> createAddressWithValidInputFields() {
        List<Address> addressList = new ArrayList<>();
        Address address = new Address();
        address.setAddressLine1("addressLine1 address");
        address.setAddressLine2("customer_address_l2");
        address.setStreet("customer_address_street");
        address.setPostalCode("12345");
        addressList.add(address);
        return addressList;
    }
}
