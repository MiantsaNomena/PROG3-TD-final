package com.example.fca.entity;

import com.example.fca.entity.enums.Gender;
import com.example.fca.entity.enums.MemberOccupation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class Member {
    private String id;  // UUID ou généré automatiquement
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private MemberOccupation occupation;
    private String collectivityId;
    private boolean active;
    private LocalDate membershipDate; // date d'adhésion à la fédération
}