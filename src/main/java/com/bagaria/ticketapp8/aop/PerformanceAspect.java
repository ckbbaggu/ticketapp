package com.bagaria.ticketapp8.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {
    @Around("execution(* com.bagaria.ticketapp8.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long executionTime = end - start;

        log.info("{} executed in {} ms", joinPoint.getSignature().getName(), executionTime);

        if (executionTime > 1000) {

            log.warn("Slow method detected: {}", joinPoint.getSignature().getName());
        }

        return result;
    }
}
