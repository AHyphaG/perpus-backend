package com.example.perpusapi.repository;
import com.example.perpusapi.config.DatabaseConfig;
import com.example.perpusapi.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
public class BookRepository {
    private final String table = "buku";

    public Optional<Book> findById(int id) throws SQLException {
        String sql = "Select * from " +table+ "where buku_id = ?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                Book book = new Book(
                  rs.getInt("buku_id") ,
                  rs.getInt("rakbuku_id_fk"),
                        rs.getInt("jumlah"),
                        rs.getInt("jml_tersedia"),
                        rs.getString("nama_buku"),
                        rs.getString("tipe_buku"),
                        rs.getString("jenis_buku"),
                        rs.getString("author"),
                        rs.getDate("tgl_terbit").toLocalDate(),
                        rs.getInt("status_booking")
                );
                return Optional.of(book);
            }

        }catch (Exception e){
            throw e;
        }
        return Optional.empty();
    }
}
