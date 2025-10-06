package com.example.regata.repository;

import com.example.regata.model.Barco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {
    
    List<Barco> findByJugadorId(Long jugadorId);
    
    List<Barco> findByModeloId(Long modeloId);
    
    List<Barco> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT b FROM Barco b WHERE b.jugador.id = :jugadorId ORDER BY b.puntosGanados DESC")
    List<Barco> findByJugadorIdOrderByPuntosGanadosDesc(@Param("jugadorId") Long jugadorId);
    
    @Query("SELECT b FROM Barco b ORDER BY b.puntosGanados DESC")
    List<Barco> findAllOrderByPuntosGanadosDesc();
    
    @Query("SELECT b FROM Barco b WHERE b.velocidadActual > 0")
    List<Barco> findBarcosEnMovimiento();
    
    @Query("SELECT b FROM Barco b WHERE b.jugador.id = :jugadorId AND b.modelo.id = :modeloId")
    List<Barco> findByJugadorAndModelo(@Param("jugadorId") Long jugadorId, @Param("modeloId") Long modeloId);
}
