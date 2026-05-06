package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class MemberStatistics {
    private String memberId;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
    private Double earnedAmount;
    private Double unpaidAmount;
}