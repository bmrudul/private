package com.prokarma.publisher.response;

public class FieldError
{
	private final String field;

    private final String message;

    FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
