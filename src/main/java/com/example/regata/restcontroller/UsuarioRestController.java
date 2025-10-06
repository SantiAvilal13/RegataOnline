package com.example.regata.restcontroller;

import com.example.regata.dto.UsuarioDTO;
import com.example.regata.mapper.UsuarioMapper;
import com.example.regata.model.Usuario;
import com.example.regata.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable UUID id) {
        try {
            Optional<Usuario> usuario = usuarioService.findById(id);
            return usuario.map(usuarioMapper::toDTO)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
            Usuario savedUsuario = usuarioService.save(usuario);
            UsuarioDTO savedUsuarioDTO = usuarioMapper.toDTO(savedUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable UUID id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
            Usuario updatedUsuario = usuarioService.update(id, usuario);
            UsuarioDTO updatedUsuarioDTO = usuarioMapper.toDTO(updatedUsuario);
            return ResponseEntity.ok(updatedUsuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable UUID id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuarios(@RequestParam String nombre) {
        try {
            List<Usuario> usuarios = usuarioService.findByNombreContainingIgnoreCase(nombre);
            List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRol(@PathVariable Usuario.Rol rol) {
        try {
            List<Usuario> usuarios = usuarioService.findByRol(rol);
            List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/jugadores")
    public ResponseEntity<List<UsuarioDTO>> obtenerJugadores() {
        try {
            List<Usuario> jugadores = usuarioService.findJugadores();
            List<UsuarioDTO> jugadoresDTO = jugadores.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(jugadoresDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/administradores")
    public ResponseEntity<List<UsuarioDTO>> obtenerAdministradores() {
        try {
            List<Usuario> administradores = usuarioService.findAdministradores();
            List<UsuarioDTO> administradoresDTO = administradores.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(administradoresDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorEmail(@PathVariable String email) {
        try {
            Optional<Usuario> usuario = usuarioService.findByEmail(email);
            return usuario.map(usuarioMapper::toDTO)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/existe-email/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        try {
            boolean existe = usuarioService.existsByEmail(email);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/contar-por-rol/{rol}")
    public ResponseEntity<Long> contarUsuariosPorRol(@PathVariable Usuario.Rol rol) {
        try {
            Long count = usuarioService.countByRol(rol);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
