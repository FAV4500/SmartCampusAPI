package com.smartcampus.config;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {
    // This is the address where your API will live
    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        // This tells the server to look in your resources package for your code
        final ResourceConfig rc = new ResourceConfig().packages("com.smartcampus.resources", "com.smartcampus.exceptions");
        
        // This allows the server to understand JSON data
        rc.register(org.glassfish.jersey.jackson.JacksonFeature.class);

        System.out.println("Starting Server...");
        
        try {
            // This actually starts the server
            GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
            System.out.println("🚀 SUCCESS! API is live at: " + BASE_URI + "rooms");
            
            // Keep the program running
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}