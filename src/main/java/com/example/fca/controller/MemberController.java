package com.example.fca.controller;

import com.example.fca.entity.dto.CreateMember;
import com.example.fca.entity.dto.MemberResponse;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> createMembers(@RequestBody List<CreateMember> dtos) {
        try {
            List<MemberResponse> created = service.createMembers(dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Erreur interne"));
        }
    }
}