package com.example.regata.mapper;

import com.example.regata.dto.UsuarioDTO;
import com.example.regata.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        
        // No incluimos password por seguridad
        // No incluimos relaciones para evitar referencias circulares
        
        return dto;
    }
    
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPasswordHash(dto.getPassword());
        usuario.setRol(dto.getRol());
        
        return usuario;
    }
}
