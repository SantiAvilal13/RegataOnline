package com.example.regata.restcontroller;

import com.example.regata.model.Modelo;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/modelos")
public class ModeloRestController {
    
    @Autowired
    private ModeloService modeloService;
    
    @GetMapping
    public ResponseEntity<List<Modelo>> listarModelos() {
        try {
            List<Modelo> modelos = modeloService.findAll();
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> obtenerModelo(@PathVariable UUID id) {
        try {
            Optional<Modelo> modelo = modeloService.findById(id);
            return modelo.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Modelo> crearModelo(@RequestBody Modelo modelo) {
        try {
            Modelo savedModelo = modeloService.save(modelo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedModelo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Modelo> actualizarModelo(@PathVariable UUID id, @RequestBody Modelo modelo) {
        try {
            Modelo updatedModelo = modeloService.update(id, modelo);
            return ResponseEntity.ok(updatedModelo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarModelo(@PathVariable UUID id) {
        try {
            modeloService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Modelo>> buscarModelos(@RequestParam String nombre) {
        try {
            List<Modelo> modelos = modeloService.findByNombreContaining(nombre);
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/color/{colorHex}")
    public ResponseEntity<List<Modelo>> obtenerModelosPorColor(@PathVariable String colorHex) {
        try {
            List<Modelo> modelos = modeloService.findByColorHex(colorHex);
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/color-buscar")
    public ResponseEntity<List<Modelo>> buscarModelosPorColor(@RequestParam String color) {
        try {
            List<Modelo> modelos = modeloService.findByColorHexContainingIgnoreCase(color);
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/ordenados")
    public ResponseEntity<List<Modelo>> obtenerModelosOrdenados() {
        try {
            List<Modelo> modelos = modeloService.findAllOrderByNombreAsc();
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/populares")
    public ResponseEntity<List<Modelo>> obtenerModelosPopulares() {
        try {
            List<Modelo> modelos = modeloService.findModelosMasPopulares();
            return ResponseEntity.ok(modelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}/barcos")
    public ResponseEntity<Long> contarBarcosPorModelo(@PathVariable UUID id) {
        try {
            Long count = modeloService.countBarcosByModeloId(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}