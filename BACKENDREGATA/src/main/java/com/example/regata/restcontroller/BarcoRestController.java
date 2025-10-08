package com.example.regata.restcontroller;

import com.example.regata.dto.BarcoDTO;
import com.example.regata.mapper.BarcoMapper;
import com.example.regata.model.Barco;
import com.example.regata.service.BarcoService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barcos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Gestión de Barcos", description = "API para gestionar barcos del juego")
public class BarcoRestController {
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private BarcoMapper barcoMapper;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    @GetMapping
    @Operation(summary = "Listar todos los barcos", description = "Obtiene una lista de todos los barcos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de barcos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BarcoDTO>> listarBarcos() {
        try {
            List<Barco> barcos = barcoService.findAll();
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener barco por ID", description = "Obtiene un barco específico por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BarcoDTO> obtenerBarco(
            @Parameter(description = "ID único del barco", required = true) @PathVariable Long id) {
        try {
            Optional<Barco> barco = barcoService.findById(id);
            return barco.map(barcoMapper::toDTO)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo barco", description = "Crea un nuevo barco en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Barco creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BarcoDTO> crearBarco(
            @Parameter(description = "Datos del barco a crear", required = true) @RequestBody BarcoDTO barcoDTO) {
        try {
            Barco barco = new Barco();
            barco.setAlias(barcoDTO.getAlias());
            
            // Cargar usuario y modelo desde los IDs
            if (barcoDTO.getUsuarioId() != null) {
                barco.setUsuario(usuarioService.findById(barcoDTO.getUsuarioId()).orElse(null));
            }
            if (barcoDTO.getModeloId() != null) {
                barco.setModelo(modeloService.findById(barcoDTO.getModeloId()).orElse(null));
            }
            
            Barco savedBarco = barcoService.save(barco);
            BarcoDTO savedBarcoDTO = barcoMapper.toDTO(savedBarco);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBarcoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BarcoDTO> actualizarBarco(@PathVariable Long id, @RequestBody BarcoDTO barcoDTO) {
        try {
            Barco barco = new Barco();
            barco.setIdBarco(id);
            barco.setAlias(barcoDTO.getAlias());
            
            // Cargar usuario y modelo desde los IDs
            if (barcoDTO.getUsuarioId() != null) {
                barco.setUsuario(usuarioService.findById(barcoDTO.getUsuarioId()).orElse(null));
            }
            if (barcoDTO.getModeloId() != null) {
                barco.setModelo(modeloService.findById(barcoDTO.getModeloId()).orElse(null));
            }
            
            Barco updatedBarco = barcoService.update(id, barco);
            BarcoDTO updatedBarcoDTO = barcoMapper.toDTO(updatedBarco);
            return ResponseEntity.ok(updatedBarcoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar barco", description = "Elimina un barco del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Barco eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarBarco(
            @Parameter(description = "ID único del barco a eliminar", required = true) @PathVariable Long id) {
        try {
            barcoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<BarcoDTO>> buscarBarcos(@RequestParam String alias) {
        try {
            List<Barco> barcos = barcoService.findByAliasContainingIgnoreCase(alias);
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioId(usuarioId);
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/modelo/{modeloId}")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosPorModelo(@PathVariable Long modeloId) {
        try {
            List<Barco> barcos = barcoService.findByModeloId(modeloId);
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/ordenados")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosPorUsuarioOrdenados(@PathVariable Long usuarioId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioIdOrderByAliasAsc(usuarioId);
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/con-participaciones")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosConParticipaciones() {
        try {
            List<Barco> barcos = barcoService.findBarcosConParticipaciones();
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/modelo/{modeloId}")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosPorUsuarioYModelo(@PathVariable Long usuarioId, 
                                                                     @PathVariable Long modeloId) {
        try {
            List<Barco> barcos = barcoService.findByUsuarioAndModelo(usuarioId, modeloId);
            List<BarcoDTO> barcosDTO = barcos.stream()
                .map(barcoMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(barcosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}/contar")
    public ResponseEntity<Long> contarBarcosPorUsuario(@PathVariable Long usuarioId) {
        try {
            Long count = barcoService.countByUsuarioId(usuarioId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}