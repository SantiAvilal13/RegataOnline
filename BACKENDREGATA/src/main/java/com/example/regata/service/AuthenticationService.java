package com.example.regata.service;

import com.example.regata.dto.auth.AuthResponse;
import com.example.regata.dto.auth.LoginRequest;
import com.example.regata.dto.auth.SignupRequest;
import com.example.regata.model.Usuario;
import com.example.regata.repository.UsuarioRepository;
import com.example.regata.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación siguiendo el patrón del ejemplo del profesor
 */
@Service
public class AuthenticationService {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * Registro de nuevo usuario
     */
    public AuthResponse signup(SignupRequest request) {
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol() != null ? request.getRol() : Usuario.Rol.JUGADOR)
                .build();
        
        usuarioRepository.save(usuario);
        String jwt = jwtService.generateToken(usuario.getUsername());
        
        return AuthResponse.builder()
                .token(jwt)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .idUsuario(usuario.getIdUsuario())
                .build();
    }
    
    /**
     * Login de usuario existente
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña inválidos."));
        
        String jwt = jwtService.generateToken(usuario.getUsername());
        
        return AuthResponse.builder()
                .token(jwt)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .idUsuario(usuario.getIdUsuario())
                .build();
    }
}
