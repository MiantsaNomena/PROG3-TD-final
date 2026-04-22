package com.example.fca.entity.dto;

import com.example.fca.entity.FinancialAccount;
import com.example.fca.entity.enums.PaymentMode;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberPaymentResponse {
    private String id;
    private Double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;
}