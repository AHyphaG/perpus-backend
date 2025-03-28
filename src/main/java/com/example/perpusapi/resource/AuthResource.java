package com.example.perpusapi.resource;

import com.example.perpusapi.model.Account;
import com.example.perpusapi.repository.AccountRepository;
import com.example.perpusapi.service.AuthService;
import com.example.perpusapi.util.HashUtil;
import com.example.perpusapi.util.TokenUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class AuthResource {

    private final AccountRepository accountRepo = new AccountRepository();

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Map<String, String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");
        AccountRepository accountRepo = new AccountRepository();
        try {
            AuthService auth = new AuthService(accountRepo);
            if (auth.login(email, password)) {
                String token = TokenUtil.generateToken(email);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login berhasil!");
                response.put("token", token);

                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Collections.singletonMap("message", "Kredensial Salah"))
                        .build();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Collections.singletonMap("message", "Terjadi kesalahan pada server"))
                    .build();
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Map<String, String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");
        try {
            AuthService auth = new AuthService(accountRepo);
            Account account = auth.register(email, password).orElse(null);
            if (account != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Registrasi berhasil!");
                response.put("account", account);

                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Collections.singletonMap("message", "Registrasi gagal"))
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Collections.singletonMap("message", e.getMessage()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Collections.singletonMap("message", "Terjadi kesalahan pada server"))
                    .build();
        }
    }
}