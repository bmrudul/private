package com.prokarma.publisher.converters;

import com.prokarma.publisher.model.CustomerRequest;
import com.prokarma.publisher.model.kafkamodel.CustomerRequestKafka;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerPublisherConverter implements Converter<CustomerRequest, CustomerRequestKafka> {

    @Override
    public CustomerRequestKafka convert(CustomerRequest customerRequest){
        CustomerRequestKafka customerRequestKafka = new CustomerRequestKafka();
        BeanUtils.copyProperties(customerRequest, customerRequestKafka);
        customerRequestKafka.setCustomerStatus(
                CustomerRequestKafka.CustomerStatusEnum.fromValue(customerRequest.getCustomerStatus().toString())
        );
        return customerRequestKafka;
    }

}
