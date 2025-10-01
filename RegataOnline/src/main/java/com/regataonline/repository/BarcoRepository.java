package com.regataonline.repository;

import com.regataonline.model.Barco;
import com.regataonline.model.Jugador;
import com.regataonline.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {
    
    // Buscar barcos por nombre
    List<Barco> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar barcos por jugador
    List<Barco> findByJugador(Jugador jugador);
    
    // Buscar barcos activos por jugador
    List<Barco> findByJugadorAndActivoTrue(Jugador jugador);
    
    // Buscar barcos por modelo
    List<Barco> findByModelo(Modelo modelo);
    
    // Buscar barcos activos por modelo
    List<Barco> findByModeloAndActivoTrue(Modelo modelo);
    
    // Buscar barcos por jugador ID
    List<Barco> findByJugadorId(Long jugadorId);
    
    // Buscar barcos activos por jugador ID
    List<Barco> findByJugadorIdAndActivoTrue(Long jugadorId);
    
    // Buscar barcos por modelo ID
    List<Barco> findByModeloId(Long modeloId);
    
    // Buscar barcos con salud mayor a X
    List<Barco> findByEstadoSaludGreaterThan(Integer salud);
    
    // Buscar barcos con experiencia mayor a X
    List<Barco> findByExperienciaGreaterThan(Integer experiencia);
    
    // Buscar barcos con carreras ganadas mayor a X
    List<Barco> findByCarrerasGanadasGreaterThan(Integer carreras);
    
    // Buscar barcos creados después de una fecha
    List<Barco> findByFechaCreacionAfter(LocalDateTime fecha);
    
    // Buscar barcos por color
    List<Barco> findByColor(String color);
    
    // Buscar todos los barcos activos
    List<Barco> findByActivoTrue();
    
    // Buscar todos los barcos inactivos
    List<Barco> findByActivoFalse();
    
    // Buscar barcos ordenados por experiencia descendente
    List<Barco> findAllByOrderByExperienciaDesc();
    
    // Buscar barcos ordenados por carreras ganadas descendente
    List<Barco> findAllByOrderByCarrerasGanadasDesc();
    
    // Buscar top barcos por experiencia
    @Query("SELECT b FROM Barco b WHERE b.activo = true ORDER BY b.experiencia DESC")
    List<Barco> findTopBarcosByExperiencia();
    
    // Buscar barcos con bajo combustible
    @Query("SELECT b FROM Barco b WHERE b.nivelCombustible < :limite AND b.activo = true")
    List<Barco> findBarcosConBajoCombustible(@Param("limite") Integer limite);
    
    // Buscar barcos dañados
    @Query("SELECT b FROM Barco b WHERE b.estadoSalud < :limite AND b.activo = true")
    List<Barco> findBarcosDanados(@Param("limite") Integer limite);
    
    // Contar barcos por jugador
    Long countByJugadorIdAndActivoTrue(Long jugadorId);
    
    // Contar barcos por modelo
    Long countByModeloIdAndActivoTrue(Long modeloId);
    
    // Verificar si existe un barco con el nombre para un jugador específico
    boolean existsByNombreAndJugadorId(String nombre, Long jugadorId);
    
    // Buscar barcos con nivel de combustible menor a X
    List<Barco> findByNivelCombustibleLessThan(Integer nivel);
    
    // Buscar barcos con estado de salud menor a X
    List<Barco> findByEstadoSaludLessThan(Double salud);
    
    // Contar barcos activos
    Long countByActivoTrue();
    
    // Contar barcos por jugador (sin filtro de activo)
    Long countByJugadorId(Long jugadorId);
    
    // Contar barcos por modelo (sin filtro de activo)
    Long countByModeloId(Long modeloId);
    
    // Buscar top barcos por experiencia (método simplificado)
    List<Barco> findTopByOrderByExperienciaDesc();
    
    // Buscar top barcos por carreras ganadas (método simplificado)
    List<Barco> findTopByOrderByCarrerasGanadasDesc();
}
