package com.prokarma.publisher.converters;
//

import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.model.CustomerRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerMaskConverter implements Converter<CustomerRequest, CustomerRequest> {

    private static final char maskChar = '*';

    @Override
    public CustomerRequest convert(CustomerRequest customerRequest) {
        CustomerRequest maskedCustomerRequest = new CustomerRequest();
        BeanUtils.copyProperties(customerRequest, maskedCustomerRequest);

        maskedCustomerRequest.setCustomerNumber(
                maskString(maskedCustomerRequest.getCustomerNumber(), PublisherConstant.CUSTOMER_NUMBER_MASK.getValue())
        );
        maskedCustomerRequest.setBirthdate(
                maskString(maskedCustomerRequest.getBirthdate(), PublisherConstant.DOB_MASK.getValue())
        );
        maskedCustomerRequest.setEmail(maskString(maskedCustomerRequest.getEmail(), PublisherConstant.EMAIL_MASK.getValue()));

        return maskedCustomerRequest;
    }

    private static String maskString(String maskString, String regex) {
        return maskString.replaceAll(regex, String.valueOf(maskChar));
    }
}
