package com.regataonline.repository;

import com.regataonline.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    // Buscar jugador por email (único)
    Optional<Jugador> findByEmail(String email);
    
    // Buscar jugadores por nombre (puede haber varios con el mismo nombre)
    List<Jugador> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar jugadores con más de X victorias
    List<Jugador> findByVictoriasGreaterThan(Integer victorias);
    
    // Buscar jugadores con experiencia entre dos valores
    List<Jugador> findByExperienciaBetween(Integer minExperiencia, Integer maxExperiencia);
    
    // Buscar los top N jugadores por victorias
    @Query("SELECT j FROM Jugador j ORDER BY j.victorias DESC")
    List<Jugador> findTopJugadoresByVictorias();
    
    // Buscar jugadores con al menos un barco
    @Query("SELECT DISTINCT j FROM Jugador j JOIN j.barcos b WHERE b.activo = true")
    List<Jugador> findJugadoresConBarcosActivos();
    
    // Contar total de barcos de un jugador
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.jugador.id = :jugadorId AND b.activo = true")
    Long countBarcosActivosByJugadorId(@Param("jugadorId") Long jugadorId);
    
    // Verificar si existe un jugador con el email dado
    boolean existsByEmail(String email);
}
