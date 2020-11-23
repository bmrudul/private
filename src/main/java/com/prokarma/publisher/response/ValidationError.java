package com.prokarma.publisher.response;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationError extends ErrorResponse {
	private final List<FieldError> fieldErrors = new ArrayList<FieldError>();

	@Override
	public String toString() {
		return "ValidationErrorDTO [fieldErrors=" + fieldErrors+"]";
	}

	public ValidationError() {

	}

	public void addFieldError(String path, String message) {
		FieldError error = new FieldError(path, message);
		fieldErrors.add(error);
	}

	public List<FieldError> getFieldErrors() {
		return Collections.unmodifiableList(fieldErrors);
	}

}
