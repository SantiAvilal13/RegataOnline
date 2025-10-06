package com.example.regata.restcontroller;

import com.example.regata.dto.ModeloDTO;
import com.example.regata.mapper.ModeloMapper;
import com.example.regata.model.Modelo;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/modelos")
public class ModeloRestController {
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private ModeloMapper modeloMapper;
    
    @GetMapping
    public ResponseEntity<List<ModeloDTO>> listarModelos() {
        try {
            List<Modelo> modelos = modeloService.findAll();
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> obtenerModelo(@PathVariable UUID id) {
        try {
            Optional<Modelo> modelo = modeloService.findById(id);
            return modelo.map(modeloMapper::toDTO)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ModeloDTO> crearModelo(@RequestBody ModeloDTO modeloDTO) {
        try {
            Modelo modelo = modeloMapper.toEntity(modeloDTO);
            Modelo savedModelo = modeloService.save(modelo);
            ModeloDTO savedModeloDTO = modeloMapper.toDTO(savedModelo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedModeloDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ModeloDTO> actualizarModelo(@PathVariable UUID id, @RequestBody ModeloDTO modeloDTO) {
        try {
            Modelo modelo = modeloMapper.toEntity(modeloDTO);
            Modelo updatedModelo = modeloService.update(id, modelo);
            ModeloDTO updatedModeloDTO = modeloMapper.toDTO(updatedModelo);
            return ResponseEntity.ok(updatedModeloDTO);
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
    public ResponseEntity<List<ModeloDTO>> buscarModelos(@RequestParam String nombre) {
        try {
            List<Modelo> modelos = modeloService.findByNombreContaining(nombre);
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/color/{colorHex}")
    public ResponseEntity<List<ModeloDTO>> obtenerModelosPorColor(@PathVariable String colorHex) {
        try {
            List<Modelo> modelos = modeloService.findByColorHex(colorHex);
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/color-buscar")
    public ResponseEntity<List<ModeloDTO>> buscarModelosPorColor(@RequestParam String color) {
        try {
            List<Modelo> modelos = modeloService.findByColorHexContainingIgnoreCase(color);
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/ordenados")
    public ResponseEntity<List<ModeloDTO>> obtenerModelosOrdenados() {
        try {
            List<Modelo> modelos = modeloService.findAllOrderByNombreAsc();
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/populares")
    public ResponseEntity<List<ModeloDTO>> obtenerModelosPopulares() {
        try {
            List<Modelo> modelos = modeloService.findModelosMasPopulares();
            List<ModeloDTO> modelosDTO = modelos.stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(modelosDTO);
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