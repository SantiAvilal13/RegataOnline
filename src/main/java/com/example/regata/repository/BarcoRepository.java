package com.example.regata.repository;

import com.example.regata.model.Barco;
import com.example.regata.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, UUID> {
    
    // Buscar barcos por usuario propietario
    List<Barco> findByUsuario(Usuario usuario);
    
    // Buscar barcos por ID del usuario
    @Query("SELECT b FROM Barco b WHERE b.usuario.idUsuario = :usuarioId")
    List<Barco> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    // Buscar barcos por modelo
    @Query("SELECT b FROM Barco b WHERE b.modelo.idModelo = :modeloId")
    List<Barco> findByModeloId(@Param("modeloId") UUID modeloId);
    
    // Buscar barcos por alias que contenga el texto (búsqueda parcial)
    @Query("SELECT b FROM Barco b WHERE LOWER(b.alias) LIKE LOWER(CONCAT('%', :alias, '%'))")
    List<Barco> findByAliasContainingIgnoreCase(@Param("alias") String alias);
    
    // Buscar barcos de un usuario ordenados por alias
    @Query("SELECT b FROM Barco b WHERE b.usuario.idUsuario = :usuarioId ORDER BY b.alias ASC")
    List<Barco> findByUsuarioIdOrderByAliasAsc(@Param("usuarioId") UUID usuarioId);
    
    // Buscar barcos que participaron en partidas
    @Query("SELECT DISTINCT b FROM Barco b JOIN b.participaciones p")
    List<Barco> findBarcosConParticipaciones();
    
    // Buscar barcos por usuario y modelo
    @Query("SELECT b FROM Barco b WHERE b.usuario.idUsuario = :usuarioId AND b.modelo.idModelo = :modeloId")
    List<Barco> findByUsuarioAndModelo(@Param("usuarioId") UUID usuarioId, @Param("modeloId") UUID modeloId);
    
    // Contar barcos por usuario
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.usuario.idUsuario = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") UUID usuarioId);
}
