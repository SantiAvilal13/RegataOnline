package com.example.regata.restcontroller;

import com.example.regata.dto.auth.AuthResponse;
import com.example.regata.dto.auth.LoginRequest;
import com.example.regata.dto.auth.SignupRequest;
import com.example.regata.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación siguiendo el patrón del ejemplo del profesor
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * Endpoint para registro de nuevos usuarios
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }
    
    /**
     * Endpoint para login de usuarios
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
