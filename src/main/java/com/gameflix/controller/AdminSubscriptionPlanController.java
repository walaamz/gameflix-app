package com.gameflix.controller;

import com.gameflix.dto.SubscriptionPlanRequest;
import com.gameflix.model.SubscriptionPlan;
import com.gameflix.service.AdminSubscriptionPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subscription-plans")
public class AdminSubscriptionPlanController {

    private final AdminSubscriptionPlanService planService;

    public AdminSubscriptionPlanController(
            AdminSubscriptionPlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionPlan> createPlan(
            @Valid @RequestBody SubscriptionPlanRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(planService.createPlan(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanRequest request) {

        return ResponseEntity.ok(
                planService.updatePlan(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id) {

        planService.deletePlan(id);

        return ResponseEntity.noContent().build();
    }
}