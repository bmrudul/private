package com.prokarma.publisher.controller;

import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.converters.CustomerMaskConverter;
import com.prokarma.publisher.kafkapublisher.KafkaPublisher;
import com.prokarma.publisher.model.CustomerRequest;
import com.prokarma.publisher.response.Response;
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
    CustomerMaskConverter publisherMaskConverter;

    @Autowired
    KafkaPublisher kafkaPublisher;

    @Autowired
    CustomerPublisherConverter customerPublisherConverter;

    @PostMapping("/retail-customer")
    public ResponseEntity<Response> publishCustomerData(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "Activity-id") String activityId,
            @RequestHeader(value = "Transaction-id") String transactionId,
            @Valid @RequestBody CustomerRequest customerRequest){
        logger.info("InComingRequest : " + publisherMaskConverter.convert(customerRequest));

        //Integration with Kafka - Convert and publish customer data to the Kafka
        kafkaPublisher.publish(customerPublisherConverter.convert(customerRequest));

        Response response = new Response();
        response.setStatus(PublisherConstant.SUCCESS.getValue());
        response.setMessage("Customer data has been published successfully");
        logger.info("OutGoingResponse : "+response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
