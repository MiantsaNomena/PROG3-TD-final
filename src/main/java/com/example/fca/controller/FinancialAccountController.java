package com.example.fca.controller;

import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.FinancialAccountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/collectivities/{id}/financialAccounts")
public class FinancialAccountController {
    private final FinancialAccountService service;

    public FinancialAccountController(FinancialAccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getFinancialAccounts(@PathVariable String id,
                                                  @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at) {
        try {
            var accounts = service.getAccountsWithBalanceAt(id, at);
            return ResponseEntity.ok(accounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}