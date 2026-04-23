package com.example.fca.validator;

import com.example.fca.entity.MembershipFee;
import org.springframework.stereotype.Component;

@Component
public class MembershipFeeValidator {
    public void validate(MembershipFee fee) {
        if (fee.getAmount() == null || fee.getAmount() < 0)
            throw new IllegalArgumentException("Amount must be >= 0");
        if (fee.getEligibleFrom() == null)
            throw new IllegalArgumentException("Eligible from date required");
    }
}