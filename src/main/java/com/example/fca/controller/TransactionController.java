package com.example.fca.controller;

import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/collectivities/{id}/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> getTransactions(@PathVariable String id,
                                             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            var transactions = service.getTransactions(id, from, to);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Collectivity not found"))
                return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}