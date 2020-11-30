package com.prokarma.publisher.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokarma.publisher.PublisherApplication;
import com.prokarma.publisher.kafkapublisher.KafkaPublisher;
import com.prokarma.publisher.model.Address;
import com.prokarma.publisher.model.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes={ PublisherApplication.class })
class TestCustomerPublisherController {

    MockMvc mockMvc;

    @MockBean
    KafkaPublisher kafkaPublisher;

    @Autowired
    WebApplicationContext webApplicationContext;

    CustomerRequest customerRequest;

    HttpHeaders httpHeaders;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        customerRequest = createValidCustomerObject();
        httpHeaders = setHeaders();
    }

    @Test
    void shouldReturnSuccesWhenValidRequest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/v1/publisher/retail-customer")
                .content(asJsonString(customerRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Success"));
    }

    @Test
    void shouldReturn404WhenInvalidURLRequest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/v1/retail-customer")
                .content(asJsonString(customerRequest))
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn403WhenAuthorizationHeaderMissing() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/v1/publisher/retail-customer")
                .content(asJsonString(customerRequest))
                .header("Activity-id", "activity_10")
                .header("Transaction-id", "transaction_10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                        .value("Request headers are missing Missing request header 'Authorization' for method parameter of type String")
                );
    }

    CustomerRequest createValidCustomerObject() {
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
        customerRequest.setAddress(createValidAddressObject());
        return customerRequest;
    }

    List<Address> createValidAddressObject() {
        List<Address> addressList = new ArrayList<>();
        Address address = new Address();
        address.setAddressLine1("addressLine1 address");
        address.setAddressLine2("customer_address_l2");
        address.setStreet("customer_address_street");
        address.setPostalCode("12345");
        addressList.add(address);
        return addressList;
    }

    HttpHeaders setHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "bearer cc9e91c3-c739-4ff8-b433-fe09de41829b");
        httpHeaders.add("Activity-id", "activity_10");
        httpHeaders.add("Transaction-id", "transaction_10");
        return httpHeaders;
    }

    String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
