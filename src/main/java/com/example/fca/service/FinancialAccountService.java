package com.example.fca.service;

import com.example.fca.entity.FinancialAccount;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.FinancialAccountRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialAccountService {
    private final FinancialAccountRepository accountRepository;
    private final CollectivityRepository collectivityRepository;

    public FinancialAccountService(FinancialAccountRepository ar, CollectivityRepository cr) {
        this.accountRepository = ar;
        this.collectivityRepository = cr;
    }

    public List<FinancialAccount> getAccountsWithBalanceAt(String collectivityId, LocalDate at) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        if (at == null) throw new IllegalArgumentException("Parameter 'at' is required");
        return accountRepository.findByCollectivityIdWithBalanceAt(collectivityId, at);
    }
}