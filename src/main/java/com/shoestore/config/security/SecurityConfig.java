package com.shoestore.config.security;

import com.shoestore.auth.entity.User;
import com.shoestore.auth.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
     @Bean
    public JwtService jwtService(){
      return new JwtService();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailService userDetailService
    ){
         return new JwtAuthenticationFilter(jwtService,userDetailService);
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwtfilter) throws Exception{
         http.csrf(csrf-> csrf.disable())
                 .sessionManagement(sessionManagemnet-> sessionManagemnet.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(auth -> auth.
                         requestMatchers("/auth/**").permitAll()
                         .requestMatchers(
                                 "/v3/api-docs/**",
                                 "/swagger-ui/**",
                                 "/swagger-ui.html"
                         ).permitAll()
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .requestMatchers("/cart/**", "/orders/**").hasRole("USER")
                         .anyRequest().authenticated()
                   ).addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }
}
