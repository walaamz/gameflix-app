package com.gameflix.config;

import com.gameflix.model.SubscriptionPlan;
import com.gameflix.repository.SubscriptionPlanRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@Profile("demo")
public class SubscriptionPlanDataSeeder {

    @Bean
    public CommandLineRunner seedSubscriptionPlans(
            SubscriptionPlanRepository planRepository) {

        return args -> {

            // Avoids inserting duplicate plans on application restart.
            if (planRepository.count() == 0) {

                planRepository.saveAll(List.of(
                        new SubscriptionPlan(
                                "Basic",
                                new BigDecimal("4.99")),

                        new SubscriptionPlan(
                                "Standard",
                                new BigDecimal("9.99")),

                        new SubscriptionPlan(
                                "Premium",
                                new BigDecimal("14.99"))
                ));
            }
        };
    }
}
