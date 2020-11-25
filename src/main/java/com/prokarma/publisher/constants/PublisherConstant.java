package com.prokarma.publisher.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PublisherConstant {

    SUCCESS("Success"),

    ERROR("Error");



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
