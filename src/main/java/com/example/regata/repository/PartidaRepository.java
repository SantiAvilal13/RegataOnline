package com.example.regata.repository;

import com.example.regata.model.Partida;
import com.example.regata.model.Mapa;
import com.example.regata.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    
    // Buscar partidas por estado
    List<Partida> findByEstado(Partida.Estado estado);
    
    // Buscar partidas por mapa
    List<Partida> findByMapa(Mapa mapa);
    
    // Buscar partidas por ID del mapa
    @Query("SELECT p FROM Partida p WHERE p.mapa.idMapa = :mapaId")
    List<Partida> findByMapaId(@Param("mapaId") Long mapaId);
    
    // Buscar partidas por ganador
    List<Partida> findByGanadorUsuario(Usuario ganadorUsuario);
    
    // Buscar partidas por ID del ganador
    @Query("SELECT p FROM Partida p WHERE p.ganadorUsuario.idUsuario = :usuarioId")
    List<Partida> findByGanadorUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Buscar partidas por rango de fechas
    @Query("SELECT p FROM Partida p WHERE p.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
    List<Partida> findByFechaInicioBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                          @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar partidas activas (EN_JUEGO)
    @Query("SELECT p FROM Partida p WHERE p.estado = 'EN_JUEGO'")
    List<Partida> findPartidasActivas();
    
    // Buscar partidas terminadas ordenadas por fecha de fin
    @Query("SELECT p FROM Partida p WHERE p.estado = 'TERMINADA' ORDER BY p.fechaFin DESC")
    List<Partida> findPartidasTerminadasOrderByFechaFinDesc();
    
    // Contar partidas por estado
    @Query("SELECT COUNT(p) FROM Partida p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") Partida.Estado estado);
    
    // Buscar partidas donde participó un usuario específico
    @Query("SELECT DISTINCT p FROM Partida p JOIN p.participaciones part WHERE part.jugador.idUsuario = :usuarioId")
    List<Partida> findPartidasByParticipacionUsuario(@Param("usuarioId") Long usuarioId);
    
    // Buscar partidas recientes (últimas N partidas)
    @Query("SELECT p FROM Partida p WHERE p.estado = 'TERMINADA' ORDER BY p.fechaFin DESC")
    List<Partida> findTopNPartidasRecientes(@Param("limit") int limit);
}

