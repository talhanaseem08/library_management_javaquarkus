package com.library.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Path("/")
public class WelcomeController {
    
    private static final Logger LOG = Logger.getLogger(WelcomeController.class);

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String welcome() {
        LOG.info("Welcome page accessed");
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Library Management System</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        margin: 0;
                        padding: 0;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                    }
                    .container {
                        background: white;
                        padding: 3rem;
                        border-radius: 20px;
                        box-shadow: 0 20px 40px rgba(0,0,0,0.1);
                        text-align: center;
                        max-width: 600px;
                        margin: 2rem;
                    }
                    h1 {
                        color: #333;
                        margin-bottom: 1rem;
                        font-size: 2.5rem;
                    }
                    .subtitle {
                        color: #666;
                        margin-bottom: 2rem;
                        font-size: 1.2rem;
                    }
                    .links {
                        display: flex;
                        gap: 1rem;
                        justify-content: center;
                        flex-wrap: wrap;
                    }
                    .btn {
                        display: inline-block;
                        padding: 1rem 2rem;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        text-decoration: none;
                        border-radius: 10px;
                        font-weight: 600;
                        transition: transform 0.3s ease;
                    }
                    .btn:hover {
                        transform: translateY(-3px);
                    }
                    .btn-secondary {
                        background: #e2e8f0;
                        color: #475569;
                    }
                    .btn-secondary:hover {
                        background: #cbd5e1;
                    }
                    .api-info {
                        margin-top: 2rem;
                        padding: 1.5rem;
                        background: #f8fafc;
                        border-radius: 10px;
                        text-align: left;
                    }
                    .api-info h3 {
                        color: #333;
                        margin-bottom: 1rem;
                    }
                    .endpoint {
                        margin: 0.5rem 0;
                        font-family: monospace;
                        background: #e2e8f0;
                        padding: 0.5rem;
                        border-radius: 5px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>📚 Library Management System</h1>
                    <p class="subtitle">Welcome to your Library Management System API</p>
                    
                    <div class="links">
                        <a href="http://localhost:8000" class="btn">🎨 Open Frontend</a>
                        <a href="/swagger-ui" class="btn btn-secondary">📖 API Documentation</a>
                    </div>
                    
                    <div class="api-info">
                        <h3>🚀 Available API Endpoints:</h3>
                        <div class="endpoint">GET /api/books - Get all books (20 books loaded!)</div>
                        <div class="endpoint">POST /api/books - Add new book</div>
                        <div class="endpoint">GET /api/members - Get all members</div>
                        <div class="endpoint">POST /api/members - Add new member</div>
                        <div class="endpoint">POST /api/lending - Lend a book</div>
                        <div class="endpoint">GET /api/lending/history - Get lending history</div>
                        <div class="endpoint">POST /api/lending/returns/{id} - Return a book</div>
                    </div>
                    
                    <p style="margin-top: 2rem; color: #666;">
                        <strong>💡 Tip:</strong> Use the Frontend link above for the full web interface!
                    </p>
                </div>
            </body>
            </html>
            """;
    }

    @GET
    @Path("/api")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> apiInfo() {
        LOG.info("API info endpoint accessed");
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Library Management System API");
        info.put("version", "1.0.0");
        info.put("description", "RESTful API for managing library books, members, and lending");
        info.put("frontend", "http://localhost:8000");
        info.put("documentation", "/swagger-ui");
        info.put("books_loaded", "20 books pre-loaded");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("books", "/api/books");
        endpoints.put("members", "/api/members");
        endpoints.put("lending", "/api/lending");
        endpoints.put("lending_history", "/api/lending/history");
        info.put("endpoints", endpoints);
        
        return info;
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> health() {
        LOG.info("Health check endpoint accessed");
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "Library Management System");
        health.put("books_loaded", "20 books available");
        return health;
    }
}
