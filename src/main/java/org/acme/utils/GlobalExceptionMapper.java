package org.acme.utils;

import org.acme.exception.UnauthorizedException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


public class GlobalExceptionMapper {
    
    public record ErrorResponse(
        int status,
        String message,
        OffsetDateTime timestamp
    ) {}
    
    public record ValidationErrorResponse(
        int status,
        String message,
        Map<String, String> errors,
        OffsetDateTime timestamp
    ) {}

    private static OffsetDateTime time() {
        return OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Provider
    public static class ValidationExceptionMapper 
        implements ExceptionMapper<ConstraintViolationException> {
        
        @Override
        public Response toResponse(ConstraintViolationException e) {
            Map<String, String> errors = new HashMap<>();
            
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                String field = violation.getPropertyPath().toString();
                String fieldName = field.substring(field.lastIndexOf('.') + 1);
                String message = violation.getMessage();
                errors.put(fieldName, message);
            }
            
            ValidationErrorResponse error = new ValidationErrorResponse(
                400,
                "Validation failed",
                errors,
                time()
            );
            
            return Response.status(400).entity(error).build();
        }
    }
    
    @Provider
    public static class UnauthorizedExceptionMapper 
        implements ExceptionMapper<UnauthorizedException> {
        
        @Override
        public Response toResponse(UnauthorizedException e) {
            ErrorResponse error = new ErrorResponse(
                401,
                e.getMessage(),
                time()
            );
            
            return Response.status(401).entity(error).build();
        }
    }
    
    @Provider
    public static class GeneralExceptionMapper 
        implements ExceptionMapper<Exception> {
        
        @Override
        public Response toResponse(Exception e) {
            if (e instanceof ConstraintViolationException) {
                return null; 
            }
            
            ErrorResponse error = new ErrorResponse(
                500,
                e.getMessage(),
                time()
            );
            
            return Response.status(500).entity(error).build();
        }
    }
}
