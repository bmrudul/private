package com.prokarma.publisher.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PublisherConstant {

    SUCCESS("Success"),

    ERROR("Error"),

    CUSTOMER_NUMBER_MASK("(\\w{0,4})$"),

    DOB_MASK("^([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))"),

    EMAIL_MASK("^(\\w{0,4})");

    private final String value;

    PublisherConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PublisherConstant fromValue(String text) {
        for (PublisherConstant b : PublisherConstant.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
