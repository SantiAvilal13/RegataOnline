package com.example.regata.repository;

import com.example.regata.model.Movimiento;
import com.example.regata.model.Participacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    // Buscar movimientos por participación
    List<Movimiento> findByParticipacion(Participacion participacion);
    
    // Buscar movimientos por ID de participación
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId")
    List<Movimiento> findByParticipacionId(@Param("participacionId") Long participacionId);
    
    // Buscar movimientos por turno específico
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND m.turno = :turno")
    Optional<Movimiento> findByParticipacionIdAndTurno(@Param("participacionId") Long participacionId, 
                                                      @Param("turno") Integer turno);
    
    // Buscar movimientos por rango de turnos
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND " +
           "m.turno BETWEEN :turnoInicio AND :turnoFin ORDER BY m.turno ASC")
    List<Movimiento> findByParticipacionIdAndTurnoBetween(@Param("participacionId") Long participacionId,
                                                         @Param("turnoInicio") Integer turnoInicio,
                                                         @Param("turnoFin") Integer turnoFin);
    
    // Buscar movimientos con colisiones
    @Query("SELECT m FROM Movimiento m WHERE m.resultado = 'COLISION_PARED'")
    List<Movimiento> findMovimientosConColision();
    
    // Buscar movimientos con colisiones en una participación específica
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND m.resultado = 'COLISION_PARED'")
    List<Movimiento> findMovimientosConColisionByParticipacionId(@Param("participacionId") Long participacionId);
    
    // Buscar movimientos por posición específica
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND " +
           "m.posX = :posX AND m.posY = :posY")
    List<Movimiento> findByParticipacionIdAndPosicion(@Param("participacionId") Long participacionId,
                                                     @Param("posX") Integer posX, 
                                                     @Param("posY") Integer posY);
    
    // QUERIES CLAVE: Obtener estado actual (último movimiento)
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId ORDER BY m.turno DESC LIMIT 1")
    Optional<Movimiento> findUltimoMovimiento(@Param("participacionId") Long participacionId);
    
    // Obtener historial completo ordenado por turno
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId ORDER BY m.turno ASC")
    List<Movimiento> findHistorialCompleto(@Param("participacionId") Long participacionId);
    
    // Buscar movimientos por cambio de velocidad específico
    @Query("SELECT m FROM Movimiento m WHERE m.deltaVx = :deltaVx AND m.deltaVy = :deltaVy")
    List<Movimiento> findByDeltaVelocidad(@Param("deltaVx") Integer deltaVx,
                                         @Param("deltaVy") Integer deltaVy);
    
    // Contar movimientos por participación
    @Query("SELECT COUNT(m) FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId")
    Long countByParticipacionId(@Param("participacionId") Long participacionId);
    
    // Buscar movimientos ordenados por turno
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId ORDER BY m.turno ASC")
    List<Movimiento> findByParticipacionIdOrderByTurnoAsc(@Param("participacionId") Long participacionId);
    
    // Buscar movimientos por velocidad específica
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND " +
           "m.velX = :velX AND m.velY = :velY")
    List<Movimiento> findByParticipacionIdAndVelocidad(@Param("participacionId") Long participacionId,
                                                      @Param("velX") Integer velX, 
                                                      @Param("velY") Integer velY);
    
    // Buscar movimientos por resultado específico
    @Query("SELECT m FROM Movimiento m WHERE m.resultado = :resultado")
    List<Movimiento> findByResultado(@Param("resultado") Movimiento.Resultado resultado);
    
    // Buscar movimientos por resultado en una participación específica
    @Query("SELECT m FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId AND m.resultado = :resultado")
    List<Movimiento> findByParticipacionIdAndResultado(@Param("participacionId") Long participacionId,
                                                      @Param("resultado") Movimiento.Resultado resultado);
    
    // Obtener el siguiente número de turno para una participación
    @Query("SELECT COALESCE(MAX(m.turno), -1) + 1 FROM Movimiento m WHERE m.participacion.idParticipacion = :participacionId")
    Integer getSiguienteTurno(@Param("participacionId") Long participacionId);
}

