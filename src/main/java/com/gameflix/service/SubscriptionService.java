package com.gameflix.service;

import com.gameflix.dto.SubscriptionResponse;
import com.gameflix.model.Subscription;
import com.gameflix.model.SubscriptionPlan;
import com.gameflix.model.User;
import com.gameflix.repository.SubscriptionPlanRepository;
import com.gameflix.repository.SubscriptionRepository;
import com.gameflix.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserRepository userRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository planRepository,
            UserRepository userRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    // Retrieves all available subscription plans.
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> getPlans() {
        return planRepository.findAll();
    }

    // Creates a subscription for the authenticated user.
    @Transactional
    public SubscriptionResponse createSubscription(
            String email, Long planId) {

        User user = userRepository.findByEmailForUpdate(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (planId == null || planId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid plan ID");
        }

        // Prevents multiple active subscriptions for one user.
        if (subscriptionRepository.existsByUser_IdAndStatus(
                user.getId(), "ACTIVE")) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already has an active subscription");
        }

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Subscription plan not found"));

        Subscription subscription = new Subscription(user, plan);

        return new SubscriptionResponse(
                subscriptionRepository.save(subscription));
    }

    // Retrieves the user's current active subscription.
    @Transactional(readOnly = true)
    public SubscriptionResponse getCurrentSubscription(
            String email, Long userId) {

        User user = getUser(email);

        if (!user.getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied");
        }

        Subscription subscription = subscriptionRepository
                .findByUser_IdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No active subscription found"));

        return new SubscriptionResponse(subscription);
    }

    // Updates the auto-renewal preference.
    @Transactional
    public SubscriptionResponse updateSubscription(
            String email, Long subscriptionId, Boolean autoRenew) {

        Subscription subscription =
                getOwnedSubscription(email, subscriptionId);

        validateActiveSubscription(subscription);

        if (autoRenew == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Auto-renew preference is required");
        }

        subscription.setAutoRenew(autoRenew);

        return new SubscriptionResponse(
                subscriptionRepository.save(subscription));
    }

    // Cancels the subscription while preserving its history.
    @Transactional
    public void cancelSubscription(
            String email, Long subscriptionId) {

        Subscription subscription =
                getOwnedSubscription(email, subscriptionId);

        validateActiveSubscription(subscription);

        subscription.setStatus("CANCELLED");
        subscription.setAutoRenew(false);

        subscriptionRepository.save(subscription);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    // Verifies ownership before allowing subscription changes.
    private Subscription getOwnedSubscription(
            String email, Long subscriptionId) {

        if (subscriptionId == null || subscriptionId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid subscription ID");
        }

        User user = getUser(email);

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Subscription not found"));

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied");
        }

        return subscription;
    }

    private void validateActiveSubscription(
            Subscription subscription) {

        if (!"ACTIVE".equals(subscription.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Subscription is not active");
        }
    }
}
