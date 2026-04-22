package com.example.fca.entity.dto;

import com.example.fca.entity.FinancialAccount;
import com.example.fca.entity.Member;
import com.example.fca.entity.enums.PaymentMode;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CollectivityTransaction {
    private String id;
    private LocalDate creationDate;
    private Double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;
}