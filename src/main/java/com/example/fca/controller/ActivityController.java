package com.example.fca.controller;

import com.example.fca.entity.dto.CreateCollectivityActivity;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/collectivities/{id}/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            var activities = activityService.getActivities(id);
            return ResponseEntity.ok(activities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createActivities(@PathVariable String id, @RequestBody List<CreateCollectivityActivity> dtos) {
        try {
            var created = activityService.createActivities(id, dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}