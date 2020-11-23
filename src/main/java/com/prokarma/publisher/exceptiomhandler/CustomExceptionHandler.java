package com.prokarma.publisher.exceptiomhandler;



import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.response.ErrorResponse;
import com.prokarma.publisher.response.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        /*ErrorResponse errorDetail = new ErrorResponse(StatusConstants.HttpConstants.CUSTOM_FIELD_VALIDATION.getCode(),
                ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity(new ErrorResponse(errorDetail, null), HttpStatus.BAD_REQUEST);*/

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ValidationError resp = new ValidationError();
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Request fields are invalid");
        resp.setErrorType("MethodArgumentNotValidException");

        for (FieldError fieldError: fieldErrors) {
            String message = fieldError.getDefaultMessage();
            resp.addFieldError(fieldError.getField(), message);
        }

        return new ResponseEntity(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Throwable.class})
    public final ResponseEntity handleExceptions(Throwable ex, WebRequest request) {
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType("");
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Unexpected error occurred."
                +ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity handleUserNotFoundException(NullPointerException ex, WebRequest request) {
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType("NullPointerException");
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
