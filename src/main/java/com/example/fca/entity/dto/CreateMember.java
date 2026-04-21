package com.example.fca.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateMember {
    private String collectivityIdentifier;
    private List<String> referees;
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;
    private MemberInformation memberInfo;
}