package com.example.regata.repository;

import com.example.regata.model.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MapaRepository extends JpaRepository<Mapa, UUID> {
    
    // Buscar mapas por nombre que contenga el texto (búsqueda parcial)
    @Query("SELECT m FROM Mapa m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Mapa> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar mapas por tamaño de filas
    @Query("SELECT m FROM Mapa m WHERE m.tamFilas = :tamFilas")
    List<Mapa> findByTamFilas(@Param("tamFilas") Integer tamFilas);
    
    // Buscar mapas por tamaño de columnas
    @Query("SELECT m FROM Mapa m WHERE m.tamColumnas = :tamColumnas")
    List<Mapa> findByTamColumnas(@Param("tamColumnas") Integer tamColumnas);
    
    // Buscar mapas por tamaño específico
    @Query("SELECT m FROM Mapa m WHERE m.tamFilas = :tamFilas AND m.tamColumnas = :tamColumnas")
    List<Mapa> findByTamFilasAndTamColumnas(@Param("tamFilas") Integer tamFilas, 
                                           @Param("tamColumnas") Integer tamColumnas);
    
    // Buscar mapas ordenados por nombre
    @Query("SELECT m FROM Mapa m ORDER BY m.nombre ASC")
    List<Mapa> findAllOrderByNombreAsc();
    
    // Buscar mapas ordenados por tamaño (filas * columnas)
    @Query("SELECT m FROM Mapa m ORDER BY (m.tamFilas * m.tamColumnas) ASC")
    List<Mapa> findAllOrderByTamañoAsc();
    
    // Contar partidas que usan este mapa
    @Query("SELECT COUNT(p) FROM Partida p WHERE p.mapa.idMapa = :mapaId")
    Long countPartidasByMapaId(@Param("mapaId") UUID mapaId);
    
    // Buscar mapas más populares (más usados en partidas)
    @Query("SELECT m FROM Mapa m LEFT JOIN m.partidas p GROUP BY m.idMapa ORDER BY COUNT(p) DESC")
    List<Mapa> findMapasMasPopulares();
    
    // Buscar mapas por tamaño mínimo
    @Query("SELECT m FROM Mapa m WHERE (m.tamFilas * m.tamColumnas) >= :tamañoMinimo")
    List<Mapa> findByTamañoMinimo(@Param("tamañoMinimo") Integer tamañoMinimo);
}

