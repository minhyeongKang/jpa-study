package me.hellomeen.jdbc.dao;

import me.hellomeen.jdbc.vo.AccountVO;

import java.sql.*;

public class AccountDAO {

    //JDBC 관련 변수
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    String url = "jdbc:postgresql://localhost:5432/messenger";
    String username = "minhyeongKang";
    String password = "pass";

    //SQL 관련 명령어
    private final String ACCOUNT_INSERT = "INSERT INTO account(ID, USERNAME, PASSWORD)"
            + "VALUES((SELECT coalesce(MAX(ID), 0) + 1 FROM ACCOUNT A), ?, ?)";
    // coalesce 은 Postgresql 용 IFNULL
    private final String ACCOUNT_SELECT = "SELECT * FROM account WHERE ID = ?";

    // CRUD 기능 메소드
    public Integer insertAccount(AccountVO vo) {
        var id = -1;
        try {
            String[] returnId = {"id"};
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_INSERT, returnId);
            stmt.setString(1, vo.getUsername());
            stmt.setString(2, vo.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()){
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public AccountVO selectAccount(Integer id) {

        AccountVO vo = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_SELECT);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                vo = new AccountVO();
                vo.setId(rs.getInt("ID"));
                vo.setUsername(rs.getString("USERNAME"));
                vo.setPassword(rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vo;
    }
}
