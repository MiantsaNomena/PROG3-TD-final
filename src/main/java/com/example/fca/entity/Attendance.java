package com.example.fca.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class Attendance {
    private String id;
    private String activityId;
    private String memberId;
    private String status;
    private LocalDateTime updatedAt;
}