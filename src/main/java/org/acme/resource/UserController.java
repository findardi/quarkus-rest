package org.acme.resource;

import java.util.List;

import org.acme.dto.ApiResponse;
import org.acme.dto.UserLogin;
import org.acme.dto.UserRegister;
import org.acme.service.UserService;
import org.acme.service.UserService.login;
import org.acme.service.UserService.profile;
import org.acme.service.UserService.registerResponse;
import org.acme.service.UserService.userList;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/user")
public class UserController {

    @Inject
    UserService us;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserRegister req) {
        registerResponse result = us.insert(req);
        ApiResponse<registerResponse> response = ApiResponse.created("User registered successfully", result);
        return Response.status(201).entity(response).build();
    }

    @GET
    @Path("/all")
    @RolesAllowed({"ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<userList> result = us.getAll();
        ApiResponse<List<userList>> response = ApiResponse.ok("get users successfully", result);
        return Response.status(200).entity(response).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid UserLogin req) {
        login result = us.loginUser(req);
        ApiResponse<login> response = ApiResponse.ok("login successfully", result);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/profile")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@Context SecurityContext ctx) {
        // Get username from SecurityContext
        String username = ctx.getUserPrincipal().getName();

        profile profile = us.profile(username);
        ApiResponse<profile> response = ApiResponse.ok("get profile successfully", profile);
        return Response.status(200).entity(response).build();
    }

    public record StatusResponse(String status) {}
}
