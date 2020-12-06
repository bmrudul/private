package com.prokarma.publisher.exceptionhandler;


import com.prokarma.publisher.constants.PublisherConstant;
import com.prokarma.publisher.exception.GenericException;
import com.prokarma.publisher.response.ErrorResponse;
import com.prokarma.publisher.response.ValidationError;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity requestValidation(MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage(), ex);

        ValidationError response = new ValidationError();
        response.setStatus(PublisherConstant.ERROR.getValue());
        response.setMessage("Request fields are invalid");
        response.setErrorType(ex.getClass().getSimpleName());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError: fieldErrors) {
            String message = fieldError.getDefaultMessage();
            response.addFieldError(fieldError.getField(), message);
        }

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({GenericException.class})
    public ResponseEntity handleGenericException(GenericException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ex.getClass().getSimpleName());
        response.setStatus(PublisherConstant.ERROR.getValue());
        response.setMessage("Unexpected error occurred. "+ex.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoMethodException(HttpServletRequest request, NoHandlerFoundException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ex.getClass().getSimpleName());
        response.setStatus(PublisherConstant.ERROR.getValue());
        response.setMessage("resource not found "+ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(HttpServletRequest request, AuthenticationException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ex.getClass().getSimpleName());
        response.setStatus(PublisherConstant.ERROR.getValue());
        response.setMessage("Authorization required. "+ex.getMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(HttpServletRequest request, ServletRequestBindingException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ex.getClass().getSimpleName());
        response.setStatus(PublisherConstant.ERROR.getValue());
        response.setMessage("Request headers are missing "+ex.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
