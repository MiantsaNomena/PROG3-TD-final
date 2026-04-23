package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.MemberPayment;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.UUID;

@Repository
public class MemberPaymentRepository {
    private final Datasource datasource;

    public MemberPaymentRepository(Datasource datasource) { this.datasource = datasource; }

    public MemberPayment save(MemberPayment payment) throws SQLException {
        String sql = "INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES (?, ?, ?, ?, ?::payment_mode, ?, ?)";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, payment.getMemberId());
            stmt.setString(3, payment.getMembershipFeeId());
            stmt.setDouble(4, payment.getAmount());
            stmt.setString(5, payment.getPaymentMode().name());
            stmt.setString(6, payment.getAccountCreditedId());
            stmt.setDate(7, Date.valueOf(payment.getCreationDate()));
            stmt.executeUpdate();
            payment.setId(id);
            return payment;
        }
    }
}