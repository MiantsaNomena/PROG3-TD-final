package com.example.fca.entity.dto;

import com.example.fca.entity.Member;
import lombok.Data;
import java.util.List;

@Data
public class CollectivityResponse {
    private String id;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;
}