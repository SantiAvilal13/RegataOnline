package com.example.regata.restcontroller;

import com.example.regata.dto.ParticipacionDTO;
import com.example.regata.dto.EstadoActualDTO;
import com.example.regata.model.Participacion;
import com.example.regata.model.Movimiento;
import com.example.regata.service.ParticipacionService;
import com.example.regata.service.MovimientoService;
import com.example.regata.mapper.ParticipacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participaciones")
@CrossOrigin(origins = "*")
@PreAuthorize("isAuthenticated()")
public class ParticipacionRestController {
    
    @Autowired
    private ParticipacionService participacionService;
    
    @Autowired
    private MovimientoService movimientoService;
    
    @Autowired
    private ParticipacionMapper participacionMapper;
    
    // CRUD básico
    @GetMapping
    public ResponseEntity<List<ParticipacionDTO>> findAll() {
        List<Participacion> participaciones = participacionService.findAll();
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParticipacionDTO> findById(@PathVariable Long id) {
        Optional<Participacion> participacion = participacionService.findById(id);
        if (participacion.isPresent()) {
            ParticipacionDTO dto = participacionMapper.toDTO(participacion.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/partida/{partidaId}")
    public ResponseEntity<List<ParticipacionDTO>> findByPartidaId(@PathVariable Long partidaId) {
        List<Participacion> participaciones = participacionService.findByPartidaId(partidaId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            participacionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Búsquedas específicas
    
    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<ParticipacionDTO>> findByJugadorId(@PathVariable Long jugadorId) {
        List<Participacion> participaciones = participacionService.findByJugadorId(jugadorId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/barco/{barcoId}")
    public ResponseEntity<List<ParticipacionDTO>> findByBarcoId(@PathVariable Long barcoId) {
        List<Participacion> participaciones = participacionService.findByBarcoId(barcoId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ParticipacionDTO>> findByEstado(@PathVariable Participacion.Estado estado) {
        List<Participacion> participaciones = participacionService.findByEstado(estado);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // Búsquedas específicas de partida
    @GetMapping("/partida/{partidaId}/activas")
    public ResponseEntity<List<ParticipacionDTO>> findParticipacionesActivasByPartidaId(@PathVariable Long partidaId) {
        List<Participacion> participaciones = participacionService.findParticipacionesActivasByPartidaId(partidaId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/partida/{partidaId}/jugador/{jugadorId}")
    public ResponseEntity<ParticipacionDTO> findByPartidaIdAndJugadorId(
            @PathVariable Long partidaId, 
            @PathVariable Long jugadorId) {
        Optional<Participacion> participacion = participacionService.findByPartidaIdAndJugadorId(partidaId, jugadorId);
        if (participacion.isPresent()) {
            ParticipacionDTO dto = participacionMapper.toDTO(participacion.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/partida/{partidaId}/barco/{barcoId}")
    public ResponseEntity<ParticipacionDTO> findByPartidaIdAndBarcoId(
            @PathVariable Long partidaId, 
            @PathVariable Long barcoId) {
        Optional<Participacion> participacion = participacionService.findByPartidaIdAndBarcoId(partidaId, barcoId);
        if (participacion.isPresent()) {
            ParticipacionDTO dto = participacionMapper.toDTO(participacion.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/partida/{partidaId}/orden")
    public ResponseEntity<List<ParticipacionDTO>> findByPartidaIdOrderByOrdenTurnoAsc(@PathVariable Long partidaId) {
        List<Participacion> participaciones = participacionService.findByPartidaIdOrderByOrdenTurnoAsc(partidaId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/partida/{partidaId}/meta")
    public ResponseEntity<List<ParticipacionDTO>> findParticipacionesEnMetaByPartidaId(@PathVariable Long partidaId) {
        List<Participacion> participaciones = participacionService.findParticipacionesEnMetaByPartidaId(partidaId);
        List<ParticipacionDTO> dtos = participaciones.stream()
                .map(participacionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    // ENDPOINT CLAVE: Obtener estado actual de una participación
    @GetMapping("/{id}/estado-actual")
    public ResponseEntity<EstadoActualDTO> obtenerEstadoActual(@PathVariable Long id) {
        Optional<Participacion> participacion = participacionService.findById(id);
        if (participacion.isPresent()) {
            Optional<Movimiento> ultimoMovimiento = movimientoService.obtenerEstadoActual(id);
            EstadoActualDTO dto = participacionMapper.toEstadoActualDTO(
                participacion.get(), 
                ultimoMovimiento.orElse(null)
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Métodos de negocio
    @PostMapping("/{id}/destruir")
    public ResponseEntity<ParticipacionDTO> destruirParticipacion(@PathVariable Long id) {
        try {
            participacionService.destruirParticipacion(id);
            Optional<Participacion> participacion = participacionService.findById(id);
            if (participacion.isPresent()) {
                ParticipacionDTO dto = participacionMapper.toDTO(participacion.get());
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/llegar-meta")
    public ResponseEntity<ParticipacionDTO> marcarComoEnMeta(@PathVariable Long id) {
        try {
            participacionService.marcarComoEnMeta(id);
            Optional<Participacion> participacion = participacionService.findById(id);
            if (participacion.isPresent()) {
                ParticipacionDTO dto = participacionMapper.toDTO(participacion.get());
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Validaciones
    @GetMapping("/partida/{partidaId}/jugador/{jugadorId}/exists")
    public ResponseEntity<Boolean> existsByPartidaIdAndJugadorId(
            @PathVariable Long partidaId, 
            @PathVariable Long jugadorId) {
        boolean exists = participacionService.existsByPartidaIdAndJugadorId(partidaId, jugadorId);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/partida/{partidaId}/barco/{barcoId}/exists")
    public ResponseEntity<Boolean> existsByPartidaIdAndBarcoId(
            @PathVariable Long partidaId, 
            @PathVariable Long barcoId) {
        boolean exists = participacionService.existsByPartidaIdAndBarcoId(partidaId, barcoId);
        return ResponseEntity.ok(exists);
    }
    
    // Contadores
    @GetMapping("/partida/{partidaId}/estado/{estado}/count")
    public ResponseEntity<Long> countByPartidaIdAndEstado(
            @PathVariable Long partidaId, 
            @PathVariable Participacion.Estado estado) {
        Long count = participacionService.countByPartidaIdAndEstado(partidaId, estado);
        return ResponseEntity.ok(count);
    }
}
