package com.example.fca.controller;

import com.example.fca.entity.dto.CreateMemberPayment;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.entity.dto.MemberPaymentResponse;
import com.example.fca.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/members/{id}/payments")
public class MemberPaymentController {

    private final PaymentService paymentService;

    public MemberPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPayments(@PathVariable String id, @RequestBody List<CreateMemberPayment> payments) {
        try {
            List<MemberPaymentResponse> created = paymentService.createPayments(id, payments);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Internal error: " + e.getMessage()));
        }
    }
}