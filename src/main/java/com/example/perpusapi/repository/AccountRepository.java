package com.example.perpusapi.repository;

import com.example.perpusapi.config.DatabaseConfig;
import com.example.perpusapi.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class AccountRepository {
    public Optional<Account> findById(int id) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Account account = new Account(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                return Optional.of(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Account> findByEmail(String email) {
        String sql = "SELECT * FROM account WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Account account = new Account(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                return Optional.of(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(Account account) {
        String sql = "INSERT INTO account (email, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Account account) {
        String sql = "UPDATE account SET email = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getUser_id());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM account WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
