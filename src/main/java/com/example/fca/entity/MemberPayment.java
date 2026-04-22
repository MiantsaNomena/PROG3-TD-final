package com.example.fca.entity;

import com.example.fca.entity.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MemberPayment {
    private String id;
    private String memberId;
    private String membershipFeeId;
    private Double amount;
    private PaymentMode paymentMode;
    private String accountCreditedId;
    private LocalDate creationDate;
}