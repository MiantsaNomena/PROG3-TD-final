package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class CreateActivityMemberAttendance {
    private String memberIdentifier;
    private String attendanceStatus;
}