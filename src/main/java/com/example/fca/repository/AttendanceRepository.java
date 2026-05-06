package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Attendance;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AttendanceRepository {
    private final Datasource datasource;

    public AttendanceRepository(Datasource datasource) { this.datasource = datasource; }

    public List<Attendance> findByActivityId(String activityId) throws SQLException {
        String sql = "SELECT * FROM attendance WHERE activity_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activityId);
            ResultSet rs = stmt.executeQuery();
            List<Attendance> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public Optional<Attendance> findByActivityAndMember(String activityId, String memberId) throws SQLException {
        String sql = "SELECT * FROM attendance WHERE activity_id = ? AND member_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activityId);
            stmt.setString(2, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        }
    }

    public Attendance save(Attendance a) throws SQLException {
        String sql = "INSERT INTO attendance (id, activity_id, member_id, status, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, a.getActivityId());
            stmt.setString(3, a.getMemberId());
            stmt.setString(4, a.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            a.setId(id);
            return a;
        }
    }

    public boolean updateStatus(String activityId, String memberId, String newStatus) throws SQLException {
        // Vérifier si le statut actuel n'est pas déjà ATTENDED ou MISSING (non modifiable)
        String checkSql = "SELECT status FROM attendance WHERE activity_id = ? AND member_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setString(1, activityId);
            stmt.setString(2, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String current = rs.getString("status");
                if ("ATTENDED".equals(current) || "MISSING".equals(current)) {
                    return false; // ne peut pas être modifié
                }
            }
        }
        String sql = "UPDATE attendance SET status = ?, updated_at = ? WHERE activity_id = ? AND member_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, activityId);
            stmt.setString(4, memberId);
            int updated = stmt.executeUpdate();
            return updated > 0;
        }
    }

    public void initializeDefaultAttendance(String activityId, List<String> memberIds) throws SQLException {
        String sql = "INSERT INTO attendance (id, activity_id, member_id, status, updated_at) VALUES (?, ?, ?, 'UNDEFINED', ?) ON CONFLICT DO NOTHING";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String memberId : memberIds) {
                String id = UUID.randomUUID().toString();
                stmt.setString(1, id);
                stmt.setString(2, activityId);
                stmt.setString(3, memberId);
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Attendance map(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setId(rs.getString("id"));
        a.setActivityId(rs.getString("activity_id"));
        a.setMemberId(rs.getString("member_id"));
        a.setStatus(rs.getString("status"));
        a.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return a;
    }
}