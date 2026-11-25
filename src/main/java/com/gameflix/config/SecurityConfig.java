package com.gameflix.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(
                            List.of("http://localhost:5173")
                    );

                    config.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    );

                    config.setAllowedHeaders(
                            List.of("Authorization", "Content-Type")
                    );

                    return config;
                }))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                                // Allows public access to the game catalog.
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/games",
                                        "/api/games/**"
                                ).permitAll()

                                // Allows visitors to browse subscription plans.
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/subscription-plans"
                                ).permitAll()

                        // Restrict administrative endpoints to administrator accounts.
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers("/error").permitAll()

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exceptions -> exceptions

                        // Return 401 when authentication is missing or invalid.
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\":\"Unauthorized\"}"
                            );
                        })

                        // Return 403 when the user lacks the required permissions.
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\":\"Access denied\"}"
                            );
                        })
                )

                .httpBasic(basic -> basic.disable())

                .formLogin(form -> form.disable())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
