package com.example.fca.controller;

import com.example.fca.entity.dto.CreateMembershipFee;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.entity.MembershipFee;
import com.example.fca.service.MembershipFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/collectivities/{id}/membershipFees")
public class MembershipFeeController {
    private final MembershipFeeService service;

    public MembershipFeeController(MembershipFeeService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> getFees(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getFees(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFees(@PathVariable String id, @RequestBody List<CreateMembershipFee> dtos) {
        try {
            List<MembershipFee> fees = dtos.stream().map(dto -> {
                MembershipFee fee = new MembershipFee();
                fee.setEligibleFrom(dto.getEligibleFrom());
                fee.setFrequency(dto.getFrequency());
                fee.setAmount(dto.getAmount());
                fee.setLabel(dto.getLabel());
                return fee;
            }).collect(Collectors.toList());
            List<MembershipFee> created = service.createFees(id, fees);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}