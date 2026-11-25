package com.gameflix.repository;

import com.gameflix.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    // Retrieves a user's subscription history.
    List<Subscription> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // Retrieves the user's current active subscription.
    Optional<Subscription> findByUser_IdAndStatus(
            Long userId, String status);

    // Checks whether the user already has an active subscription.
    boolean existsByUser_IdAndStatus(
            Long userId, String status);

    // Checks whether a subscription plan is currently referenced.
    boolean existsByPlan_PlanId(Long planId);
}
