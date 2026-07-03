package com.bagaria.ticketapp8.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.bagaria.ticketapp8.controller.*.*(..))")
    public void logControllerRequest(JoinPoint joinPoint) {

        log.info("Controller Method: {}", joinPoint.getSignature().getName());
        log.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(pointcut = "execution(* com.bagaria.ticketapp8..*(..))", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Exception exception) {
        log.error("Exception in {} : {}", joinPoint.getSignature().getName(), exception.getMessage(), exception);
    }
}