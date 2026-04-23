package com.example.fca.entity.dto;

import com.example.fca.entity.enums.PaymentMode;
import lombok.Data;

@Data
public class CreateMemberPayment {
    private Double amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMode paymentMode;
}