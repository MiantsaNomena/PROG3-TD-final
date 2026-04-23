package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.MembershipFee;
import com.example.fca.entity.enums.ActivityStatus;
import com.example.fca.entity.enums.Frequency;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class MembershipFeeRepository {
    private final Datasource datasource;

    public MembershipFeeRepository(Datasource datasource) { this.datasource = datasource; }

    public List<MembershipFee> findByCollectivityId(String collectivityId) throws SQLException {
        String sql = "SELECT * FROM membership_fee WHERE collectivity_id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public List<MembershipFee> saveAll(String collectivityId, List<MembershipFee> fees) throws SQLException {
        String sql = "INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status) VALUES (?, ?, ?, ?, ?, ?, ?)";        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            List<MembershipFee> saved = new ArrayList<>();
            for (MembershipFee fee : fees) {
                String id = UUID.randomUUID().toString();
                stmt.setString(1, id);
                stmt.setString(2, collectivityId);
                stmt.setDate(3, Date.valueOf(fee.getEligibleFrom()));
                stmt.setString(4, fee.getFrequency().name());
                stmt.setDouble(5, fee.getAmount());
                stmt.setString(6, fee.getLabel());
                stmt.setString(7, fee.getStatus().name());
                stmt.addBatch();
                fee.setId(id);
                saved.add(fee);
            }
            stmt.executeBatch();
            return saved;
        }
    }

    private MembershipFee map(ResultSet rs) throws SQLException {
        MembershipFee mf = new MembershipFee();
        mf.setId(rs.getString("id"));
        mf.setCollectivityId(rs.getString("collectivity_id"));
        mf.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
        mf.setFrequency(Frequency.valueOf(rs.getString("frequency")));
        mf.setAmount(rs.getDouble("amount"));
        mf.setLabel(rs.getString("label"));
        mf.setStatus(ActivityStatus.valueOf(rs.getString("status")));
        return mf;
    }
    public Optional<MembershipFee> findById(String id) throws SQLException {
        String sql = "SELECT * FROM membership_fee WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        }
    }
}