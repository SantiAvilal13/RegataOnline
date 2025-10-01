package com.regataonline.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF para simplificar el desarrollo
            .csrf().disable()
            
            // Permitir acceso a todas las rutas sin autenticación
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            
            // Desactivar el formulario de login por defecto
            .formLogin().disable()
            
            // Desactivar autenticación HTTP básica
            .httpBasic().disable()
            
            // Desactivar logout
            .logout().disable();
            
        return http.build();
    }
}
