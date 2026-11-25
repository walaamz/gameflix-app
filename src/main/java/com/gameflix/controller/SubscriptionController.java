package com.gameflix.controller;

import com.gameflix.dto.CreateSubscriptionRequest;
import com.gameflix.dto.UpdateSubscriptionRequest;
import com.gameflix.dto.SubscriptionResponse;
import com.gameflix.model.SubscriptionPlan;
import com.gameflix.service.SubscriptionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Retrieves available subscription plans.
    @GetMapping("/subscription-plans")
    public ResponseEntity<List<SubscriptionPlan>> getPlans() {
        return ResponseEntity.ok(subscriptionService.getPlans());
    }

    // Creates a subscription for the authenticated user.
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request,
            Authentication authentication) {

        SubscriptionResponse response =
                subscriptionService.createSubscription(
                        authentication.getName(),
                        request.getPlanId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // Retrieves the user's current subscription.
    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription(
            @PathVariable Long userId,
            Authentication authentication) {

        return ResponseEntity.ok(
                subscriptionService.getCurrentSubscription(
                        authentication.getName(), userId));
    }

    // Updates the subscription's auto-renewal preference.
    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubscriptionRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                subscriptionService.updateSubscription(
                        authentication.getName(),
                        id,
                        request.getAutoRenew()));
    }

    // Cancels a subscription without deleting its history.
    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable Long id,
            Authentication authentication) {

        subscriptionService.cancelSubscription(
                authentication.getName(), id);

        return ResponseEntity.noContent().build();
    }
}
