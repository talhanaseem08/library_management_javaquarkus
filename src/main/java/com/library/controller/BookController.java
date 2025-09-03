package com.library.controller;

import com.library.dto.BookDTO;
import com.library.service.BookService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {
    
    private static final Logger LOG = Logger.getLogger(BookController.class);
    
    @Inject
    BookService bookService;
    
    @GET
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved all books"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllBooks() {
        LOG.info("GET /books - Retrieving all books");
        List<BookDTO> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved the book"),
        @APIResponse(responseCode = "404", description = "Book not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getBookById(@PathParam("id") String id) {
        LOG.info("GET /books/" + id + " - Retrieving book by ID");
        BookDTO book = bookService.getBookById(id);
        return Response.ok(book).build();
    }
    
    @POST
    @Operation(summary = "Create a new book", description = "Add a new book to the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Book created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid input data"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createBook(@Valid BookDTO bookDTO) {
        LOG.info("POST /books - Creating new book: " + bookDTO);
        BookDTO createdBook = bookService.createBook(bookDTO);
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book's information")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Book updated successfully"),
        @APIResponse(responseCode = "400", description = "Invalid input data"),
        @APIResponse(responseCode = "404", description = "Book not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateBook(@PathParam("id") String id, @Valid BookDTO bookDTO) {
        LOG.info("PUT /books/" + id + " - Updating book");
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Book deleted successfully"),
        @APIResponse(responseCode = "404", description = "Book not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteBook(@PathParam("id") String id) {
        LOG.info("DELETE /books/" + id + " - Deleting book");
        bookService.deleteBook(id);
        return Response.noContent().build();
    }
}
