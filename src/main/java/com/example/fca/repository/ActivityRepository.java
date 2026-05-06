package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Activity;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ActivityRepository {
    private final Datasource datasource;

    public ActivityRepository(Datasource datasource) { this.datasource = datasource; }

    public List<Activity> findByCollectivityId(String collectivityId) throws SQLException {
        String sql = "SELECT * FROM activity WHERE collectivity_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            ResultSet rs = stmt.executeQuery();
            List<Activity> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public Optional<Activity> findById(String id) throws SQLException {
        String sql = "SELECT * FROM activity WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        }
    }

    public Activity save(Activity a) throws SQLException {
        String sql = "INSERT INTO activity (id, collectivity_id, label, activity_type, executive_date, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, a.getCollectivityId());
            stmt.setString(3, a.getLabel());
            stmt.setString(4, a.getActivityType());
            stmt.setDate(5, a.getExecutiveDate() != null ? Date.valueOf(a.getExecutiveDate()) : null);
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            a.setId(id);
            return a;
        }
    }

    private Activity map(ResultSet rs) throws SQLException {
        Activity a = new Activity();
        a.setId(rs.getString("id"));
        a.setCollectivityId(rs.getString("collectivity_id"));
        a.setLabel(rs.getString("label"));
        a.setActivityType(rs.getString("activity_type"));
        Date d = rs.getDate("executive_date");
        if (d != null) a.setExecutiveDate(d.toLocalDate());
        a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return a;
    }
}