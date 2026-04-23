package com.example.fca.controller;

import com.example.fca.entity.Collectivity;
import com.example.fca.entity.dto.CollectivityInformation;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.CollectivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collectivities")
public class CollectivityInformationController {

    private final CollectivityService service;

    public CollectivityInformationController(CollectivityService service) {
        this.service = service;
    }

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> updateInformation(@PathVariable String id, @RequestBody CollectivityInformation info) {
        try {
            Collectivity updated = service.assignInformations(id, info);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(new ErrorResponse(409, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}