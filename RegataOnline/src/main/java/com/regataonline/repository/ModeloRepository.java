package com.regataonline.repository;

import com.regataonline.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    
    // Buscar modelos por nombre
    List<Modelo> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar modelos por tipo
    List<Modelo> findByTipoIgnoreCase(String tipo);
    
    // Buscar modelos con velocidad mayor a X
    List<Modelo> findByVelocidadMaximaGreaterThan(Integer velocidad);
    
    // Buscar modelos con resistencia mayor a X
    List<Modelo> findByResistenciaGreaterThan(Integer resistencia);
    
    // Buscar modelos con maniobrabilidad mayor a X
    List<Modelo> findByManiobrabilidadGreaterThan(Integer maniobrabilidad);
    
    // Buscar modelos por rango de velocidad
    List<Modelo> findByVelocidadMaximaBetween(Integer minVelocidad, Integer maxVelocidad);
    
    // Buscar modelos ordenados por velocidad descendente
    List<Modelo> findAllByOrderByVelocidadMaximaDesc();
    
    // Buscar modelos ordenados por resistencia descendente
    List<Modelo> findAllByOrderByResistenciaDesc();
    
    // Buscar modelos más populares (con más barcos)
    @Query("SELECT m FROM Modelo m JOIN m.barcos b GROUP BY m ORDER BY COUNT(b) DESC")
    List<Modelo> findModelosMasPopulares();
    
    // Contar barcos por modelo
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.modelo.id = :modeloId AND b.activo = true")
    Long countBarcosActivosByModeloId(@Param("modeloId") Long modeloId);
    
    // Buscar modelos con características balanceadas
    @Query("SELECT m FROM Modelo m WHERE m.velocidadMaxima >= :minVel AND m.resistencia >= :minRes AND m.maniobrabilidad >= :minMan")
    List<Modelo> findModelosBalanceados(@Param("minVel") Integer minVelocidad, 
                                       @Param("minRes") Integer minResistencia, 
                                       @Param("minMan") Integer minManiobrabilidad);
    
    // Obtener todos los tipos de modelos únicos
    @Query("SELECT DISTINCT m.tipo FROM Modelo m ORDER BY m.tipo")
    List<String> findAllTipos();
    
    // Buscar modelo por nombre exacto
    Optional<Modelo> findByNombre(String nombre);
    
    // Buscar modelos por tipo que contenga el texto
    List<Modelo> findByTipoContainingIgnoreCase(String tipo);
    
    // Buscar modelos por rango de velocidad con Double
    List<Modelo> findByVelocidadMaximaBetween(Double minVelocidad, Double maxVelocidad);
    
    // Buscar modelos con velocidad mínima
    List<Modelo> findByVelocidadMaximaGreaterThanEqual(Double velocidadMinima);
    
    // Buscar modelos con resistencia mínima
    List<Modelo> findByResistenciaGreaterThanEqual(Double resistenciaMinima);
    
    // Buscar modelos con maniobrabilidad mínima
    List<Modelo> findByManiobrabilidadGreaterThanEqual(Double maniobrabilidadMinima);
}
