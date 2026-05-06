package com.example.fca.entity.dto;

import lombok.Data;

@Data
public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
}