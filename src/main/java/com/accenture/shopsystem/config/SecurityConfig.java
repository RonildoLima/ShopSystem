package com.accenture.shopsystem.config;

import com.accenture.shopsystem.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf (AbstractHttpConfigurer::disable)
                .httpBasic (withDefaults ())
                .formLogin(configurer -> {
                    configurer.loginPage ("/login").permitAll();
                })
                .authorizeHttpRequests (authorize ->{
                    authorize.requestMatchers("/pedidos/{vendedorId}").permitAll();
                    authorize.requestMatchers("/").authenticated();
                    authorize.requestMatchers("/api-docs/**", "/api-docs.html", "/swagger-ui/**").permitAll();
                    authorize.requestMatchers("/user/cadastrar/**").permitAll();
                    authorize.anyRequest().authenticated ();
                })
                .oauth2Login(oauth -> {
                    oauth.loginPage("/login")
                    .defaultSuccessUrl("/oauth2/success", true);
                })
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
