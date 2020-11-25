package com.prokarma.publisher.kafkapublisher;

import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TOPIC = "users";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * Method publishes customer data to the kafka server.
     *
     * @param customerRequestKafka
     */
    public void publish(CustomerRequestKafka customerRequestKafka) {

    }
}
