package com.riftco.userprofiledataserv.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Filter for capturing HTTP request and response details for logging.
 */
@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String USER_ID = "userId";
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    @Override
    protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request, 
            @org.springframework.lang.NonNull HttpServletResponse response, 
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate unique request ID and add to MDC
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);
        
        // Set request ID in response header for client-side troubleshooting
        response.addHeader("X-Request-ID", requestId);
        
        // Extract user ID from request if available (modify as per your auth mechanism)
        String userId = extractUserId(request);
        if (userId != null) {
            MDC.put(USER_ID, userId);
        }
        
        // Wrap request and response to allow reading the body multiple times
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Log request details before processing
            logRequest(requestWrapper);
            
            // Process the request
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // Log response details after processing
            long duration = System.currentTimeMillis() - startTime;
            logResponse(responseWrapper, duration);
            
            // Copy content to the original response
            responseWrapper.copyBodyToResponse();
            
            // Clear MDC context to prevent memory leaks
            MDC.clear();
        }
    }
    
    private void logRequest(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        String requestURI = request.getRequestURI() + queryString;
        String method = request.getMethod();
        
        log.info("Received HTTP {} request to {}", method, requestURI);
        
        // Log request headers
        if (log.isDebugEnabled()) {
            log.debug("Request headers: {}", LoggingUtils.getHeadersAsMap(request));
        }
        
        // Log request body for specific content types
        if (LoggingUtils.shouldLogRequestBody(request.getContentType())) {
            String requestBody = getRequestPayload(request);
            if (requestBody != null && !requestBody.isEmpty()) {
                log.debug("Request body: {}", LoggingUtils.sanitizePayload(requestBody));
            }
        }
    }
    
    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        int status = response.getStatus();
        log.info("Returned status {} in {} ms", status, duration);
        
        // Log response headers
        if (log.isDebugEnabled()) {
            log.debug("Response headers: {}", LoggingUtils.getHeadersAsMap(response));
        }
        
        // Log response body for specific content types
        if (LoggingUtils.shouldLogResponseBody(response.getContentType())) {
            String responseBody = getResponsePayload(response);
            if (responseBody != null && !responseBody.isEmpty()) {
                log.debug("Response body: {}", LoggingUtils.sanitizePayload(responseBody));
            }
        }
    }
    
    private String getRequestPayload(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return getPayloadAsString(content, request.getCharacterEncoding());
        }
        return null;
    }
    
    private String getResponsePayload(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return getPayloadAsString(content, response.getCharacterEncoding());
        }
        return null;
    }
    
    private String getPayloadAsString(byte[] content, String encoding) {
        try {
            int length = Math.min(content.length, MAX_PAYLOAD_LENGTH);
            return new String(content, 0, length, encoding);
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed to parse payload", e);
            return "[Failed to parse payload]";
        }
    }
    
    private String extractUserId(HttpServletRequest request) {
        // Implement based on your authentication mechanism
        // Example: JWT token extraction, session attribute, etc.
        return null;
    }
}
