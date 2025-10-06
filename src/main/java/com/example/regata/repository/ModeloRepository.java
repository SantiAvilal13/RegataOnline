package com.example.regata.repository;

import com.example.regata.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, UUID> {
    
    // Buscar modelos por nombre que contenga el texto (búsqueda parcial)
    @Query("SELECT m FROM Modelo m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Modelo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar modelos por color hexadecimal
    @Query("SELECT m FROM Modelo m WHERE m.colorHex = :colorHex")
    List<Modelo> findByColorHex(@Param("colorHex") String colorHex);
    
    // Buscar modelos que contengan un color similar
    @Query("SELECT m FROM Modelo m WHERE LOWER(m.colorHex) LIKE LOWER(CONCAT('%', :color, '%'))")
    List<Modelo> findByColorHexContainingIgnoreCase(@Param("color") String color);
    
    // Buscar modelos ordenados por nombre
    @Query("SELECT m FROM Modelo m ORDER BY m.nombre ASC")
    List<Modelo> findAllOrderByNombreAsc();
    
    // Contar barcos que usan este modelo
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.modelo.idModelo = :modeloId")
    Long countBarcosByModeloId(@Param("modeloId") UUID modeloId);
    
    // Buscar modelos más populares (más usados)
    @Query("SELECT m FROM Modelo m LEFT JOIN m.barcos b GROUP BY m.idModelo ORDER BY COUNT(b) DESC")
    List<Modelo> findModelosMasPopulares();
}
