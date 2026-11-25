package com.gameflix.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Represents a request to subscribe to a selected plan.
public class CreateSubscriptionRequest {

    @NotNull(message = "Plan ID is required")
    @Positive(message = "Invalid plan ID")
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
