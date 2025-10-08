package com.example.regata.repository;

import com.example.regata.model.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapaRepository extends JpaRepository<Mapa, Long> {
    
    /**
     * Buscar mapas por nombre (búsqueda parcial, case-insensitive)
     */
    List<Mapa> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Buscar mapas por tamaño específico
     */
    List<Mapa> findByTamFilasAndTamColumnas(Integer tamFilas, Integer tamColumnas);
    
    /**
     * Buscar mapas por rango de tamaño
     */
    @Query("SELECT m FROM Mapa m WHERE m.tamFilas >= :minFilas AND m.tamFilas <= :maxFilas " +
           "AND m.tamColumnas >= :minColumnas AND m.tamColumnas <= :maxColumnas")
    List<Mapa> findByTamanoRango(Integer minFilas, Integer maxFilas, Integer minColumnas, Integer maxColumnas);
    
    /**
     * Buscar mapas ordenados por nombre
     */
    List<Mapa> findAllByOrderByNombreAsc();
    
    /**
     * Buscar mapas ordenados por tamaño (filas * columnas)
     */
    @Query("SELECT m FROM Mapa m ORDER BY (m.tamFilas * m.tamColumnas) ASC")
    List<Mapa> findAllOrderByTamanoAsc();
    
    /**
     * Contar mapas por tamaño
     */
    @Query("SELECT COUNT(m) FROM Mapa m WHERE m.tamFilas = :filas AND m.tamColumnas = :columnas")
    Long countByTamano(Integer filas, Integer columnas);
    
    /**
     * Buscar mapas que tengan partidas asociadas
     */
    @Query("SELECT DISTINCT m FROM Mapa m JOIN m.partidas p")
    List<Mapa> findMapasConPartidas();
    
    /**
     * Buscar mapas sin partidas asociadas
     */
    @Query("SELECT m FROM Mapa m LEFT JOIN m.partidas p WHERE p IS NULL")
    List<Mapa> findMapasSinPartidas();
    
    /**
     * Buscar mapas por nombre exacto
     */
    Optional<Mapa> findByNombre(String nombre);
}