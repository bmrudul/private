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
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity requestValidation(MethodArgumentNotValidException ex) {
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
    public ResponseEntity handleGenericException(GenericException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Unexpected error occurred. "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoMethodException(HttpServletRequest request, NoHandlerFoundException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("resource not found "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(HttpServletRequest request, AuthenticationException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("resource not found "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(HttpServletRequest request, ServletRequestBindingException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorType(ex.getClass().getSimpleName());
        resp.setStatus(PublisherConstant.ERROR.getValue());
        resp.setMessage("Request headers are missing "+ex.getMessage());
        return new ResponseEntity(resp, HttpStatus.BAD_REQUEST);
    }
}
