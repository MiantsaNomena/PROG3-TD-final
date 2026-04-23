package com.example.fca.service;

import com.example.fca.entity.*;
import com.example.fca.entity.dto.CollectivityTransaction;
import com.example.fca.repository.*;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, CollectivityRepository collectivityRepository, MemberRepository memberRepository, FinancialAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    public List<CollectivityTransaction> getTransactions(String collectivityId, LocalDate from, LocalDate to) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        List<Transaction> transactions = transactionRepository.findByCollectivityIdAndPeriod(collectivityId, from, to);
        List<CollectivityTransaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            CollectivityTransaction ct = new CollectivityTransaction();
            ct.setId(t.getId());
            ct.setCreationDate(t.getCreationDate());
            ct.setAmount(t.getAmount());
            ct.setPaymentMode(t.getPaymentMode());
            ct.setAccountCredited(accountRepository.findById(t.getAccountCreditedId())
                    .orElseThrow(() -> new RuntimeException("Account not found")));
            ct.setMemberDebited(memberRepository.findById(t.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found")));
            result.add(ct);
        }
        return result;
    }
}