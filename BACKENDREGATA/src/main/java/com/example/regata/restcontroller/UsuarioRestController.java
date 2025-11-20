package com.example.regata.restcontroller;

import com.example.regata.dto.UsuarioDTO;
import com.example.regata.mapper.UsuarioMapper;
import com.example.regata.model.Usuario;
import com.example.regata.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Gestión de Usuarios", description = "API para gestionar usuarios del juego")
public class UsuarioRestController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        try {
            boolean existe = usuarioService.existsByEmail(email);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/contar-por-rol/{rol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> contarUsuariosPorRol(@PathVariable Usuario.Rol rol) {
        try {
            Long count = usuarioService.countByRol(rol);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
