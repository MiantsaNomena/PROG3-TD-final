package com.example.fca.service;

import com.example.fca.entity.dto.CreateMemberPayment;
import com.example.fca.entity.dto.MemberPaymentResponse;
import com.example.fca.entity.*;
import com.example.fca.repository.*;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final MemberRepository memberRepository;
    private final MembershipFeeRepository feeRepository;
    private final FinancialAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MemberPaymentRepository paymentRepository;

    public PaymentService(MemberRepository memberRepository,
                          MembershipFeeRepository feeRepository,
                          FinancialAccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          MemberPaymentRepository paymentRepository) {
        this.memberRepository = memberRepository;
        this.feeRepository = feeRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<MemberPaymentResponse> createPayments(String memberId, List<CreateMemberPayment> payments) throws SQLException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        String collectivityId = member.getCollectivityId();
        if (collectivityId == null)
            throw new IllegalArgumentException("Member not attached to any collectivity");

        List<MemberPaymentResponse> responses = new ArrayList<>();
        for (CreateMemberPayment p : payments) {
            MembershipFee fee = feeRepository.findById(p.getMembershipFeeIdentifier())
                    .orElseThrow(() -> new IllegalArgumentException("Membership fee not found"));
            FinancialAccount account = accountRepository.findById(p.getAccountCreditedIdentifier())
                    .orElseThrow(() -> new IllegalArgumentException("Financial account not found"));
            if (p.getAmount() <= 0)
                throw new IllegalArgumentException("Amount must be positive");

            MemberPayment payment = new MemberPayment();
            payment.setMemberId(memberId);
            payment.setMembershipFeeId(p.getMembershipFeeIdentifier());
            payment.setAmount(p.getAmount());
            payment.setPaymentMode(p.getPaymentMode());
            payment.setAccountCreditedId(account.getId());
            payment.setCreationDate(LocalDate.now());
            payment = paymentRepository.save(payment);

            Transaction transaction = new Transaction();
            transaction.setCollectivityId(collectivityId);
            transaction.setMemberId(memberId);
            transaction.setAmount(p.getAmount());
            transaction.setPaymentMode(p.getPaymentMode());
            transaction.setAccountCreditedId(account.getId());
            transaction.setCreationDate(LocalDate.now());
            transactionRepository.save(transaction);

            account.setBalance(account.getBalance() + p.getAmount());
            accountRepository.updateBalance(account.getId(), account.getBalance());

            MemberPaymentResponse response = new MemberPaymentResponse();
            response.setId(payment.getId());
            response.setAmount(payment.getAmount());
            response.setPaymentMode(payment.getPaymentMode());
            response.setAccountCredited(account);
            response.setCreationDate(payment.getCreationDate());
            responses.add(response);
        }
        return responses;
    }
}