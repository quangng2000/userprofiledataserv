package com.riftco.userprofiledataserv.config.telemetry;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect that creates spans for methods annotated with @Traced.
 * This allows for custom business operation tracing.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TracingAspect {

    private final Tracer tracer;

    /**
     * Creates a span for methods annotated with @Observed.
     * 
     * @param joinPoint The join point for the intercepted method
     * @return The result of the method execution
     * @throws Throwable If the method throws an exception
     */
    @Around("@annotation(io.micrometer.observation.annotation.Observed)")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        
        Observed observed = signature.getMethod().getAnnotation(Observed.class);
        String spanName = observed.name().isEmpty() 
                ? className + "." + methodName 
                : observed.name();
        
        Span span = tracer.nextSpan().name(spanName);
        
        // Add common attributes to the span
        span.tag("class", className);
        span.tag("method", methodName);
        
        // Add method parameters as tags if they are simple types
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && isSimpleType(args[i].getClass())) {
                span.tag(paramNames[i], args[i].toString());
            }
        }
        
        try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {
            log.debug("Starting span for {}.{}", className, methodName);
            Object result = joinPoint.proceed();
            log.debug("Completed span for {}.{}", className, methodName);
            return result;
        } catch (Exception e) {
            span.tag("error", e.getClass().getSimpleName());
            span.tag("error.message", e.getMessage());
            log.error("Error in span for {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
    
    /**
     * Checks if a class is a simple type that can be safely converted to a string.
     * 
     * @param clazz The class to check
     * @return true if the class is a simple type
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() 
            || clazz == String.class
            || Number.class.isAssignableFrom(clazz)
            || Boolean.class == clazz
            || Character.class == clazz;
    }
}
