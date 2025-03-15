package com.example.perpusapi.resource;

import com.example.perpusapi.model.Account;
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
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Map<String, String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            Account query = new Account();
            query.where("email", email);
            query.where("password", HashUtil.sha256(password));
            ArrayList<Account> users = query.get();

            if (!users.isEmpty()) {

                Account user = users.get(0);

                String token = TokenUtil.generateToken(email);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login berhasil!");
                response.put("id",user.getUser_id());
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
}