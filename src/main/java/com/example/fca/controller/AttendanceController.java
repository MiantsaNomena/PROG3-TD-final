package com.example.fca.controller;

import com.example.fca.entity.dto.CreateActivityMemberAttendance;
import com.example.fca.exception.ErrorResponse;
import com.example.fca.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/collectivities/{collectivityId}/activities/{activityId}/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<?> getAttendance(@PathVariable String collectivityId, @PathVariable String activityId) {
        try {
            var attendance = attendanceService.getAttendance(collectivityId, activityId);
            return ResponseEntity.ok(attendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> setAttendance(@PathVariable String collectivityId,
                                           @PathVariable String activityId,
                                           @RequestBody List<CreateActivityMemberAttendance> dtos) {
        try {
            var created = attendanceService.setAttendance(collectivityId, activityId, dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal error"));
        }
    }
}