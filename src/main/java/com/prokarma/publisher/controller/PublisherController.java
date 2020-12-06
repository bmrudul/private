package com.prokarma.publisher.controller;

import com.prokarma.publisher.converters.CustomerMaskConverter;
import com.prokarma.publisher.kafkapublisher.CustomerPublisherKafka;
import com.prokarma.publisher.model.CustomerRequest;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import com.prokarma.publisher.response.CustomerResponse;
import com.prokarma.publisher.converters.CustomerPublisherConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/publisher")
public class PublisherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerMaskConverter customerMaskConverter;

    @Autowired
    private CustomerPublisherKafka customerPublisherKafka;

    @Autowired
    private CustomerPublisherConverter customerPublisherConverter;

    @PostMapping("/retail-customer")
    public ResponseEntity<CustomerResponse> publishCustomerData(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "Activity-id") String activityId,
            @RequestHeader(value = "Transaction-id") String transactionId,
            @Valid @RequestBody CustomerRequest customerRequest){
        logger.info("InComingRequest : {}", customerMaskConverter.convert(customerRequest));

        //Integration with Kafka - Convert and publish customer data to the Kafka
        CustomerRequestKafka customerRequestKafka = customerPublisherConverter.convert(customerRequest);
        customerRequestKafka.setActivityId(activityId);
        customerRequestKafka.setTransactionId(transactionId);

        CustomerResponse customerResponse = customerPublisherKafka.publish(customerRequestKafka);
        logger.info("OutGoingResponse : {}", customerResponse.toString());
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

}
