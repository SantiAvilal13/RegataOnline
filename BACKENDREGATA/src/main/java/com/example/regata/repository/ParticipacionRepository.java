package com.example.regata.repository;

import com.example.regata.model.Participacion;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {
    
    // Buscar participaciones por partida
    List<Participacion> findByPartida(Partida partida);
    
    // Buscar participaciones por ID de partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId")
    List<Participacion> findByPartidaId(@Param("partidaId") Long partidaId);
    
    // Buscar participaciones por jugador
    List<Participacion> findByJugador(Usuario jugador);
    
    // Buscar participaciones por ID del jugador
    @Query("SELECT p FROM Participacion p WHERE p.jugador.idUsuario = :usuarioId")
    List<Participacion> findByJugadorId(@Param("usuarioId") Long usuarioId);
    
    // Buscar participaciones por barco
    List<Participacion> findByBarco(Barco barco);
    
    // Buscar participaciones por ID del barco
    @Query("SELECT p FROM Participacion p WHERE p.barco.idBarco = :barcoId")
    List<Participacion> findByBarcoId(@Param("barcoId") Long barcoId);
    
    // Buscar participaciones por estado
    List<Participacion> findByEstado(Participacion.Estado estado);
    
    // Buscar participaciones activas en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = 'ACTIVO'")
    List<Participacion> findParticipacionesActivasByPartidaId(@Param("partidaId") Long partidaId);
    
    // Buscar participación específica de un jugador en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.jugador.idUsuario = :usuarioId")
    Optional<Participacion> findByPartidaIdAndJugadorId(@Param("partidaId") Long partidaId, 
                                                       @Param("usuarioId") Long usuarioId);
    
    // Buscar participación específica de un barco en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.barco.idBarco = :barcoId")
    Optional<Participacion> findByPartidaIdAndBarcoId(@Param("partidaId") Long partidaId, 
                                                     @Param("barcoId") Long barcoId);
    
    // Buscar participaciones ordenadas por orden de turno
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId ORDER BY p.ordenTurno ASC")
    List<Participacion> findByPartidaIdOrderByOrdenTurnoAsc(@Param("partidaId") Long partidaId);
    
    // Contar participaciones por estado en una partida
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = :estado")
    Long countByPartidaIdAndEstado(@Param("partidaId") Long partidaId, @Param("estado") Participacion.Estado estado);
    
    // Buscar participaciones que llegaron a la meta
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = 'EN_META' ORDER BY p.fechaFin ASC")
    List<Participacion> findParticipacionesEnMetaByPartidaId(@Param("partidaId") Long partidaId);
    
    // Verificar si existe participación para un jugador en una partida
    @Query("SELECT COUNT(p) > 0 FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.jugador.idUsuario = :usuarioId")
    boolean existsByPartidaIdAndJugadorId(@Param("partidaId") Long partidaId, @Param("usuarioId") Long usuarioId);
    
    // Verificar si existe participación para un barco en una partida
    @Query("SELECT COUNT(p) > 0 FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.barco.idBarco = :barcoId")
    boolean existsByPartidaIdAndBarcoId(@Param("partidaId") Long partidaId, @Param("barcoId") Long barcoId);
}

