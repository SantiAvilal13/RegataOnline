package com.example.regata.restcontroller;

import com.example.regata.dto.PartidaDTO;
import com.example.regata.mapper.PartidaMapper;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.model.Celda;
import com.example.regata.model.Participacion;
import com.example.regata.service.PartidaService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.BarcoService;
import com.example.regata.service.CeldaService;
import com.example.regata.service.ParticipacionService;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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
}
