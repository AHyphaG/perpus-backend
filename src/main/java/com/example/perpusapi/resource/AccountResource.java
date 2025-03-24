package com.example.perpusapi.resource;

import com.example.perpusapi.middleware.AuthRequired;
import com.example.perpusapi.model.Account;
import com.example.perpusapi.model.Member;
import com.example.perpusapi.repository.AccountRepository;
import com.example.perpusapi.repository.MemberRepository;
import com.example.perpusapi.service.AccountService;
import com.example.perpusapi.util.TokenUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@AuthRequired
@Path("/accounts")  // Endpoint: /api/accounts
public class AccountResource {
    private final AccountRepository accountRepo = new AccountRepository();
    private final MemberRepository memberRepo = new MemberRepository();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        AccountService accountService = new AccountService(accountRepo,memberRepo);
        List<Account> accounts = accountService.getAllAccount();
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response getAccountByToken(@Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        AccountService accountService = new AccountService(accountRepo,memberRepo);
        Member result = accountService.getProfileByEmail(TokenUtil.getEmailFromToken(token));
        if (result != null) {
            return Response.ok(result).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Account not found\"}")
                    .build();
        }
    }
}
