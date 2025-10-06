package com.example.regata.repository;

import com.example.regata.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    Optional<Jugador> findByEmail(String email);
    
    List<Jugador> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT j FROM Jugador j ORDER BY j.puntosTotales DESC")
    List<Jugador> findAllOrderByPuntosTotalesDesc();
    
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.jugador.id = :jugadorId")
    Long countBarcosByJugadorId(@Param("jugadorId") Long jugadorId);
    
    boolean existsByEmail(String email);
}
