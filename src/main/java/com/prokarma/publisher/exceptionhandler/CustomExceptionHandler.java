package com.prokarma.publisher.exceptionhandler;



import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.exception.GenericException;
import com.prokarma.publisher.response.ErrorResponse;
import com.prokarma.publisher.response.ValidationError;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity requestValidation(MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage(), ex);

        ValidationError resp = new ValidationError();
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Request fields are invalid");
        resp.setErrorType(ex.getClass().getSimpleName());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError: fieldErrors) {
            String message = fieldError.getDefaultMessage();
            resp.addFieldError(fieldError.getField(), message);
        }

        return new ResponseEntity(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({GenericException.class})
    public final ResponseEntity handleGenericException(GenericException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Unexpected error occurred. "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Throwable.class})
    public final ResponseEntity handleExceptions(Throwable ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Unexpected error occurred. "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getJson(Map<String, List<String>> errorValidation)
    {
        JSONObject json_obj=new JSONObject();
        for (Map.Entry<String, List<String>> entry : errorValidation.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            try {
                json_obj.put(key,value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return json_obj.toString();
    }
}
