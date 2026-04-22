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
    private final MemberRepository memberRepository;

    public CollectivityRepository(Datasource datasource, MemberRepository memberRepository) {
        this.datasource = datasource;
        this.memberRepository = memberRepository;
    }

    public Collectivity save(Collectivity c) throws SQLException {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);

            String id = UUID.randomUUID().toString();
            String sqlColl = "INSERT INTO collectivity (id, location, creation_date, federation_approval) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlColl)) {
                stmt.setString(1, id);
                stmt.setString(2, c.getLocation());
                stmt.setDate(3, Date.valueOf(c.getCreationDate()));
                stmt.setBoolean(4, c.isFederationApproval());
                stmt.executeUpdate();
            }
            c.setId(id);

            String sqlUpd = "UPDATE member SET collectivity_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpd)) {
                for (Member m : c.getMembers()) {
                    stmt.setString(1, id);
                    stmt.setString(2, m.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

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

    public Optional<Collectivity> findById(String id) throws SQLException {
        String sql = "SELECT id, location, creation_date, federation_approval, unique_number, unique_name FROM collectivity WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Collectivity c = map(rs);
                // Load members and structure
                c.setMembers(memberRepository.findByCollectivityId(id, null));
                loadStructure(c, conn);
                return Optional.of(c);
            }
            return Optional.empty();
        }
    }

    public List<Collectivity> findAll() throws SQLException {
        String sql = "SELECT id, location, creation_date, federation_approval, unique_number, unique_name FROM collectivity";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Collectivity> list = new ArrayList<>();
            while (rs.next()) {
                Collectivity c = map(rs);
                c.setMembers(memberRepository.findByCollectivityId(c.getId(), null));
                loadStructure(c, conn);
                list.add(c);
            }
            return list;
        }
    }

    private void loadStructure(Collectivity c, Connection conn) throws SQLException {
        String sql = "SELECT role, member_id FROM collectivity_structure WHERE collectivity_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("role");
                String memberId = rs.getString("member_id");
                Member m = memberRepository.findById(memberId).orElse(null);
                if (m != null) {
                    switch (role) {
                        case "PRESIDENT": c.setPresident(m); break;
                        case "VICE_PRESIDENT": c.setVicePresident(m); break;
                        case "TREASURER": c.setTreasurer(m); break;
                        case "SECRETARY": c.setSecretary(m); break;
                    }
                }
            }
        }
    }

    public void assignIdentifiers(String collectivityId, String uniqueNumber, String uniqueName) throws SQLException {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);

            // 1. Check collectivity exists
            String checkExists = "SELECT id FROM collectivity WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkExists)) {
                stmt.setString(1, collectivityId);
                if (!stmt.executeQuery().next())
                    throw new IllegalArgumentException("Collectivity not found");
            }

            String checkAssigned = "SELECT unique_number, unique_name FROM collectivity WHERE id = ? AND (unique_number IS NOT NULL OR unique_name IS NOT NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(checkAssigned)) {
                stmt.setString(1, collectivityId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String existingNumber = rs.getString("unique_number");
                    String existingName = rs.getString("unique_name");
                    throw new IllegalStateException("Identifiers already assigned (number: " + existingNumber + ", name: " + existingName + ")");
                }
            }

            String checkUnique = "SELECT id FROM collectivity WHERE unique_number = ? OR unique_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkUnique)) {
                stmt.setString(1, uniqueNumber);
                stmt.setString(2, uniqueName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    throw new IllegalArgumentException("Unique number or name already exists in another collectivity");
            }

            String updateSql = "UPDATE collectivity SET unique_number = ?, unique_name = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setString(1, uniqueNumber);
                stmt.setString(2, uniqueName);
                stmt.setString(3, collectivityId);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException | IllegalArgumentException | IllegalStateException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    public void deleteById(String id) throws SQLException {
        String sql = "DELETE FROM collectivity WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Collectivity map(ResultSet rs) throws SQLException {
        Collectivity c = new Collectivity();
        c.setId(rs.getString("id"));
        c.setLocation(rs.getString("location"));
        c.setCreationDate(rs.getDate("creation_date").toLocalDate());
        c.setFederationApproval(rs.getBoolean("federation_approval"));
        c.setUniqueNumber(rs.getString("unique_number"));
        c.setUniqueName(rs.getString("unique_name"));
        return c;
    }
}