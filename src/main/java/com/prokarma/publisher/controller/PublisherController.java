package com.prokarma.publisher.controller;

import com.prokarma.publisher.exception.GenericException;
import com.prokarma.publisher.model.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/test")
public class PublisherController {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("security")
    public String checkSecurity(){
        logger.info("test");
        logger.debug("test");
        logger.error("test");
        String s = null;
        if(s == null)
            throw new GenericException("String is null");
        s.trim();
        return "success";
    }


    @PostMapping("test")
    public String checkSecurity(@RequestBody @Valid Test test){
        return "success";
    }

}
