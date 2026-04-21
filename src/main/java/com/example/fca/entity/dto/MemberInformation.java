package com.example.fca.entity.dto;

import com.example.fca.entity.enums.Gender;
import com.example.fca.entity.enums.MemberOccupation;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberInformation {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private MemberOccupation occupation;
}