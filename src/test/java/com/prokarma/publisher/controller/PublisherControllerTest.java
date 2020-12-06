package com.prokarma.publisher.controller;

import com.prokarma.publisher.converters.CustomerMaskConverter;
import com.prokarma.publisher.converters.CustomerPublisherConverter;
import com.prokarma.publisher.exceptionhandler.CustomExceptionHandler;
import com.prokarma.publisher.kafkapublisher.CustomerPublisherKafka;
import com.prokarma.publisher.model.Address;
import com.prokarma.publisher.model.CustomerRequest;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import com.prokarma.publisher.util.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class PublisherControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PublisherController publisherController;

    @Mock
    private CustomerMaskConverter customerMaskConverter;

    @Mock
    private CustomerPublisherKafka customerPublisherKafka;

    @Mock
    private CustomerPublisherConverter customerPublisherConverter;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(publisherController)
                .setControllerAdvice(new CustomExceptionHandler()).build();
    }

    @Test
    public void shouldReturnSuccessWhenValidRequest() throws Exception {
        Mockito.doReturn(new CustomerRequestKafka())
                .when(customerPublisherConverter)
                .convert(Mockito.any(CustomerRequest.class));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/publisher/retail-customer")
                .content(ObjectMapperUtil.returnJsonFromObject(createValidCustomerObject()))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(createHeaders())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldReturn403WhenAuthorizationHeaderMissing() throws Exception {
        MockHttpServletResponse response = mockMvc.perform( post("/v1/publisher/retail-customer")
                .content(ObjectMapperUtil.returnJsonFromObject(createValidCustomerObject()))
                .header("Activity-id", "activity_10")
                .header("Transaction-id", "transaction_10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturn404WhenInvalidURLRequest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform( post("/v1/retail-customer")
                .content(ObjectMapperUtil.returnJsonFromObject(createValidCustomerObject()))
                .headers(createHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private CustomerRequest createValidCustomerObject() {
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
        customerRequest.setAddress(createValidAddressObject());
        return customerRequest;
    }

    private List<Address> createValidAddressObject() {
        List<Address> addressList = new ArrayList<>();
        Address address = new Address();
        address.setAddressLine1("addressLine1 address");
        address.setAddressLine2("customer_address_l2");
        address.setStreet("customer_address_street");
        address.setPostalCode("12345");
        addressList.add(address);
        return addressList;
    }

    private HttpHeaders createHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "bearer cc9e91c3-c739-4ff8-b433-fe09de41829b");
        httpHeaders.add("Activity-id", "activity_10");
        httpHeaders.add("Transaction-id", "transaction_10");
        return httpHeaders;
    }

    //https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
}
