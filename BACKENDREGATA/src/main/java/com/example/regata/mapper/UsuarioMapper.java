package com.example.regata.mapper;

import com.example.regata.dto.UsuarioDTO;
import com.example.regata.model.Usuario;
import com.example.regata.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    
    @Autowired
    private UsuarioService usuarioService;
    
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        
        // Calcular estadísticas
        dto.setTotalPartidasGanadas(usuarioService.countPartidasGanadasByUsuarioId(usuario.getIdUsuario()));
        dto.setTotalBarcos(usuarioService.countBarcosByUsuarioId(usuario.getIdUsuario()));
        
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
