package com.library.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<RuntimeException> {
    
    private static final Logger LOG = Logger.getLogger(CustomExceptionHandler.class);
    
    @Override
    public Response toResponse(RuntimeException exception) {
        // Only handle our custom exceptions and business logic exceptions
        if (exception instanceof BookNotFoundException ||
            exception instanceof MemberNotFoundException ||
            exception instanceof BookNotAvailableException ||
            exception instanceof LendingNotFoundException ||
            exception.getMessage().contains("already exists")) {
            
            LOG.error("Custom exception handler caught: " + exception.getMessage(), exception);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", exception.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            errorResponse.put("type", exception.getClass().getSimpleName());
            
            if (exception instanceof BookNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
            } else if (exception instanceof MemberNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
            } else if (exception instanceof BookNotAvailableException) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
            } else if (exception instanceof LendingNotFoundException) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
            } else if (exception.getMessage().contains("already exists")) {
                // Handle duplicate book/member exceptions
                return Response.status(Response.Status.CONFLICT)
                        .entity(errorResponse)
                        .build();
            }
        }
        
        // Let other exceptions pass through to default handlers
        return null;
    }
}
