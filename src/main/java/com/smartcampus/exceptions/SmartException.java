package com.smartcampus.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

public class SmartException extends WebApplicationException {
    
    public SmartException(String message, int status) {
        // This tells Jersey exactly what status code and JSON message to send back
        super(Response.status(status)
                .entity("{\"error\": \"" + message + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}