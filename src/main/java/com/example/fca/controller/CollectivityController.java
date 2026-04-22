package com.example.fca.controller;

import com.example.fca.entity.Collectivity;
import com.example.fca.entity.dto.CollectivityResponse;
import com.example.fca.entity.dto.CreateCollectivity;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @RestController
    @RequestMapping("/collectivities")
    public class CollectivityIdentifierController {
        private final CollectivityService service;

        public CollectivityIdentifierController(CollectivityService service) { this.service = service; }

        @PutMapping("/{collectivityId}/identifiers")
        public ResponseEntity<?> assignIdentifiers(
                @PathVariable String collectivityId,
                @RequestBody Map<String, String> payload) {
            try {
                String uniqueNumber = payload.get("uniqueNumber");
                String uniqueName = payload.get("uniqueName");
                if (uniqueNumber == null || uniqueName == null)
                    throw new IllegalArgumentException("uniqueNumber and uniqueName are required");
                Collectivity updated = service.assignIdentifiers(collectivityId, uniqueNumber, uniqueName);
                return ResponseEntity.ok(updated);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Internal error"));
            }
        }
    }
}