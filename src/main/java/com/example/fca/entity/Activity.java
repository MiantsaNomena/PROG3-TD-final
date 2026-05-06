package com.example.fca.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class Activity {
    private String id;
    private String collectivityId;
    private String label;
    private String activityType;
    private LocalDate executiveDate;
    private java.time.LocalDateTime createdAt;
}