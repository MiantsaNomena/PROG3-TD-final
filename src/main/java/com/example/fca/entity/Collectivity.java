package com.example.fca.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class Collectivity {
    private String id;
    private String location;
    private List<Member> members;
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;
    private LocalDate creationDate;
    private boolean federationApproval;
}