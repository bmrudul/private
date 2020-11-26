package com.prokarma.publisher.kafkapublisher;

import com.prokarma.publisher.exception.GenericException;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        logger.info("#### -> Producing message -> ");

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, customerRequestKafka);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                logger.info("Sent message=["  +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.info("Unable to send message=["
                        + "] due to : " + ex.getMessage());

                throw new GenericException("Error while publishing message to Kafka. "+ex.getMessage());
            }
        });
    }
}
