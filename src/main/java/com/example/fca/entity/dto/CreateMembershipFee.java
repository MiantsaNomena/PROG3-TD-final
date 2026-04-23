package com.example.fca.entity.dto;

import com.example.fca.entity.enums.Frequency;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateMembershipFee {
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
}