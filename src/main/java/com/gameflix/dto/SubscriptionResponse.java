package com.gameflix.dto;

import com.gameflix.model.Subscription;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Returns subscription details without exposing sensitive user data.
public class SubscriptionResponse {

    private final Long subscriptionId;
    private final Long planId;
    private final String planName;
    private final BigDecimal price;
    private final String status;
    private final boolean autoRenew;
    private final LocalDateTime createdAt;

    // Maps the subscription entity to a safe API response.
    public SubscriptionResponse(Subscription subscription) {
        this.subscriptionId = subscription.getSubscriptionId();
        this.planId = subscription.getPlan().getPlanId();
        this.planName = subscription.getPlan().getPlanName();
        this.price = subscription.getPlan().getPrice();
        this.status = subscription.getStatus();
        this.autoRenew = subscription.isAutoRenew();
        this.createdAt = subscription.getCreatedAt();
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public Long getPlanId() {
        return planId;
    }

    public String getPlanName() {
        return planName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
