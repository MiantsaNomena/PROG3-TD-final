package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Collectivity;
import com.example.fca.entity.Member;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class CollectivityRepository {
    private final Datasource datasource;

    public CollectivityRepository(Datasource datasource) {
        this.datasource = datasource;
    }

    public Collectivity save(Collectivity c) throws SQLException {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);

            String id = UUID.randomUUID().toString();
            // 1. Insert collectivity
            String sqlColl = "INSERT INTO collectivity (id, location, creation_date, federation_approval) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlColl)) {
                stmt.setString(1, id);
                stmt.setString(2, c.getLocation());
                stmt.setDate(3, Date.valueOf(c.getCreationDate()));
                stmt.setBoolean(4, c.isFederationApproval());
                stmt.executeUpdate();
            }
            c.setId(id);

            // 2. Update members with collectivity_id
            String sqlUpd = "UPDATE member SET collectivity_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpd)) {
                for (Member m : c.getMembers()) {
                    stmt.setString(1, id);
                    stmt.setString(2, m.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // 3. Insert structure (roles)
            String sqlStruct = "INSERT INTO collectivity_structure (collectivity_id, role, member_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlStruct)) {
                stmt.setString(1, id);
                stmt.setString(2, "PRESIDENT");
                stmt.setString(3, c.getPresident().getId());
                stmt.addBatch();
                stmt.setString(2, "VICE_PRESIDENT");
                stmt.setString(3, c.getVicePresident().getId());
                stmt.addBatch();
                stmt.setString(2, "TREASURER");
                stmt.setString(3, c.getTreasurer().getId());
                stmt.addBatch();
                stmt.setString(2, "SECRETARY");
                stmt.setString(3, c.getSecretary().getId());
                stmt.addBatch();
                stmt.executeBatch();
            }

            conn.commit();
            return c;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

}