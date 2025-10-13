package com.example.regata.service;

import com.example.regata.model.Participacion;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.model.Celda;

import java.util.List;
import java.util.Optional;

public interface ParticipacionService {
    
    // CRUD básico
    List<Participacion> findAll();
    Optional<Participacion> findById(Long id);
    Participacion save(Participacion participacion);
    void deleteById(Long id);
    
    // Búsquedas específicas
    List<Participacion> findByPartidaId(Long partidaId);
    List<Participacion> findByJugadorId(Long usuarioId);
    List<Participacion> findByBarcoId(Long barcoId);
    List<Participacion> findByEstado(Participacion.Estado estado);
    
    // Búsquedas específicas de partida
    List<Participacion> findParticipacionesActivasByPartidaId(Long partidaId);
    Optional<Participacion> findByPartidaIdAndJugadorId(Long partidaId, Long usuarioId);
    Optional<Participacion> findByPartidaIdAndBarcoId(Long partidaId, Long barcoId);
    List<Participacion> findByPartidaIdOrderByOrdenTurnoAsc(Long partidaId);
    List<Participacion> findParticipacionesEnMetaByPartidaId(Long partidaId);
    
    // Validaciones
    boolean existsByPartidaIdAndJugadorId(Long partidaId, Long usuarioId);
    boolean existsByPartidaIdAndBarcoId(Long partidaId, Long barcoId);
    
    // Métodos de negocio
    Participacion crearParticipacion(Partida partida, Usuario jugador, Barco barco, Celda celdaInicial, Integer ordenTurno);
    void destruirParticipacion(Long participacionId);
    void marcarComoEnMeta(Long participacionId);
    
    // Contadores
    Long countByPartidaIdAndEstado(Long partidaId, Participacion.Estado estado);
}
