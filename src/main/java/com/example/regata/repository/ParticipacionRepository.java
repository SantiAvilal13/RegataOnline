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
import java.util.UUID;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, UUID> {
    
    // Buscar participaciones por partida
    List<Participacion> findByPartida(Partida partida);
    
    // Buscar participaciones por ID de partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId")
    List<Participacion> findByPartidaId(@Param("partidaId") UUID partidaId);
    
    // Buscar participaciones por jugador
    List<Participacion> findByJugador(Usuario jugador);
    
    // Buscar participaciones por ID del jugador
    @Query("SELECT p FROM Participacion p WHERE p.jugador.idUsuario = :usuarioId")
    List<Participacion> findByJugadorId(@Param("usuarioId") UUID usuarioId);
    
    // Buscar participaciones por barco
    List<Participacion> findByBarco(Barco barco);
    
    // Buscar participaciones por ID del barco
    @Query("SELECT p FROM Participacion p WHERE p.barco.idBarco = :barcoId")
    List<Participacion> findByBarcoId(@Param("barcoId") UUID barcoId);
    
    // Buscar participaciones por estado
    List<Participacion> findByEstado(Participacion.Estado estado);
    
    // Buscar participaciones activas en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = 'ACTIVO'")
    List<Participacion> findParticipacionesActivasByPartidaId(@Param("partidaId") UUID partidaId);
    
    // Buscar participación específica de un jugador en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.jugador.idUsuario = :usuarioId")
    Optional<Participacion> findByPartidaIdAndJugadorId(@Param("partidaId") UUID partidaId, 
                                                       @Param("usuarioId") UUID usuarioId);
    
    // Buscar participación específica de un barco en una partida
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.barco.idBarco = :barcoId")
    Optional<Participacion> findByPartidaIdAndBarcoId(@Param("partidaId") UUID partidaId, 
                                                     @Param("barcoId") UUID barcoId);
    
    // Buscar participaciones por posición
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.posX = :posX AND p.posY = :posY")
    List<Participacion> findByPartidaIdAndPosicion(@Param("partidaId") UUID partidaId, 
                                                  @Param("posX") Integer posX, 
                                                  @Param("posY") Integer posY);
    
    // Buscar participaciones ordenadas por turno actual
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId ORDER BY p.turnoActual ASC")
    List<Participacion> findByPartidaIdOrderByTurnoActualAsc(@Param("partidaId") UUID partidaId);
    
    // Contar participaciones por estado en una partida
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = :estado")
    Long countByPartidaIdAndEstado(@Param("partidaId") UUID partidaId, @Param("estado") Participacion.Estado estado);
    
    // Buscar participaciones que llegaron a la meta
    @Query("SELECT p FROM Participacion p WHERE p.partida.idPartida = :partidaId AND p.estado = 'EN_META' ORDER BY p.turnoActual ASC")
    List<Participacion> findParticipacionesEnMetaByPartidaId(@Param("partidaId") UUID partidaId);
}

