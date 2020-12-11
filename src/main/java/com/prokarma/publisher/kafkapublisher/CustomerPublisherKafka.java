package com.prokarma.publisher.kafkapublisher;

import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.exception.GenericException;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import com.prokarma.publisher.response.CustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerPublisherKafka {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TOPIC = "udv8zj5z-test";

    @Autowired
    private KafkaTemplate<String, CustomerRequestKafka> kafkaTemplate;



    /**
     * Method publishes customer data to the kafka server.
     *
     * @param customerRequestKafka
     */
    public CustomerResponse publish(CustomerRequestKafka customerRequestKafka) {
        try {
            kafkaTemplate.send(TOPIC, customerRequestKafka);
            logger.info("Customer data published to kafka successfully.");
            return customerResponse();
        } catch (KafkaException ex) {
            logger.error("Error while publishing customer data to kafka. {}", ex);
            throw new GenericException("Error while publishing customer data to kafka. "+ex.getMessage());
        }
    }

    private CustomerResponse customerResponse(){
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setStatus(PublisherConstant.SUCCESS.getValue());
        customerResponse.setMessage("Customer data has been published successfully");
        return customerResponse;
    }
}
