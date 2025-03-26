package com.example.perpusapi.repository;

import com.example.perpusapi.config.DatabaseConfig;
import com.example.perpusapi.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private Connection connection;

    public BookRepository() {
        connection = DatabaseConfig.getConnection();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM buku";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("buku_id"),
                        rs.getInt("rakbuku_id_fk"),
                        rs.getInt("jumlah"),
                        rs.getInt("jml_tersedia"),
                        rs.getString("nama_buku"),
                        rs.getString("tipe_buku"),
                        rs.getString("jenis_buku"),
                        rs.getString("author"),
                        rs.getDate("tgl_terbit") != null ?
                                rs.getDate("tgl_terbit").toLocalDate() : null,
                        rs.getInt("status_booking")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public Book getBookById(int bookId) {
        String query = "SELECT * FROM buku WHERE buku_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("buku_id"),
                            rs.getInt("rakbuku_id_fk"),
                            rs.getInt("jumlah"),
                            rs.getInt("jml_tersedia"),
                            rs.getString("nama_buku"),
                            rs.getString("tipe_buku"),
                            rs.getString("jenis_buku"),
                            rs.getString("author"),
                            rs.getDate("tgl_terbit") != null ?
                                    rs.getDate("tgl_terbit").toLocalDate() : null,
                            rs.getInt("status_booking")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addBook(Book book) {
        String query = "INSERT INTO buku " +
                "(rakbuku_id_fk, jumlah, jml_tersedia, nama_buku, tipe_buku, " +
                "jenis_buku, author, tgl_terbit, status_booking) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, book.getRakbuku_id_fk());
            pstmt.setInt(2, book.getJumlah());
            pstmt.setInt(3, book.getJml_tersedia());
            pstmt.setString(4, book.getNama_buku());
            pstmt.setString(5, book.getTipe_buku());
            pstmt.setString(6, book.getJenis_buku());
            pstmt.setString(7, book.getAuthor());
            pstmt.setDate(8, book.getTgl_terbit() != null ?
                    Date.valueOf(book.getTgl_terbit()) : null);
            pstmt.setInt(9, book.getStatus_booking());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String query = "UPDATE buku SET " +
                "rakbuku_id_fk = ?, jumlah = ?, jml_tersedia = ?, " +
                "nama_buku = ?, tipe_buku = ?, jenis_buku = ?, " +
                "author = ?, tgl_terbit = ?, status_booking = ? " +
                "WHERE buku_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, book.getRakbuku_id_fk());
            pstmt.setInt(2, book.getJumlah());
            pstmt.setInt(3, book.getJml_tersedia());
            pstmt.setString(4, book.getNama_buku());
            pstmt.setString(5, book.getTipe_buku());
            pstmt.setString(6, book.getJenis_buku());
            pstmt.setString(7, book.getAuthor());
            pstmt.setDate(8, book.getTgl_terbit() != null ?
                    Date.valueOf(book.getTgl_terbit()) : null);
            pstmt.setInt(9, book.getStatus_booking());
            pstmt.setInt(10, book.getBuku_id());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM buku WHERE buku_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}