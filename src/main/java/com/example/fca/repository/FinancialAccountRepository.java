package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.*;
import com.example.fca.entity.enums.Bank;
import com.example.fca.entity.enums.MobileBankingService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialAccountRepository {

    private final Datasource datasource;

    public FinancialAccountRepository(Datasource datasource) {
        this.datasource = datasource;
    }

    public FinancialAccount save(FinancialAccount account) throws SQLException {
        String sql = "INSERT INTO financial_account (id, collectivity_id, account_type, holder_name, " +
                "mobile_service, mobile_number, bank_name, bank_code, bank_branch_code, " +
                "bank_account_number, bank_account_key, balance) " +
                "VALUES (?, ?, ?, ?, ?::mobile_banking_service, ?, ?::bank, ?, ?, ?, ?, ?)";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, account.getCollectivityId());

            if (account instanceof CashAccount) {
                stmt.setString(3, "CASH");
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            } else if (account instanceof MobileBankingAccount) {
                MobileBankingAccount mb = (MobileBankingAccount) account;
                stmt.setString(3, "MOBILE");
                stmt.setString(4, mb.getHolderName());
                stmt.setString(5, mb.getMobileBankingService().name());
                stmt.setString(6, mb.getMobileNumber());
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            } else if (account instanceof BankAccount) {
                BankAccount ba = (BankAccount) account;
                stmt.setString(3, "BANK");
                stmt.setString(4, ba.getHolderName());
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setString(7, ba.getBankName().name());
                stmt.setString(8, ba.getBankCode());
                stmt.setString(9, ba.getBankBranchCode());
                stmt.setString(10, ba.getBankAccountNumber());
                stmt.setString(11, ba.getBankAccountKey());
            } else {
                throw new IllegalArgumentException("Unknown financial account type");
            }

            stmt.setDouble(12, account.getBalance());
            stmt.executeUpdate();

            account.setId(id);
            return account;
        }
    }

    public Optional<FinancialAccount> findById(String id) throws SQLException {
        String sql = "SELECT * FROM financial_account WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();
        }
    }

    public void updateBalance(String accountId, double newBalance) throws SQLException {
        String sql = "UPDATE financial_account SET balance = ? WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountId);
            stmt.executeUpdate();
        }
    }
    private FinancialAccount map(ResultSet rs) throws SQLException {
        String type = rs.getString("account_type");
        FinancialAccount account;

        if ("CASH".equals(type)) {
            account = new CashAccount();
        } else if ("MOBILE".equals(type)) {
            MobileBankingAccount mb = new MobileBankingAccount();
            mb.setHolderName(rs.getString("holder_name"));
            String service = rs.getString("mobile_service");
            if (service != null) {
                mb.setMobileBankingService(MobileBankingService.valueOf(service));
            }
            mb.setMobileNumber(rs.getString("mobile_number"));
            account = mb;
        } else if ("BANK".equals(type)) {
            BankAccount ba = new BankAccount();
            ba.setHolderName(rs.getString("holder_name"));
            String bank = rs.getString("bank_name");
            if (bank != null) {
                ba.setBankName(Bank.valueOf(bank));
            }
            ba.setBankCode(rs.getString("bank_code"));
            ba.setBankBranchCode(rs.getString("bank_branch_code"));
            ba.setBankAccountNumber(rs.getString("bank_account_number"));
            ba.setBankAccountKey(rs.getString("bank_account_key"));
            account = ba;
        } else {
            throw new SQLException("Unknown account_type: " + type);
        }

        account.setId(rs.getString("id"));
        account.setCollectivityId(rs.getString("collectivity_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}