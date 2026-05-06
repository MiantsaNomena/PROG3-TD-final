package com.example.fca.entity.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCollectivityActivity {
    private String label;
    private String activityType;
    private List<String> memberOccupationConcerned;
    private LocalDate executiveDate;
}