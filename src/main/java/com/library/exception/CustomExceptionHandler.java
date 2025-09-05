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
        LOG.error("Custom exception handler caught: " + exception.getMessage(), exception);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("type", "BusinessException");
        
        // Determine HTTP status based on error message content
        String message = exception.getMessage().toLowerCase();
        
        if (message.contains("not found")) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .build();
        } else if (message.contains("not available") || message.contains("already lent")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } else if (message.contains("already exists") || message.contains("duplicate")) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorResponse)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        }
    }
}
