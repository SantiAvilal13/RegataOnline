package com.example.regata.dto.auth;

import com.example.regata.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private String email;
    private String nombre;
    private Usuario.Rol rol;
    private Long idUsuario;
}
