package com.example.perpusapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account extends Model<Account>{
    private int user_id;
    private String email;
    private String password;

    public Account(){
        this.table = "account";
        this.primaryKey = "user_id";
    }

    public Account(int user_id, String email, String password){
        this.table = "Account";
        this.primaryKey = "user_id";
        this.user_id = user_id;
        this.email = email;
        this.password = password;
    }

    @Override
    public Account toModel(ResultSet rs){
        try{
            return new Account(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        } catch (SQLException e) {
            return null;
        }
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
