package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class ActivityMemberAttendance {
    private String id;
    private MemberDescription memberDescription;
    private String attendanceStatus;
}