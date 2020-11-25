package com.prokarma.publisher.converters;
//

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
                maskString(maskedCustomerRequest.getCustomerNumber(), "(\\w{0,4})$")
        );
        maskedCustomerRequest.setBirthdate(
                maskString(maskedCustomerRequest.getBirthdate(), "^([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))")
        );
        maskedCustomerRequest.setEmail(maskString(maskedCustomerRequest.getEmail(), "^(\\w{0,4})"));

        return maskedCustomerRequest;
    }

    private static String maskString(String maskString, String regex) {
        return maskString.replaceAll(regex, String.valueOf(maskChar));
    }





/*
    private static String maskString(String maskString, int startIndex, int endIndex) {
        if(startIndex < 0)
            startIndex = 0;
        if( endIndex > maskString.length() )
            endIndex = maskString.length();
        if(startIndex > endIndex)
            throw new GenericException("End index cannot be greater than start index");

        int maskLength = endIndex - startIndex;
        if(maskLength == 0)
            return maskString;

        StringBuilder sbMaskString = new StringBuilder(maskLength);
        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }

        return maskString.substring(0, startIndex)
                + sbMaskString.toString()
                + maskString.substring(startIndex + maskLength);
    }




    Set fieldSet=new HashSet();
    String[] spiData = { "customerNumber", "cvv", "expDate" };

    public String convert(Object object) {
        StringBuffer buffer=new StringBuffer();

            printObjectStream(buffer,object);
        logger.info(buffer.toString());
        return buffer.toString();
    }

    private void printObjectStream(StringBuffer buffer, Object object) {
        try {
            buffer.append(object.getClass().getCanonicalName()).append("[\n");
            Object value=null;
            for (Field field : object.getClass().getDeclaredFields()) {
                if(fieldSet.add(field.getName())) {
                    field.setAccessible(true);
                    //System.out.println(field.getName());
                    buffer.append(field.getName() + "=");
                    value = field.get(object);
                    if(value!=null) {
                        if(field.getType().isArray() |field.getType().getCanonicalName().startsWith("com.model")) {
                            printObjectStream(buffer,value);
                        }
                        else if (Arrays.asList(spiData).contains(field.getName())) {
                            //field.set(object, replaceDigits((String) field.get(object)));
                            buffer.append(replaceDigits((String) value) );
                        }
                        else {
                            buffer.append( value );
                        }
                    }
                }
                buffer.append("\n");
            }
            buffer.append("]");
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private String replaceDigits(String text) {
        StringBuffer buffer = new StringBuffer(text.length());
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "*");
        }
        return buffer.toString();
    }
*/


}
