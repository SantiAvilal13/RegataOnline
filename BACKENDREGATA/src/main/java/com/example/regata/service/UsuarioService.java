package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Usuario;
import com.example.regata.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new GameException("Usuario no encontrado con ID: " + id));
        
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setEmail(usuario.getEmail());
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().trim().isEmpty()) {
            existingUsuario.setPasswordHash(usuario.getPasswordHash());
        }
        
        existingUsuario.setRol(usuario.getRol());
        
        return usuarioRepository.save(existingUsuario);
    }
    
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new GameException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public List<Usuario> findByNombreContainingIgnoreCase(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Usuario> findAllOrderByNombreAsc() {
        return usuarioRepository.findAllOrderByNombreAsc();
    }
    
    public Long countBarcosByUsuarioId(Long usuarioId) {
        return usuarioRepository.countBarcosByUsuarioId(usuarioId);
    }
    
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    public List<Usuario> findByRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    public List<Usuario> findJugadores() {
        return usuarioRepository.findByRol(Usuario.Rol.JUGADOR);
    }
    
    public List<Usuario> findAdministradores() {
        return usuarioRepository.findByRol(Usuario.Rol.ADMIN);
    }
    
    public Long countByRol(Usuario.Rol rol) {
        return usuarioRepository.countByRol(rol);
    }
    
    public Long countPartidasGanadasByUsuarioId(Long usuarioId) {
        return usuarioRepository.countPartidasGanadasByUsuarioId(usuarioId);
    }
}
