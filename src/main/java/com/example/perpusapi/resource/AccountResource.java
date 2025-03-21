package com.example.perpusapi.resource;

import com.example.perpusapi.middleware.AuthRequired;
import com.example.perpusapi.model.Account;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@AuthRequired
@Path("/accounts")  // Endpoint: /api/accounts
public class AccountResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        Account account = new Account();
        List<Account> accounts = account.get();
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("id") int id) {

        try {
            Account account = new Account();
            String pkValue = String.valueOf(id);;
            Account result = account.find(pkValue);
            if (result != null) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Account not found\"}")
                        .build();
            }
        } catch (SQLException e) {
            System.out.println("🔥 Terjadi error: " + e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
