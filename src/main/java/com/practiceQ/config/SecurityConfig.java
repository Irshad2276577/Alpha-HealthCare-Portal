package com.practiceQ.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class SecurityConfig {

   @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable();
      http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
     http.authorizeHttpRequests()
             .requestMatchers("/api/patient/signup","/api/patient/sign-in","/api/admin/signup","/api/admin/sign-in","/api/doctor/sign-in").permitAll()
             .requestMatchers("/api/patient/**")
             .hasRole("USER")
             .requestMatchers(
                     "/api/admin/**",
                     "/api/department/**",
                     "/api/hospitals/**",
                     "/api/services/**",
                     "/api/invoices/**",
                     "/api/staff/**",
                     "/api/rooms/**",
                     "/api/prescriptions/**").hasRole("ADMIN")
             .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
             .anyRequest()
             .authenticated();

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
