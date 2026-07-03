package com.bagaria.ticketapp8.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuditAspect {

    @AfterReturning(value = "execution(* com.bagaria.ticketapp8.service.TicketService.updateStatus(..))")
    public void auditStatusChange(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        int ticketId = (Integer) args[0];

        Object status = args[1];

        log.info("AUDIT -> Ticket {} changed to {}", ticketId, status);
    }
}