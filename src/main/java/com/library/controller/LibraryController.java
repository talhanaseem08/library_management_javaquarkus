package com.library.controller;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import com.library.dto.LendingRequestDTO;
import com.library.dto.LendingResponseDTO;
import com.library.service.BookService;
import com.library.service.MemberService;
import com.library.service.LendingService;
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

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Library Management System", description = "Complete Library Management API")
public class LibraryController {
    
    private static final Logger LOG = Logger.getLogger(LibraryController.class);
    
    @Inject
    BookService bookService;
    
    @Inject
    MemberService memberService;
    
    @Inject
    LendingService lendingService;
    
    // ==========================================
    // BOOK MANAGEMENT ENDPOINTS
    // ==========================================
    
    @GET
    @Path("/books")
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved all books")
    })
    public Response getAllBooks() {
        LOG.info("Retrieving all books");
        List<BookResponseDTO> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/books/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Book found successfully"),
        @APIResponse(responseCode = "404", description = "Book not found")
    })
    public Response getBookById(@PathParam("id") String id) {
        LOG.info("Retrieving book with ID: " + id);
        BookResponseDTO book = bookService.getBookById(id);
        return Response.ok(book).build();
    }
    
    @POST
    @Path("/books")
    @Operation(summary = "Add new book", description = "Add a new book to the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Book created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid book data")
    })
    public Response createBook(@Valid BookRequestDTO bookRequestDTO) {
        LOG.info("Creating new book: " + bookRequestDTO);
        BookResponseDTO book = bookService.createBook(bookRequestDTO);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }
    
    @PUT
    @Path("/books/{id}")
    @Operation(summary = "Update book", description = "Update an existing book")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Book updated successfully"),
        @APIResponse(responseCode = "404", description = "Book not found")
    })
    public Response updateBook(@PathParam("id") String id, @Valid BookRequestDTO bookRequestDTO) {
        LOG.info("Updating book with ID: " + id);
        BookResponseDTO book = bookService.updateBook(id, bookRequestDTO);
        return Response.ok(book).build();
    }
    
    @DELETE
    @Path("/books/{id}")
    @Operation(summary = "Delete book", description = "Delete a book from the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Book deleted successfully"),
        @APIResponse(responseCode = "404", description = "Book not found")
    })
    public Response deleteBook(@PathParam("id") String id) {
        LOG.info("Deleting book with ID: " + id);
        bookService.deleteBook(id);
        return Response.noContent().build();
    }
    
    // ==========================================
    // MEMBER MANAGEMENT ENDPOINTS
    // ==========================================
    
    @GET
    @Path("/members")
    @Operation(summary = "Get all members", description = "Retrieve a list of all library members")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved all members")
    })
    public Response getAllMembers() {
        LOG.info("Retrieving all members");
        List<MemberResponseDTO> members = memberService.getAllMembers();
        return Response.ok(members).build();
    }
    
    @GET
    @Path("/members/{id}")
    @Operation(summary = "Get member by ID", description = "Retrieve a specific member by their ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Member found successfully"),
        @APIResponse(responseCode = "404", description = "Member not found")
    })
    public Response getMemberById(@PathParam("id") String id) {
        LOG.info("Retrieving member with ID: " + id);
        MemberResponseDTO member = memberService.getMemberById(id);
        return Response.ok(member).build();
    }
    
    @POST
    @Path("/members")
    @Operation(summary = "Add new member", description = "Register a new library member")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Member created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid member data"),
        @APIResponse(responseCode = "409", description = "Member already exists")
    })
    public Response createMember(@Valid MemberRequestDTO memberRequestDTO) {
        LOG.info("Creating new member: " + memberRequestDTO);
        MemberResponseDTO member = memberService.createMember(memberRequestDTO);
        return Response.status(Response.Status.CREATED).entity(member).build();
    }
    
    @PUT
    @Path("/members/{id}")
    @Operation(summary = "Update member", description = "Update an existing member's information")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Member updated successfully"),
        @APIResponse(responseCode = "404", description = "Member not found"),
        @APIResponse(responseCode = "409", description = "Email already exists")
    })
    public Response updateMember(@PathParam("id") String id, @Valid MemberRequestDTO memberRequestDTO) {
        LOG.info("Updating member with ID: " + id);
        MemberResponseDTO member = memberService.updateMember(id, memberRequestDTO);
        return Response.ok(member).build();
    }
    
    @DELETE
    @Path("/members/{id}")
    @Operation(summary = "Delete member", description = "Remove a member from the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Member deleted successfully"),
        @APIResponse(responseCode = "404", description = "Member not found")
    })
    public Response deleteMember(@PathParam("id") String id) {
        LOG.info("Deleting member with ID: " + id);
        memberService.deleteMember(id);
        return Response.noContent().build();
    }
    
    // ==========================================
    // LENDING MANAGEMENT ENDPOINTS
    // ==========================================
    
    @GET
    @Path("/lending")
    @Operation(summary = "Get all lendings", description = "Retrieve a list of all book lendings")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved all lendings")
    })
    public Response getAllLendings() {
        LOG.info("Retrieving all lendings");
        List<LendingResponseDTO> lendings = lendingService.getAllLendings();
        return Response.ok(lendings).build();
    }
    
    @GET
    @Path("/lending/{id}")
    @Operation(summary = "Get lending by ID", description = "Retrieve a specific lending by its ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lending found successfully"),
        @APIResponse(responseCode = "404", description = "Lending not found")
    })
    public Response getLendingById(@PathParam("id") String id) {
        LOG.info("Retrieving lending with ID: " + id);
        LendingResponseDTO lending = lendingService.getLendingById(id);
        return Response.ok(lending).build();
    }
    
    @POST
    @Path("/lending")
    @Operation(summary = "Lend a book", description = "Lend a book to a member")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Book lent successfully"),
        @APIResponse(responseCode = "400", description = "Invalid lending data or book not available"),
        @APIResponse(responseCode = "404", description = "Book or member not found")
    })
    public Response lendBook(@Valid LendingRequestDTO lendingRequestDTO) {
        LOG.info("Lending book. Book ID: " + lendingRequestDTO.getBookId() + 
                ", Member ID: " + lendingRequestDTO.getMemberId());
        LendingResponseDTO lending = lendingService.lendBook(lendingRequestDTO);
        return Response.status(Response.Status.CREATED).entity(lending).build();
    }
    
    @POST
    @Path("/lending/returns/{id}")
    @Operation(summary = "Return a book", description = "Return a lent book")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Book returned successfully"),
        @APIResponse(responseCode = "404", description = "Lending not found")
    })
    public Response returnBook(@PathParam("id") String id) {
        LOG.info("Returning book for lending ID: " + id);
        lendingService.returnBook(id);
        return Response.ok().entity("{\"message\": \"Book returned successfully\"}").build();
    }
    
    @GET
    @Path("/lending/history")
    @Operation(summary = "Get lending history", description = "Retrieve the complete lending history")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved lending history")
    })
    public Response getLendingHistory() {
        LOG.info("Retrieving lending history");
        List<LendingResponseDTO> history = lendingService.getLendingHistory();
        return Response.ok(history).build();
    }
}
