package com.riftco.userprofiledataserv.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * Utility class for common logging functionality.
 */
public class LoggingUtils {

    private static final Set<String> LOGGABLE_CONTENT_TYPES = Set.of(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.TEXT_PLAIN_VALUE
    );
    
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password", "passwordConfirm", "secret", "token", "apiKey", "creditCard", 
            "ssn", "socialSecurityNumber", "accountNumber", "accessToken", "refreshToken"
    );
    
    /**
     * Extract headers from HttpServletRequest as a Map.
     */
    public static Map<String, String> getHeadersAsMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = sanitizeHeaderValue(name, request.getHeader(name));
                headers.put(name, value);
            }
        }
        
        return headers;
    }
    
    /**
     * Extract headers from HttpServletResponse as a Map.
     */
    public static Map<String, String> getHeadersAsMap(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        
        if (headerNames != null) {
            for (String name : headerNames) {
                String value = sanitizeHeaderValue(name, response.getHeader(name));
                headers.put(name, value);
            }
        }
        
        return headers;
    }
    
    /**
     * Sanitize header value based on header name to avoid logging sensitive information.
     */
    private static String sanitizeHeaderValue(String name, String value) {
        String lowerName = name.toLowerCase();
        if (lowerName.contains("authorization") || 
            lowerName.contains("cookie") || 
            lowerName.contains("token") || 
            lowerName.contains("secret") || 
            lowerName.contains("password") || 
            lowerName.contains("key")) {
            return "[REDACTED]";
        }
        return value;
    }
    
    /**
     * Determine if the request body should be logged based on content type.
     */
    public static boolean shouldLogRequestBody(String contentType) {
        return contentType != null && LOGGABLE_CONTENT_TYPES.stream()
                .anyMatch(contentType::contains);
    }
    
    /**
     * Determine if the response body should be logged based on content type.
     */
    public static boolean shouldLogResponseBody(String contentType) {
        return contentType != null && LOGGABLE_CONTENT_TYPES.stream()
                .anyMatch(contentType::contains);
    }
    
    /**
     * Sanitize payload to avoid logging sensitive data.
     * This is a simple implementation. Consider using a JSON parser for more advanced redaction.
     */
    public static String sanitizePayload(String payload) {
        if (payload == null) {
            return null;
        }
        
        String result = payload;
        for (String field : SENSITIVE_FIELDS) {
            String pattern = String.format("\"%s\"\\s*:\\s*\"[^\"]*\"", field);
            result = result.replaceAll(pattern, "\"" + field + "\": \"[REDACTED]\"");
        }
        
        // If payload is too large, truncate it
        if (result.length() > 1000) {
            result = result.substring(0, 997) + "...";
        }
        
        return result;
    }
}
