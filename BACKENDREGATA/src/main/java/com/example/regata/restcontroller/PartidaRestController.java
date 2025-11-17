package com.example.regata.restcontroller;

import com.example.regata.dto.PartidaDTO;
import com.example.regata.mapper.PartidaMapper;
import com.example.regata.mapper.MovimientoMapper;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.model.Celda;
import com.example.regata.model.Participacion;
import com.example.regata.model.Movimiento;
import com.example.regata.service.PartidaService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.BarcoService;
import com.example.regata.service.CeldaService;
import com.example.regata.service.ParticipacionService;
import com.example.regata.service.MovimientoService;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Gestión de Partidas", description = "API para gestionar partidas del juego")
public class PartidaRestController {
    
        @Autowired
        private PartidaService partidaService;

        @Autowired
        private PartidaMapper partidaMapper;
        
        @Autowired
        private UsuarioService usuarioService;
        
        @Autowired
        private BarcoService barcoService;
        
        @Autowired
        private CeldaService celdaService;
        
        @Autowired
        private ParticipacionService participacionService;
        
        @Autowired
        private MovimientoService movimientoService;
        
        @Autowired
        private MovimientoMapper movimientoMapper;

    @GetMapping
    @Operation(summary = "Listar todas las partidas", description = "Obtiene una lista de todas las partidas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de partidas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PartidaDTO>> listarPartidas() {
        try {
            List<Partida> partidas = partidaService.findAll();
            List<PartidaDTO> partidasDTO = partidas.stream()
                .map(partidaMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(partidasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener partida por ID", description = "Obtiene una partida específica por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PartidaDTO> obtenerPartida(
            @Parameter(description = "ID único de la partida", required = true) @PathVariable Long id) {
        try {
            Optional<Partida> partida = partidaService.findById(id);
            return partida.map(partidaMapper::toDTO)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva partida", description = "Crea una nueva partida en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PartidaDTO> crearPartida(
            @Parameter(description = "Datos de la partida a crear", required = true) @RequestBody PartidaDTO partidaDTO) {
        try {
            Partida partida = partidaMapper.toEntity(partidaDTO);
            Partida savedPartida = partidaService.save(partida);
            PartidaDTO savedPartidaDTO = partidaMapper.toDTO(savedPartida);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPartidaDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/crear-y-jugar")
    @Operation(summary = "Crear partida y obtener participación", description = "Crea una nueva partida y retorna información para jugar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearPartidaYJugar(
            @Parameter(description = "Datos de la partida a crear", required = true) @RequestBody PartidaDTO partidaDTO) {
        try {
            // Crear la partida
            Partida partida = partidaMapper.toEntity(partidaDTO);
            Partida savedPartida = partidaService.save(partida);
            
            // Obtener el primer usuario disponible (JUGADOR)
            Usuario jugador = usuarioService.findAll().stream()
                    .filter(u -> u.getRol() == Usuario.Rol.JUGADOR)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay usuarios jugadores disponibles"));
            
            // Obtener el primer barco disponible del jugador
            Barco barco = barcoService.findAll().stream()
                    .filter(b -> b.getUsuario().getIdUsuario().equals(jugador.getIdUsuario()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay barcos disponibles para el jugador"));
            
            // Encontrar la celda de partida del mapa
            Celda celdaPartida = celdaService.findByMapaAndTipo(savedPartida.getMapa(), Celda.Tipo.PARTIDA)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró celda de partida en el mapa"));
            
            // Crear participación automática
            Participacion participacion = participacionService.crearParticipacion(
                    savedPartida, 
                    jugador, 
                    barco, 
                    celdaPartida, 
                    1 // Primer turno
            );
            
            // Cambiar estado de la partida a EN_JUEGO
            savedPartida.iniciarPartida();
            partidaService.save(savedPartida);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "partidaId", savedPartida.getIdPartida(),
                "participacionId", participacion.getIdParticipacion(),
                "mapaId", savedPartida.getMapa().getIdMapa(),
                "redirectTo", "/game/participacion/" + participacion.getIdParticipacion()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar partida existente", description = "Actualiza una partida existente en el sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PartidaDTO> actualizarPartida(
            @Parameter(description = "ID único de la partida a actualizar", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la partida", required = true) @RequestBody PartidaDTO partidaDTO) {
        try {
            Partida partida = partidaMapper.toEntity(partidaDTO);
            partida.setIdPartida(id);
            Partida updatedPartida = partidaService.update(id, partida);
            PartidaDTO updatedPartidaDTO = partidaMapper.toDTO(updatedPartida);
            return ResponseEntity.ok(updatedPartidaDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar partida", description = "Elimina una partida del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Partida eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarPartida(
            @Parameter(description = "ID único de la partida a eliminar", required = true) @PathVariable Long id) {
        try {
            partidaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== ENDPOINTS MULTIJUGADOR ====================

    @GetMapping("/disponibles")
    @Operation(summary = "Listar partidas disponibles para unirse", 
               description = "Obtiene partidas en estado ESPERANDO que permiten nuevos jugadores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PartidaDTO>> listarPartidasDisponibles() {
        try {
            List<Partida> partidas = partidaService.findAll().stream()
                .filter(p -> p.getEstado() == Partida.Estado.ESPERANDO)
                .collect(Collectors.toList());
            
            List<PartidaDTO> partidasDTO = partidas.stream()
                .map(partidaMapper::toDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(partidasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{partidaId}/info-jugadores")
    @Operation(summary = "Obtener información de jugadores en la partida", 
               description = "Retorna la cantidad de jugadores y sus nombres")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerInfoJugadores(@PathVariable Long partidaId) {
        try {
            Partida partida = partidaService.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
            
            List<Participacion> participaciones = participacionService.findByPartidaId(partidaId);
            
            List<Map<String, Object>> jugadores = participaciones.stream()
                .map(p -> {
                    Map<String, Object> jugador = new java.util.HashMap<>();
                    jugador.put("nombreUsuario", p.getJugador().getNombre());
                    jugador.put("barcoAlias", p.getBarco().getAlias());
                    jugador.put("numeroTurno", p.getOrdenTurno());
                    return jugador;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "totalJugadores", participaciones.size(),
                "jugadores", jugadores,
                "estado", partida.getEstado().toString()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{partidaId}/unirse")
    @Operation(summary = "Unirse a una partida existente", 
               description = "Permite que un jugador se una a una partida en estado ESPERANDO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unido exitosamente"),
        @ApiResponse(responseCode = "400", description = "No se puede unir a la partida"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> unirseAPartida(
            @PathVariable Long partidaId,
            @RequestParam Long usuarioId,
            @RequestParam Long barcoId) {
        try {
            // Verificar que la partida existe
            Partida partida = partidaService.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
            
            // Verificar que está en estado ESPERANDO
            if (partida.getEstado() != Partida.Estado.ESPERANDO) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La partida ya ha comenzado o finalizado"));
            }
            
            // Obtener usuario y barco
            Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Barco barco = barcoService.findById(barcoId)
                .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
            
            // Verificar que el barco pertenece al usuario
            if (!barco.getUsuario().getIdUsuario().equals(usuarioId)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El barco no pertenece al usuario"));
            }
            
            // Verificar que el usuario no está ya en la partida
            boolean yaParticipa = participacionService.findByPartidaId(partidaId).stream()
                .anyMatch(p -> p.getJugador().getIdUsuario().equals(usuarioId));
            
            if (yaParticipa) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ya estás participando en esta partida"));
            }
            
            // Buscar celda de partida disponible
            List<Celda> celdasPartida = celdaService.findByMapaAndTipo(
                partida.getMapa(), Celda.Tipo.PARTIDA);
            
            // Obtener celdas ya ocupadas
            List<Participacion> participacionesActuales = participacionService.findByPartidaId(partidaId);
            List<Long> celdasOcupadas = participacionesActuales.stream()
                .map(p -> p.getCeldaInicial().getIdCelda())
                .collect(Collectors.toList());
            
            // Buscar primera celda disponible
            Celda celdaDisponible = celdasPartida.stream()
                .filter(c -> !celdasOcupadas.contains(c.getIdCelda()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay celdas de partida disponibles"));
            
            // Calcular número de turno (basado en participaciones existentes)
            int numeroTurno = participacionesActuales.size() + 1;
            
            // Crear participación
            Participacion participacion = participacionService.crearParticipacion(
                partida, usuario, barco, celdaDisponible, numeroTurno);
            
            // Auto-iniciar partida si ya hay 2 o más jugadores
            List<Participacion> todasLasParticipaciones = participacionService.findByPartidaId(partidaId);
            if (todasLasParticipaciones.size() >= 2 && partida.getEstado() == Partida.Estado.ESPERANDO) {
                partida.iniciarPartida();
                partidaService.save(partida);
                System.out.println("🎮 Partida auto-iniciada con " + todasLasParticipaciones.size() + " jugadores");
            }
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Te has unido exitosamente a la partida",
                "participacionId", participacion.getIdParticipacion(),
                "partidaId", partidaId,
                "turno", numeroTurno,
                "estadoPartida", partida.getEstado().toString(),
                "redirectTo", "/game/participacion/" + participacion.getIdParticipacion()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{partidaId}/iniciar")
    @Operation(summary = "Iniciar partida", 
               description = "Cambia el estado de la partida de ESPERANDO a EN_JUEGO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida iniciada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No se puede iniciar la partida"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> iniciarPartida(@PathVariable Long partidaId) {
        try {
            Partida partida = partidaService.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
            
            // Verificar que está en estado ESPERANDO
            if (partida.getEstado() != Partida.Estado.ESPERANDO) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La partida ya fue iniciada"));
            }
            
            // Verificar que hay al menos 2 participantes
            List<Participacion> participaciones = participacionService.findByPartidaId(partidaId);
            if (participaciones.size() < 2) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Se necesitan al menos 2 jugadores para iniciar"));
            }
            
            // Cambiar estado a EN_JUEGO
            partida.iniciarPartida();
            partidaService.save(partida);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Partida iniciada exitosamente",
                "partidaId", partidaId,
                "estado", partida.getEstado().toString()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/crear-y-esperar")
    @Operation(summary = "Crear partida y esperar jugadores", 
               description = "Crea una partida en estado ESPERANDO y crea la primera participación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearPartidaYEsperar(
            @RequestBody PartidaDTO partidaDTO,
            @RequestParam Long usuarioId,
            @RequestParam Long barcoId) {
        try {
            // Crear la partida en estado ESPERANDO
            Partida partida = partidaMapper.toEntity(partidaDTO);
            partida.setEstado(Partida.Estado.ESPERANDO);
            Partida savedPartida = partidaService.save(partida);
            
            // Obtener el usuario que crea la partida
            Usuario jugador = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Obtener el barco seleccionado
            Barco barco = barcoService.findById(barcoId)
                    .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
            
            // Verificar que el barco pertenece al usuario
            if (!barco.getUsuario().getIdUsuario().equals(usuarioId)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El barco no pertenece al usuario"));
            }
            
            // Encontrar la primera celda de partida del mapa
            Celda celdaPartida = celdaService.findByMapaAndTipo(savedPartida.getMapa(), Celda.Tipo.PARTIDA)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró celda de partida en el mapa"));
            
            // Crear participación inicial (turno 1)
            Participacion participacion = participacionService.crearParticipacion(
                    savedPartida, jugador, barco, celdaPartida, 1);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Partida creada. Esperando jugadores...",
                "partidaId", savedPartida.getIdPartida(),
                "participacionId", participacion.getIdParticipacion(),
                "estado", savedPartida.getEstado().toString()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{partidaId}/estado-completo")
    @Operation(summary = "Obtener estado completo de la partida", 
               description = "Retorna toda la información de la partida incluyendo todas las participaciones y sus posiciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerEstadoCompleto(@PathVariable Long partidaId) {
        try {
            Partida partida = partidaService.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
            
            List<Participacion> participaciones = participacionService.findByPartidaId(partidaId);
            
            // Construir respuesta con toda la info necesaria
            Map<String, Object> estado = new java.util.HashMap<>();
            estado.put("partidaId", partida.getIdPartida());
            estado.put("estado", partida.getEstado().toString());
            estado.put("mapaId", partida.getMapa().getIdMapa());
            
            // Información de todas las participaciones
            List<Map<String, Object>> participacionesInfo = participaciones.stream()
                .map(p -> {
                    Map<String, Object> info = new java.util.HashMap<>();
                    info.put("participacionId", p.getIdParticipacion());
                    info.put("usuarioId", p.getJugador().getIdUsuario());
                    info.put("usuarioNombre", p.getJugador().getNombre());
                    info.put("barcoId", p.getBarco().getIdBarco());
                    info.put("barcoAlias", p.getBarco().getAlias());
                    info.put("barcoColor", p.getBarco().getModelo().getColorHex());
                    info.put("ordenTurno", p.getOrdenTurno());
                    info.put("estado", p.getEstado().toString());
                    
                    // Obtener el último movimiento de esta participación
                    Optional<Movimiento> ultimoMovimiento = movimientoService.obtenerEstadoActual(p.getIdParticipacion());
                    if (ultimoMovimiento.isPresent()) {
                        info.put("ultimoMovimiento", movimientoMapper.toDTO(ultimoMovimiento.get()));
                    } else {
                        // Si no hay movimiento, usar posición inicial
                        info.put("posicionX", p.getCeldaInicial().getCoordX());
                        info.put("posicionY", p.getCeldaInicial().getCoordY());
                    }
                    
                    return info;
                })
                .collect(Collectors.toList());
            
            estado.put("participaciones", participacionesInfo);
            estado.put("totalJugadores", participaciones.size());
            
            return ResponseEntity.ok(estado);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
