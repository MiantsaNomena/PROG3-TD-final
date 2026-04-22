package com.example.fca.entity.dto;

import com.example.fca.entity.Member;
import lombok.Data;

@Data
public class CollectivityStructure {
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;
}