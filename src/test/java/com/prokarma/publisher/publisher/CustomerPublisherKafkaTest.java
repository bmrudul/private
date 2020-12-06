package com.prokarma.publisher.publisher;

import com.prokarma.publisher.kafkapublisher.CustomerPublisherKafka;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import com.prokarma.publisher.response.CustomerResponse;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class CustomerPublisherKafkaTest {

    @InjectMocks
    private CustomerPublisherKafka customerPublisherKafka;

    @Mock
    private KafkaTemplate<String, CustomerRequestKafka> kafkaTemplate;

    @Test
    public void testSuccessCustomerResponseWhenPublishDataToKafka(){
        CustomerResponse customerResponse = customerPublisherKafka.publish(Mockito.any(CustomerRequestKafka.class));
        assertThat(customerResponse.getStatus()).isEqualTo("Success");
    }
}
