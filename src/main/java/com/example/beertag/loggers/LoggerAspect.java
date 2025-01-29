package com.example.beertag.loggers;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    // Define a pointcut to target all methods in the UserService class
    @Pointcut("execution(* com.example.beertag.services.UserServiceImpl.*(..))")
    public void userServiceMethods() {}

    // This advice will be executed before any method in the UserService class
    @Before("userServiceMethods()")
    public void logBefore() {
        logger.info("Logging before executing method...");
    }
}

