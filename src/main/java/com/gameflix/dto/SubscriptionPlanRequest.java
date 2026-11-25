package com.gameflix.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class SubscriptionPlanRequest {

    @NotBlank(message = "Plan name is required")
    @Size(max = 100, message = "Plan name cannot exceed 100 characters")
    private String planName;

    @NotNull(message = "Plan price is required")
    @DecimalMin(value = "0.00", message = "Plan price cannot be negative")
    @Digits(integer = 8, fraction = 2,
            message = "Invalid plan price format")
    private BigDecimal price;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}