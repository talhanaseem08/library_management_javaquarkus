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
        LOG.info("GET / - Welcome page accessed");
        
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
                        padding: 20px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        color: white;
                    }
                    .container {
                        max-width: 1200px;
                        margin: 0 auto;
                        background: rgba(255, 255, 255, 0.1);
                        border-radius: 15px;
                        padding: 30px;
                        backdrop-filter: blur(10px);
                        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
                    }
                    h1 {
                        text-align: center;
                        font-size: 2.5em;
                        margin-bottom: 10px;
                        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                    }
                    .subtitle {
                        text-align: center;
                        font-size: 1.2em;
                        margin-bottom: 40px;
                        opacity: 0.9;
                    }
                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 20px;
                        margin-bottom: 30px;
                    }
                    .card {
                        background: rgba(255, 255, 255, 0.15);
                        border-radius: 10px;
                        padding: 25px;
                        text-align: center;
                        transition: transform 0.3s ease, box-shadow 0.3s ease;
                        border: 1px solid rgba(255, 255, 255, 0.2);
                    }
                    .card:hover {
                        transform: translateY(-5px);
                        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
                    }
                    .card h3 {
                        margin-top: 0;
                        color: #fff;
                        font-size: 1.4em;
                    }
                    .card p {
                        margin-bottom: 20px;
                        opacity: 0.9;
                        line-height: 1.6;
                    }
                    .btn {
                        display: inline-block;
                        padding: 12px 24px;
                        background: linear-gradient(45deg, #ff6b6b, #ee5a24);
                        color: white;
                        text-decoration: none;
                        border-radius: 25px;
                        font-weight: bold;
                        transition: all 0.3s ease;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                    }
                    .btn:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
                        background: linear-gradient(45deg, #ee5a24, #ff6b6b);
                    }
                    .btn-secondary {
                        background: linear-gradient(45deg, #4834d4, #686de0);
                    }
                    .btn-secondary:hover {
                        background: linear-gradient(45deg, #686de0, #4834d4);
                    }
                    .btn-success {
                        background: linear-gradient(45deg, #00b894, #00cec9);
                    }
                    .btn-success:hover {
                        background: linear-gradient(45deg, #00cec9, #00b894);
                    }
                    .footer {
                        text-align: center;
                        margin-top: 40px;
                        padding-top: 20px;
                        border-top: 1px solid rgba(255, 255, 255, 0.2);
                        opacity: 0.8;
                    }
                    .status {
                        background: rgba(0, 255, 0, 0.2);
                        border: 1px solid rgba(0, 255, 0, 0.3);
                        border-radius: 5px;
                        padding: 10px;
                        text-align: center;
                        margin-bottom: 20px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>📚 Library Management System</h1>
                    <p class="subtitle">A modern RESTful API built with Quarkus and Java</p>
                    
                    <div class="status">
                        ✅ System Status: <strong>Online</strong> | 🚀 Ready to manage your library!
                    </div>
                    
                    <div class="grid">
                        <div class="card">
                            <h3>📖 Book Management</h3>
                            <p>Add, view, update, and delete books in your library collection.</p>
                            <a href="/books" class="btn">Manage Books</a>
                        </div>
                        
                        <div class="card">
                            <h3>👥 Member Management</h3>
                            <p>Register and manage library members and their information.</p>
                            <a href="/members" class="btn btn-secondary">Manage Members</a>
                        </div>
                        
                        <div class="card">
                            <h3>🔄 Lending Operations</h3>
                            <p>Lend books to members, track returns, and view lending history.</p>
                            <a href="/lending/history" class="btn btn-success">View Lending History</a>
                        </div>
                    </div>
                    
                    <div class="grid">
                        <div class="card">
                            <h3>📋 API Documentation</h3>
                            <p>Interactive API documentation with Swagger UI for testing endpoints.</p>
                            <a href="/q/swagger-ui" class="btn btn-secondary">Open Swagger UI</a>
                        </div>
                        
                        <div class="card">
                            <h3>🔧 Development Tools</h3>
                            <p>Access development tools and monitoring features.</p>
                            <a href="/q/dev" class="btn btn-success">Dev UI</a>
                        </div>
                        
                        <div class="card">
                            <h3>📄 OpenAPI Spec</h3>
                            <p>View the complete OpenAPI specification for the API.</p>
                            <a href="/q/openapi" class="btn">View API Spec</a>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p>Built with ❤️ using Quarkus, Java 21, and modern web technologies</p>
                        <p>Version 1.0.0 | Library Management System</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
    
    @GET
    @Path("/api")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> apiInfo() {
        LOG.info("GET /api - API info accessed");
        
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Library Management System");
        response.put("version", "1.0.0");
        response.put("status", "online");
        response.put("description", "A modern RESTful API for library management");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("books", "/books");
        endpoints.put("members", "/members");
        endpoints.put("lending", "/lending");
        endpoints.put("swagger", "/q/swagger-ui");
        endpoints.put("openapi", "/q/openapi");
        endpoints.put("dev", "/q/dev");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
    
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> health() {
        LOG.info("GET /health - Health check accessed");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service", "Library Management System");
        response.put("version", "1.0.0");
        
        return response;
    }
}
