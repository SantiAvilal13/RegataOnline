package com.example.regata.restcontroller;

import com.example.regata.dto.MovimientoDTO;
import com.example.regata.model.Movimiento;
import com.example.regata.service.MovimientoService;
import com.example.regata.mapper.MovimientoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoRestController {
    
    @Autowired
    private MovimientoService movimientoService;
    
    @Autowired
    private MovimientoMapper movimientoMapper;
    
    // CRUD básico
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> findAll() {
        List<Movimiento> movimientos = movimientoService.findAll();
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> findById(@PathVariable Long id) {
        Optional<Movimiento> movimiento = movimientoService.findById(id);
        if (movimiento.isPresent()) {
            MovimientoDTO dto = movimientoMapper.toDTO(movimiento.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            movimientoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Búsquedas específicas
    @GetMapping("/participacion/{participacionId}")
    public ResponseEntity<List<MovimientoDTO>> findByParticipacionId(@PathVariable Long participacionId) {
        List<Movimiento> movimientos = movimientoService.findByParticipacionId(participacionId);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/participacion/{participacionId}/turno/{turno}")
    public ResponseEntity<MovimientoDTO> findByParticipacionIdAndTurno(
            @PathVariable Long participacionId, 
            @PathVariable Integer turno) {
        Optional<Movimiento> movimiento = movimientoService.findByParticipacionIdAndTurno(participacionId, turno);
        if (movimiento.isPresent()) {
            MovimientoDTO dto = movimientoMapper.toDTO(movimiento.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/participacion/{participacionId}/rango/{turnoInicio}/{turnoFin}")
    public ResponseEntity<List<MovimientoDTO>> findByParticipacionIdAndTurnoBetween(
            @PathVariable Long participacionId,
            @PathVariable Integer turnoInicio,
            @PathVariable Integer turnoFin) {
        List<Movimiento> movimientos = movimientoService.findByParticipacionIdAndTurnoBetween(
            participacionId, turnoInicio, turnoFin);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // QUERIES CLAVE: Estado actual y historial
    @GetMapping("/participacion/{participacionId}/estado-actual")
    public ResponseEntity<MovimientoDTO> obtenerEstadoActual(@PathVariable Long participacionId) {
        Optional<Movimiento> movimiento = movimientoService.obtenerEstadoActual(participacionId);
        if (movimiento.isPresent()) {
            MovimientoDTO dto = movimientoMapper.toDTO(movimiento.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/participacion/{participacionId}/historial")
    public ResponseEntity<List<MovimientoDTO>> obtenerHistorialCompleto(@PathVariable Long participacionId) {
        List<Movimiento> movimientos = movimientoService.obtenerHistorialCompleto(participacionId);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // Búsquedas por resultado
    @GetMapping("/colisiones")
    public ResponseEntity<List<MovimientoDTO>> findMovimientosConColision() {
        List<Movimiento> movimientos = movimientoService.findMovimientosConColision();
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/participacion/{participacionId}/colisiones")
    public ResponseEntity<List<MovimientoDTO>> findMovimientosConColisionByParticipacionId(@PathVariable Long participacionId) {
        List<Movimiento> movimientos = movimientoService.findMovimientosConColisionByParticipacionId(participacionId);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/resultado/{resultado}")
    public ResponseEntity<List<MovimientoDTO>> findByResultado(@PathVariable Movimiento.Resultado resultado) {
        List<Movimiento> movimientos = movimientoService.findByResultado(resultado);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/participacion/{participacionId}/resultado/{resultado}")
    public ResponseEntity<List<MovimientoDTO>> findByParticipacionIdAndResultado(
            @PathVariable Long participacionId,
            @PathVariable Movimiento.Resultado resultado) {
        List<Movimiento> movimientos = movimientoService.findByParticipacionIdAndResultado(participacionId, resultado);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // Búsquedas por posición y velocidad
    @GetMapping("/participacion/{participacionId}/posicion/{posX}/{posY}")
    public ResponseEntity<List<MovimientoDTO>> findByParticipacionIdAndPosicion(
            @PathVariable Long participacionId,
            @PathVariable Integer posX,
            @PathVariable Integer posY) {
        List<Movimiento> movimientos = movimientoService.findByParticipacionIdAndPosicion(participacionId, posX, posY);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/participacion/{participacionId}/velocidad/{velX}/{velY}")
    public ResponseEntity<List<MovimientoDTO>> findByParticipacionIdAndVelocidad(
            @PathVariable Long participacionId,
            @PathVariable Integer velX,
            @PathVariable Integer velY) {
        List<Movimiento> movimientos = movimientoService.findByParticipacionIdAndVelocidad(participacionId, velX, velY);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/delta-velocidad/{deltaVx}/{deltaVy}")
    public ResponseEntity<List<MovimientoDTO>> findByDeltaVelocidad(
            @PathVariable Integer deltaVx,
            @PathVariable Integer deltaVy) {
        List<Movimiento> movimientos = movimientoService.findByDeltaVelocidad(deltaVx, deltaVy);
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // ENDPOINT CLAVE: Realizar movimiento
    @PostMapping("/participacion/{participacionId}/mover")
    public ResponseEntity<MovimientoDTO> realizarMovimiento(
            @PathVariable Long participacionId,
            @RequestParam Integer deltaVx,
            @RequestParam Integer deltaVy) {
        try {
            Movimiento movimiento = movimientoService.realizarMovimiento(participacionId, deltaVx, deltaVy);
            MovimientoDTO dto = movimientoMapper.toDTO(movimiento);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Información adicional
    @GetMapping("/participacion/{participacionId}/siguiente-turno")
    public ResponseEntity<Integer> getSiguienteTurno(@PathVariable Long participacionId) {
        Integer siguienteTurno = movimientoService.getSiguienteTurno(participacionId);
        return ResponseEntity.ok(siguienteTurno);
    }
    
    @GetMapping("/participacion/{participacionId}/count")
    public ResponseEntity<Long> countByParticipacionId(@PathVariable Long participacionId) {
        Long count = movimientoService.countByParticipacionId(participacionId);
        return ResponseEntity.ok(count);
    }
}
