package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Transaction;
import com.example.fca.entity.enums.PaymentMode;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {
    private final Datasource datasource;

    public TransactionRepository(Datasource datasource) { this.datasource = datasource; }

    public Transaction save(Transaction t) throws SQLException {
        String sql = "INSERT INTO transaction (id, collectivity_id, member_id, amount, payment_mode, account_credited_id, creation_date) VALUES (?, ?, ?, ?, ?::payment_mode, ?, ?)";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, t.getCollectivityId());
            stmt.setString(3, t.getMemberId());
            stmt.setDouble(4, t.getAmount());
            stmt.setString(5, t.getPaymentMode().name());
            stmt.setString(6, t.getAccountCreditedId());
            stmt.setDate(7, Date.valueOf(t.getCreationDate()));
            stmt.executeUpdate();
            t.setId(id);
            return t;
        }
    }

    public List<Transaction> findByCollectivityIdAndPeriod(String collectivityId, LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE collectivity_id = ? AND creation_date BETWEEN ? AND ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            List<Transaction> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getString("id"));
        t.setCollectivityId(rs.getString("collectivity_id"));
        t.setMemberId(rs.getString("member_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
        t.setAccountCreditedId(rs.getString("account_credited_id"));
        t.setCreationDate(rs.getDate("creation_date").toLocalDate());
        return t;
    }
}