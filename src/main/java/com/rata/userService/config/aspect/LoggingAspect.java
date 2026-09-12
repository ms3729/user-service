package com.rata.userService.config.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and repository Spring components.
 *
 * @author Ramesh Fadatare
 */
@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {

    private final HttpServletRequest request;

    @Before(value = "execution(* com.rata.userService.services..*(..))")
    public void before1Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    @Before(value = "execution(* com.rata.userService.controllers..*(..))")
    public void before2Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    @Before(value = "execution(* com.rata.userService.errorHandling..*(..))")
    public void before3Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    @After(value = "execution(* com.rata.userService.services..*(..))")
    public void after1Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    @After(value = "execution(* com.rata.userService.controllers..*(..))")
    public void after2Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    @After(value = "execution(* com.rata.userService.errorHandling..*(..))")
    public void after3Advice(JoinPoint joinPoint) {
        logGenerator(joinPoint);
    }

    private void logGenerator(JoinPoint joinPoint) {
        try {
            log.info("{}", new LogRecord(request.getHeader("X-Forwarded-For"), "user-service", joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName(),request.getHeader("user-agent")));
        } catch (Exception e) {
        }
    }

}
