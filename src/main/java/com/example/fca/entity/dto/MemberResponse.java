package com.example.fca.entity.dto;

import com.example.fca.entity.Member;
import lombok.Data;
import java.util.List;

@Data
public class MemberResponse extends MemberInformation {
    private String id;
    private List<Member> referees;
}