package com.library.controller;

import com.library.dto.LendingDTO;
import com.library.service.LendingService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/lending")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Lending Management", description = "APIs for managing book lending operations")
public class LendingController {
    
    private static final Logger LOG = Logger.getLogger(LendingController.class);
    
    @Inject
    LendingService lendingService;
    
    @POST
    @Operation(summary = "Lend a book", description = "Lend a book to a member (only if book is available)")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Book lent successfully"),
        @APIResponse(responseCode = "400", description = "Book not available or invalid request"),
        @APIResponse(responseCode = "404", description = "Book or member not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response lendBook(@QueryParam("bookId") String bookId, @QueryParam("memberId") String memberId) {
        LOG.info("POST /lending - Lending book. Book ID: " + bookId + ", Member ID: " + memberId);
        LendingDTO lending = lendingService.lendBook(bookId, memberId);
        return Response.status(Response.Status.CREATED).entity(lending).build();
    }
    
    @POST
    @Path("/returns/{id}")
    @Operation(summary = "Return a book", description = "Mark a book as returned and make it available again")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Book returned successfully"),
        @APIResponse(responseCode = "404", description = "Lending record not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response returnBook(@PathParam("id") String lendingId) {
        LOG.info("POST /lending/returns/" + lendingId + " - Returning book");
        LendingDTO lending = lendingService.returnBook(lendingId);
        return Response.ok(lending).build();
    }
    
    @GET
    @Path("/history")
    @Operation(summary = "Get lending history", description = "Show all lending books data with member information")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved lending history"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getLendingHistory() {
        LOG.info("GET /lending/history - Retrieving lending history");
        List<LendingDTO> lendings = lendingService.getLendingHistory();
        return Response.ok(lendings).build();
    }
}
