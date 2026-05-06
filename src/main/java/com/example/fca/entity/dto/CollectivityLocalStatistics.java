package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class CollectivityLocalStatistics {
    private MemberDescription memberDescription;
    private Double earnedAmount;
    private Double unpaidAmount;
    private Double assiduityPercentage;
}

