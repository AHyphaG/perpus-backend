package com.example.perpusapi.model;

import com.example.perpusapi.config.DatabaseConfig;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @param <E>
 */
public abstract class Model<E> {

    private Connection con;
    private Statement stmt;
    private boolean isConnected;
    private String message;
    protected String table;
    protected String primaryKey;
    protected String select = "*";
    private String where = "";
    private String join = "";
    private String otherQuery = "";
    private Map<String, Object> whereParams = new LinkedHashMap<>();

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = DatabaseConfig.getDbUrl();
            String user = DatabaseConfig.getDbUser();
            String password = DatabaseConfig.getDbPassword();

            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            isConnected = true;
            message = "Database Terkoneksi";
        } catch (ClassNotFoundException | SQLException e) {
            isConnected = false;
            message = e.getMessage();
        }
    }

    public void disconnect() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    //TerPreparedkan
    public void insert() {
        try {
            connect();
            StringBuilder cols = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            ArrayList<Object> values = new ArrayList<>();

            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null) {
                    cols.append(field.getName()).append(", ");
                    placeholders.append("?, ");
                    values.add(value);
                }
            }

            String sql = "INSERT INTO " + table + " (" + cols.substring(0, cols.length() - 2) + ") VALUES ("
                    + placeholders.substring(0, placeholders.length() - 2) + ")";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                for (int i = 0; i < values.size(); i++) {
                    pstmt.setObject(i + 1, values.get(i));
                }
                int result = pstmt.executeUpdate();
                message = "info insert: " + result + " rows affected";
            }

        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }

    //TerPreparedkan
    public void update() {
        try {
            connect();
            StringBuilder setClause = new StringBuilder();
            ArrayList<Object> values = new ArrayList<>();
            Object pkValue = null;

            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (field.getName().equals(primaryKey)) {
                    pkValue = value;
                } else if (value != null) {
                    setClause.append(field.getName()).append(" = ?, ");
                    values.add(value);
                }
            }

            if (pkValue == null) {
                message = "Primary key tidak boleh null!";
                return;
            }

            String sql = "UPDATE " + table + " SET " + setClause.substring(0, setClause.length() - 2)
                    + " WHERE " + primaryKey + " = ?";

            values.add(pkValue);

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                for (int i = 0; i < values.size(); i++) {
                    pstmt.setObject(i + 1, values.get(i));
                }
                int result = pstmt.executeUpdate();
                message = "info update: " + result + " rows affected";
            }

        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }


    //TerPreparedkan
    public void delete() {
        try {
            connect();
            Object pkValue = null;

            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals(primaryKey)) {
                    pkValue = field.get(this);
                    break;
                }
            }

            if (pkValue == null) {
                message = "Primary key tidak boleh null!";
                return;
            }

            String sql = "DELETE FROM " + table + " WHERE " + primaryKey + " = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setObject(1, pkValue);
                int result = pstmt.executeUpdate();
                message = "info delete: " + result + " rows affected";
            }

        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }


    ArrayList<Object> toRow(ResultSet rs) {
        ArrayList<Object> res = new ArrayList<>();
        int i = 1;
        try {
            while (true) {
                res.add(rs.getObject(i));
                i++;
            }
        } catch(SQLException e) {

        }
        return res;
    }

    //TerPreparedkan
    public ArrayList<ArrayList<Object>> query(String sql, Object... params) {
        ArrayList<ArrayList<Object>> res = new ArrayList<>();
        try {
            connect();
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        res.add(toRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
        return res;
    }

    abstract E toModel(ResultSet rs);

    //TerPreparedkan
    public ArrayList<E> get() {
        ArrayList<E> res = new ArrayList<>();
        try {
            connect();
            StringBuilder query = new StringBuilder("SELECT " + select + " FROM " + table);

            //JOIN
            if (!join.isEmpty()) {
                query.append(join);
            }

            //WHERE
            if (!whereParams.isEmpty()) {
                query.append(" WHERE ");
                query.append(whereParams.keySet().stream()
                        .map(col -> col + " = ?")
                        .collect(Collectors.joining(" AND ")));
            }

            //query tambahan (ORDER BY, LIMIT, dll.)
            if (!otherQuery.isEmpty()) {
                query.append(" ").append(otherQuery);
            }

            PreparedStatement pstmt = con.prepareStatement(query.toString());

            // Masukkan nilai parameter
            int index = 1;
            for (Object value : whereParams.values()) {
                pstmt.setObject(index, value);
                index++;
            }
            //System.out.println(pstmt);  // NotesDev: Untuk Check Query
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                res.add(toModel(rs));
            }

        } catch (SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
            resetQueryState();
        }
        return res;
    }

    private void resetQueryState() {
        whereParams.clear();
        select = "*";
        where = "";
        join = "";
        otherQuery = "";
    }

    public E find(String pkValue) throws SQLException{
        try {
            connect();
            String query = "SELECT " +  select + " FROM " + table + " WHERE " + primaryKey + " = '" + pkValue +"'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return toModel(rs);
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error saat mencari data: " + e.getMessage());
        } finally {
            disconnect();
            select = "*";
        }
        return null;
    }

    public void select(String cols) {
        select = cols;
    }

    public void join(String table, String on) {
        join += " JOIN " + table + " ON " + on;
    }

    //TerPreparedkan
    public void where(String column, Object value) {
        whereParams.put(column, value);
    }

    public void addQuery(String query) {
        otherQuery = query;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
