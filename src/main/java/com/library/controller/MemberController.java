package com.library.controller;

import com.library.dto.MemberDTO;
import com.library.service.MemberService;
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

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Member Management", description = "APIs for managing library members")
public class MemberController {
    
    private static final Logger LOG = Logger.getLogger(MemberController.class);
    
    @Inject
    MemberService memberService;
    
    @GET
    @Operation(summary = "Get all members", description = "Retrieve a list of all library members")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved all members"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllMembers() {
        LOG.info("GET /members - Retrieving all members");
        List<MemberDTO> members = memberService.getAllMembers();
        return Response.ok(members).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get member by ID", description = "Retrieve a specific member by their ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully retrieved the member"),
        @APIResponse(responseCode = "404", description = "Member not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getMemberById(@PathParam("id") String id) {
        LOG.info("GET /members/" + id + " - Retrieving member by ID");
        MemberDTO member = memberService.getMemberById(id);
        return Response.ok(member).build();
    }
    
    @POST
    @Operation(summary = "Create a new member", description = "Register a new library member")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Member created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid input data"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createMember(@Valid MemberDTO memberDTO) {
        LOG.info("POST /members - Creating new member: " + memberDTO);
        MemberDTO createdMember = memberService.createMember(memberDTO);
        return Response.status(Response.Status.CREATED).entity(createdMember).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a member", description = "Update an existing member's information")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Member updated successfully"),
        @APIResponse(responseCode = "400", description = "Invalid input data"),
        @APIResponse(responseCode = "404", description = "Member not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateMember(@PathParam("id") String id, @Valid MemberDTO memberDTO) {
        LOG.info("PUT /members/" + id + " - Updating member");
        MemberDTO updatedMember = memberService.updateMember(id, memberDTO);
        return Response.ok(updatedMember).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a member", description = "Remove a member from the library")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Member deleted successfully"),
        @APIResponse(responseCode = "404", description = "Member not found"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteMember(@PathParam("id") String id) {
        LOG.info("DELETE /members/" + id + " - Deleting member");
        memberService.deleteMember(id);
        return Response.noContent().build();
    }
}
