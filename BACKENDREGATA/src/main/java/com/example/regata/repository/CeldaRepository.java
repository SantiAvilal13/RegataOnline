package com.example.regata.repository;

import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, Long> {
    
    /**
     * Buscar celdas por mapa
     */
    List<Celda> findByMapa(Mapa mapa);
    
    /**
     * Buscar celdas por mapa y coordenadas específicas
     */
    Optional<Celda> findByMapaAndCoordXAndCoordY(Mapa mapa, Integer coordX, Integer coordY);
    
    /**
     * Buscar celdas por tipo en un mapa específico
     */
    List<Celda> findByMapaAndTipo(Mapa mapa, Celda.Tipo tipo);
    
    /**
     * Buscar celdas por tipo en todos los mapas
     */
    List<Celda> findByTipo(Celda.Tipo tipo);
    
    /**
     * Buscar celdas de agua en un mapa específico
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND c.tipo = 'AGUA'")
    List<Celda> findCeldasAguaByMapa(@Param("mapa") Mapa mapa);
    
    /**
     * Buscar celdas de pared en un mapa específico
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND c.tipo = 'PARED'")
    List<Celda> findCeldasParedByMapa(@Param("mapa") Mapa mapa);
    
    /**
     * Buscar celdas de partida en un mapa específico
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND c.tipo = 'PARTIDA'")
    List<Celda> findCeldasPartidaByMapa(@Param("mapa") Mapa mapa);
    
    /**
     * Buscar celdas de meta en un mapa específico
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND c.tipo = 'META'")
    List<Celda> findCeldasMetaByMapa(@Param("mapa") Mapa mapa);
    
    /**
     * Buscar celdas en un rango de coordenadas
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa " +
           "AND c.coordX >= :minX AND c.coordX <= :maxX " +
           "AND c.coordY >= :minY AND c.coordY <= :maxY")
    List<Celda> findByMapaAndRangoCoordenadas(@Param("mapa") Mapa mapa, 
                                              @Param("minX") Integer minX, 
                                              @Param("maxX") Integer maxX,
                                              @Param("minY") Integer minY, 
                                              @Param("maxY") Integer maxY);
    
    /**
     * Contar celdas por tipo en un mapa
     */
    @Query("SELECT COUNT(c) FROM Celda c WHERE c.mapa = :mapa AND c.tipo = :tipo")
    Long countByMapaAndTipo(@Param("mapa") Mapa mapa, @Param("tipo") Celda.Tipo tipo);
    
    /**
     * Contar celdas por tipo en todos los mapas
     */
    Long countByTipo(Celda.Tipo tipo);
    
    /**
     * Buscar celdas ordenadas por coordenadas
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa ORDER BY c.coordX ASC, c.coordY ASC")
    List<Celda> findByMapaOrderByCoordenadas(@Param("mapa") Mapa mapa);
    
    /**
     * Eliminar todas las celdas de un mapa
     */
    void deleteByMapa(Mapa mapa);
    
    /**
     * Verificar si existe una celda en coordenadas específicas
     */
    boolean existsByMapaAndCoordXAndCoordY(Mapa mapa, Integer coordX, Integer coordY);
    
    /**
     * Buscar celdas navegables (agua y partida) en un mapa
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND (c.tipo = 'AGUA' OR c.tipo = 'PARTIDA')")
    List<Celda> findCeldasNavegablesByMapa(@Param("mapa") Mapa mapa);
    
    /**
     * Buscar celdas de destino (meta) en un mapa
     */
    @Query("SELECT c FROM Celda c WHERE c.mapa = :mapa AND c.tipo = 'META'")
    List<Celda> findCeldasDestinoByMapa(@Param("mapa") Mapa mapa);
}