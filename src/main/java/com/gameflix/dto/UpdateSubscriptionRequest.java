package com.gameflix.dto;

import jakarta.validation.constraints.NotNull;

// Allows users to update their auto-renewal preference.
public class UpdateSubscriptionRequest {

    @NotNull(message = "Auto-renew preference is required")
    private Boolean autoRenew;

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
