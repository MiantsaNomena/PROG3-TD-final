package com.example.fca.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public abstract class FinancialAccount {
    private String id;
    private String collectivityId;
    private Double balance;
}