package com.example.fca.controller;
// controller/CollectivityController.java

import com.example.fca.entity.dto.CollectivityResponse;
import com.example.fca.entity.dto.CreateCollectivity;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {
    private final CollectivityService service;

    public CollectivityController(CollectivityService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> createCollectivities(@RequestBody List<CreateCollectivity> dtos) {
        try {
            List<CollectivityResponse> created = service.createCollectivities(dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Internal error"));
        }
    }
}