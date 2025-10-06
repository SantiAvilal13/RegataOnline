package com.example.regata.restcontroller;

import com.example.regata.model.Barco;
import com.example.regata.service.BarcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/barcos")
public class BarcoRestController {
    
    @Autowired
    private BarcoService barcoService;
    
    @GetMapping
    public ResponseEntity<List<Barco>> listarBarcos() {
        try {
            List<Barco> barcos = barcoService.findAll();
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Barco> obtenerBarco(@PathVariable UUID id) {
        try {
            Optional<Barco> barco = barcoService.findById(id);
            return barco.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Barco> crearBarco(@RequestBody Barco barco) {
        try {
            Barco savedBarco = barcoService.save(barco);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBarco);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Barco> actualizarBarco(@PathVariable UUID id, @RequestBody Barco barco) {
        try {
            Barco updatedBarco = barcoService.update(id, barco);
            return ResponseEntity.ok(updatedBarco);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBarco(@PathVariable UUID id) {
        try {
            barcoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Barco>> buscarBarcos(@RequestParam String alias) {
        try {
            List<Barco> barcos = barcoService.findByAliasContainingIgnoreCase(alias);
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Barco>> obtenerBarcosPorUsuario(@PathVariable UUID usuarioId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioId(usuarioId);
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/modelo/{modeloId}")
    public ResponseEntity<List<Barco>> obtenerBarcosPorModelo(@PathVariable UUID modeloId) {
        try {
            List<Barco> barcos = barcoService.findByModeloId(modeloId);
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/ordenados")
    public ResponseEntity<List<Barco>> obtenerBarcosPorUsuarioOrdenados(@PathVariable UUID usuarioId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioIdOrderByAliasAsc(usuarioId);
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/con-participaciones")
    public ResponseEntity<List<Barco>> obtenerBarcosConParticipaciones() {
        try {
            List<Barco> barcos = barcoService.findBarcosConParticipaciones();
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/modelo/{modeloId}")
    public ResponseEntity<List<Barco>> obtenerBarcosPorUsuarioYModelo(@PathVariable UUID usuarioId, 
                                                                      @PathVariable UUID modeloId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioAndModelo(usuarioId, modeloId);
            return ResponseEntity.ok(barcos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/contar")
    public ResponseEntity<Long> contarBarcosPorUsuario(@PathVariable UUID usuarioId) {
        try {
            Long count = barcoService.countByUsuarioId(usuarioId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}