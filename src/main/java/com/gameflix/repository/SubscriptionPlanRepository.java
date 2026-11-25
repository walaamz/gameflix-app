package com.gameflix.repository;

import com.gameflix.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides database access for subscription plans.
public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long> {

    // Checks for duplicate plan names when managing plans.
    boolean existsByPlanNameIgnoreCase(String planName);
}
