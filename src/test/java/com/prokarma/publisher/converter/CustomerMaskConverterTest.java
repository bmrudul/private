package com.prokarma.publisher.converter;

import com.prokarma.publisher.converters.CustomerMaskConverter;
import com.prokarma.publisher.model.Address;
import com.prokarma.publisher.model.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerMaskConverterTest {

    private CustomerMaskConverter customerMaskConverter;

    @BeforeEach
    public void setup(){
        customerMaskConverter = new CustomerMaskConverter();
    }

    @Test
    public void testMaskingLogicWhenPassedValidCustomerFields(){
        CustomerRequest maskedCustomer = customerMaskConverter.convert(createCustomerWithValidInputFields());
        assertNotNull(maskedCustomer);
        assertEquals("C00**", maskedCustomer.getCustomerNumber());
        assertEquals("*-2020", maskedCustomer.getBirthdate());
        assertEquals("*omer@gmail.com", maskedCustomer.getEmail());
    }

    private CustomerRequest createCustomerWithValidInputFields() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustomerNumber("C000001");
        customerRequest.setBirthdate("10-12-2020");
        customerRequest.setCountry("India");
        customerRequest.setCountryCode("IN");
        customerRequest.setCustomerStatus(CustomerRequest.CustomerStatusEnum.OPEN);
        customerRequest.setEmail("customer@gmail.com");
        customerRequest.setFirstName("FirstnameValid");
        customerRequest.setLastName("LastnameValid");
        customerRequest.setMobileNumber("9696969696");
        customerRequest.setAddress(createAddressWithValidInputFields());
        return customerRequest;
    }

    private List<Address> createAddressWithValidInputFields() {
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
