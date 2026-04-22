package com.example.fca.entity;

import com.example.fca.entity.enums.ActivityStatus;
import com.example.fca.entity.enums.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MembershipFee {
    private String id;
    private String collectivityId;
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
    private ActivityStatus status;
}