package com.academicrepo.back.academic_repo.auth.infrastructure.config;

import com.academicrepo.back.academic_repo.auth.application.services.CustomUserDetailsService;
import com.academicrepo.back.academic_repo.auth.infrastructure.security.JwtAccessDeniedHandler;
import com.academicrepo.back.academic_repo.auth.infrastructure.security.JwtAuthenticationEntryPoint;
import com.academicrepo.back.academic_repo.auth.infrastructure.security.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    private static final String[] PUBLIC_ENDPOINTS = {
        "/v1/auth/**",
        "/v1/public/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/scalar/**",
        "/scalar.html",
        "/api-docs/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/webjars/**",
        "/",
        "/error",
        "/actuator/health"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exception ->
                                exception
                                        .authenticationEntryPoint(authenticationEntryPoint)
                                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                                        .permitAll()
                                        .requestMatchers(PUBLIC_ENDPOINTS)
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/v1/users")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        config.setAllowedHeaders(
                Arrays.asList(
                        "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
