package com.example.regata.restcontroller;

import com.example.regata.model.Barco;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.service.BarcoService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeRestController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            List<Barco> barcos = barcoService.findAll();
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalUsuarios", usuarios.size());
            estadisticas.put("totalModelos", modelos.size());
            estadisticas.put("totalBarcos", barcos.size());
            
            // Top 5 usuarios
            List<Usuario> topUsuarios = usuarios;
            if (topUsuarios.size() > 5) {
                topUsuarios = topUsuarios.subList(0, 5);
            }
            estadisticas.put("topUsuarios", topUsuarios);
            
            // Top 5 barcos
            List<Barco> topBarcos = barcos;
            if (topBarcos.size() > 5) {
                topBarcos = topBarcos.subList(0, 5);
            }
            estadisticas.put("topBarcos", topBarcos);
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/home/usuarios")
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/home/modelos")
    public ResponseEntity<List<Modelo>> obtenerModelos() {
        try {
            List<Modelo> modelos = modeloService.findAll();
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/home/barcos")
    public ResponseEntity<List<Barco>> obtenerBarcos() {
        try {
            List<Barco> barcos = barcoService.findAll();
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/test-db")
    public ResponseEntity<Map<String, String>> testDatabase() {
        try {
            Usuario testUsuario = Usuario.builder()
                    .nombre("Test Usuario " + System.currentTimeMillis())
                    .email("test" + System.currentTimeMillis() + "@test.com")
                    .passwordHash("password123")
                    .rol(Usuario.Rol.JUGADOR)
                    .build();
            Usuario saved = usuarioService.save(testUsuario);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario de prueba creado: " + saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error en test: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/test-simple")
    public ResponseEntity<Map<String, String>> testSimple() {
        try {
            Usuario testUsuario = new Usuario();
            testUsuario.setNombre("Test Simple " + System.currentTimeMillis());
            testUsuario.setEmail("simple" + System.currentTimeMillis() + "@test.com");
            testUsuario.setPasswordHash("password123");
            testUsuario.setRol(Usuario.Rol.JUGADOR);
            
            Usuario saved = usuarioService.save(testUsuario);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario guardado en test simple: " + saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error en test simple: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/test-crud")
    public ResponseEntity<Map<String, String>> testCrud() {
        try {
            // Crear usuario de prueba
            Usuario usuario = new Usuario();
            usuario.setNombre("Test CRUD " + System.currentTimeMillis());
            usuario.setEmail("crud" + System.currentTimeMillis() + "@test.com");
            usuario.setPasswordHash("password123");
            usuario.setRol(Usuario.Rol.JUGADOR);
            
            Usuario savedUsuario = usuarioService.save(usuario);
            
            // Crear modelo de prueba
            Modelo modelo = new Modelo();
            modelo.setNombre("Test Modelo " + System.currentTimeMillis());
            modelo.setColorHex("#0000FF");
            
            Modelo savedModelo = modeloService.save(modelo);
            
            // Crear barco de prueba
            Barco barco = new Barco();
            barco.setAlias("Test Barco " + System.currentTimeMillis());
            barco.setUsuario(savedUsuario);
            barco.setModelo(savedModelo);
            
            Barco savedBarco = barcoService.save(barco);
            
            Map<String, String> response = new HashMap<>();
            response.put("success", "CRUD test completado exitosamente");
            response.put("usuario", savedUsuario.toString());
            response.put("modelo", savedModelo.toString());
            response.put("barco", savedBarco.toString());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error en test CRUD: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}