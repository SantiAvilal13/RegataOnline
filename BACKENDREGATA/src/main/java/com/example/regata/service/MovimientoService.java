package com.example.regata.service;

import com.example.regata.model.Movimiento;
import com.example.regata.model.Participacion;
import com.example.regata.model.Celda;

import java.util.List;
import java.util.Optional;

public interface MovimientoService {
    
    // CRUD básico
    List<Movimiento> findAll();
    Optional<Movimiento> findById(Long id);
    Movimiento save(Movimiento movimiento);
    void deleteById(Long id);
    
    // Búsquedas específicas
    List<Movimiento> findByParticipacionId(Long participacionId);
    Optional<Movimiento> findByParticipacionIdAndTurno(Long participacionId, Integer turno);
    List<Movimiento> findByParticipacionIdAndTurnoBetween(Long participacionId, Integer turnoInicio, Integer turnoFin);
    
    // QUERIES CLAVE: Estado actual y historial
    Optional<Movimiento> obtenerEstadoActual(Long participacionId);
    List<Movimiento> obtenerHistorialCompleto(Long participacionId);
    
    // Búsquedas por resultado
    List<Movimiento> findMovimientosConColision();
    List<Movimiento> findMovimientosConColisionByParticipacionId(Long participacionId);
    List<Movimiento> findByResultado(Movimiento.Resultado resultado);
    List<Movimiento> findByParticipacionIdAndResultado(Long participacionId, Movimiento.Resultado resultado);
    
    // Búsquedas por posición y velocidad
    List<Movimiento> findByParticipacionIdAndPosicion(Long participacionId, Integer posX, Integer posY);
    List<Movimiento> findByParticipacionIdAndVelocidad(Long participacionId, Integer velX, Integer velY);
    List<Movimiento> findByDeltaVelocidad(Integer deltaVx, Integer deltaVy);
    
    // Métodos de negocio
    Movimiento realizarMovimiento(Long participacionId, Integer deltaVx, Integer deltaVy);
    Integer getSiguienteTurno(Long participacionId);
    
    // Contadores
    Long countByParticipacionId(Long participacionId);
}
