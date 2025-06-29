package com.riftco.userprofiledataserv.config.logging;

// AspectJ imports
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Aspect for logging execution of service and repository Spring components.
 * Provides detailed method entry/exit logging and performance metrics.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut for all Spring components in the application.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)" +
            " || within(@org.springframework.stereotype.Component *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for all components in application packages.
     */
    @Pointcut("within(com.riftco.userprofiledataserv.adapter..*)" +
            " || within(com.riftco.userprofiledataserv.application..*)" +
            " || within(com.riftco.userprofiledataserv.domain..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger log = getLogger(joinPoint);
        
        log.error(
            "Exception in {}() with cause = '{}' and exception = '{}'",
            joinPoint.getSignature().getName(),
            e.getCause() != null ? e.getCause() : "NULL",
            e.getMessage(),
            e
        );
    }

    /**
     * Advice that logs when a method is entered and exited.
     * Also logs method execution time.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws Exception
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = getLogger(joinPoint);
        
        // Get method signature and parameters
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        
        // Log method entry
        if (log.isDebugEnabled()) {
            Object[] args = joinPoint.getArgs();
            String argsStr = LoggingUtils.sanitizePayload(Arrays.toString(args));
            log.debug("Enter: {}#{} with argument[s] = {}", className, methodName, argsStr);
        } else if (log.isInfoEnabled()) {
            log.info("Enter: {}#{}", className, methodName);
        }

        // Create stopwatch for timing
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            // Execute the method
            Object result = joinPoint.proceed();
            
            // Log method exit and result
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                String resultStr = result != null 
                    ? LoggingUtils.sanitizePayload(result.toString()) 
                    : "void/null";
                log.debug("Exit: {}#{} with result = {} in {} ms", 
                    className, methodName, resultStr, stopWatch.getTotalTimeMillis());
            } else if (log.isInfoEnabled()) {
                log.info("Exit: {}#{} in {} ms", 
                    className, methodName, stopWatch.getTotalTimeMillis());
            }
            
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("Exception in {}#{} after {} ms", 
                className, methodName, stopWatch.getTotalTimeMillis());
            throw e;
        }
    }

    /**
     * Get the appropriate logger for the given join point.
     */
    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }
}
