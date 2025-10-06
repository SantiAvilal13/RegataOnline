package com.example.regata.repository;

import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, UUID> {
    
    // Buscar celdas por mapa
    List<Celda> findByMapa(Mapa mapa);
    
    // Buscar celdas por ID del mapa
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId")
    List<Celda> findByMapaId(@Param("mapaId") UUID mapaId);
    
    // Buscar celdas por tipo
    List<Celda> findByTipo(Celda.Tipo tipo);
    
    // Buscar celdas por tipo en un mapa específico
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = :tipo")
    List<Celda> findByMapaIdAndTipo(@Param("mapaId") UUID mapaId, @Param("tipo") Celda.Tipo tipo);
    
    // Buscar celda por coordenadas en un mapa específico
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.coordX = :coordX AND c.coordY = :coordY")
    Optional<Celda> findByMapaIdAndCoordenadas(@Param("mapaId") UUID mapaId, 
                                               @Param("coordX") Integer coordX, 
                                               @Param("coordY") Integer coordY);
    
    // Buscar celdas de partida (spawn points) en un mapa
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = 'PARTIDA'")
    List<Celda> findCeldasPartidaByMapaId(@Param("mapaId") UUID mapaId);
    
    // Buscar celdas de meta en un mapa
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = 'META'")
    List<Celda> findCeldasMetaByMapaId(@Param("mapaId") UUID mapaId);
    
    // Buscar celdas de agua en un mapa
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = 'AGUA'")
    List<Celda> findCeldasAguaByMapaId(@Param("mapaId") UUID mapaId);
    
    // Buscar celdas de pared en un mapa
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = 'PARED'")
    List<Celda> findCeldasParedByMapaId(@Param("mapaId") UUID mapaId);
    
    // Contar celdas por tipo en un mapa
    @Query("SELECT COUNT(c) FROM Celda c WHERE c.mapa.idMapa = :mapaId AND c.tipo = :tipo")
    Long countByMapaIdAndTipo(@Param("mapaId") UUID mapaId, @Param("tipo") Celda.Tipo tipo);
    
    // Buscar celdas en un rango de coordenadas
    @Query("SELECT c FROM Celda c WHERE c.mapa.idMapa = :mapaId AND " +
           "c.coordX BETWEEN :minX AND :maxX AND c.coordY BETWEEN :minY AND :maxY")
    List<Celda> findByMapaIdAndRangoCoordenadas(@Param("mapaId") UUID mapaId,
                                                @Param("minX") Integer minX, @Param("maxX") Integer maxX,
                                                @Param("minY") Integer minY, @Param("maxY") Integer maxY);
}

