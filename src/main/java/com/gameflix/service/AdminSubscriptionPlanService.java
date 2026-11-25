package com.gameflix.service;

import com.gameflix.dto.SubscriptionPlanRequest;
import com.gameflix.model.SubscriptionPlan;
import com.gameflix.repository.SubscriptionPlanRepository;
import com.gameflix.repository.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminSubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AdminSubscriptionPlanService(
            SubscriptionPlanRepository planRepository,
            SubscriptionRepository subscriptionRepository) {

        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public SubscriptionPlan createPlan(SubscriptionPlanRequest request) {

        SubscriptionPlan plan = new SubscriptionPlan(
                request.getPlanName().trim(),
                request.getPrice()
        );

        return planRepository.save(plan);
    }

    @Transactional
    public SubscriptionPlan updatePlan(
            Long id,
            SubscriptionPlanRequest request) {

        SubscriptionPlan plan = findPlan(id);

        plan.setPlanName(request.getPlanName().trim());
        plan.setPrice(request.getPrice());

        return planRepository.save(plan);
    }

    @Transactional
    public void deletePlan(Long id) {

        SubscriptionPlan plan = findPlan(id);

        if (subscriptionRepository.existsByPlan_PlanId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete a plan with existing subscriptions"
            );
        }

        planRepository.delete(plan);
    }

    private SubscriptionPlan findPlan(Long id) {

        if (id == null || id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid subscription plan ID"
            );
        }

        return planRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Subscription plan not found"
                ));
    }
}