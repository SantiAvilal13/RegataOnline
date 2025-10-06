package com.example.regata.repository;

import com.example.regata.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    
    List<Modelo> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT m FROM Modelo m WHERE m.velocidadMaxima >= :velocidadMinima")
    List<Modelo> findByVelocidadMaximaGreaterThanEqual(@Param("velocidadMinima") Integer velocidadMinima);
    
    @Query("SELECT m FROM Modelo m WHERE m.resistencia >= :resistenciaMinima")
    List<Modelo> findByResistenciaGreaterThanEqual(@Param("resistenciaMinima") Integer resistenciaMinima);
    
    @Query("SELECT m FROM Modelo m WHERE m.maniobrabilidad >= :maniobrabilidadMinima")
    List<Modelo> findByManiobrabilidadGreaterThanEqual(@Param("maniobrabilidadMinima") Integer maniobrabilidadMinima);
    
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.modelo.id = :modeloId")
    Long countBarcosByModeloId(@Param("modeloId") Long modeloId);
}
