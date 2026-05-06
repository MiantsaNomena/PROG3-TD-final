package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class CollectivityOverallStats {
    private String collectivityId;
    private String collectivityName;
    private String collectivityNumber;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
}


