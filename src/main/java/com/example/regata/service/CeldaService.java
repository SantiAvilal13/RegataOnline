package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.repository.CeldaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.lang.Long;

@Service
@Transactional
public class CeldaService {
    
    @Autowired
    private CeldaRepository celdaRepository;
    
    @Transactional(readOnly = true)
    public List<Celda> findAll() {
        return celdaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Celda> findById(Long id) {
        return celdaRepository.findById(id);
    }
    
    @Transactional
    public Celda save(Celda celda) {
        // Validar coordenadas
        if (celda.getCoordX() < 0 || celda.getCoordY() < 0) {
            throw new GameException("Las coordenadas de la celda deben ser no negativas");
        }
        
        // Validar que el mapa existe
        if (celda.getMapa() == null) {
            throw new GameException("La celda debe estar asociada a un mapa");
        }
        
        // Verificar si ya existe una celda en esas coordenadas
        if (celdaRepository.existsByMapaAndCoordXAndCoordY(celda.getMapa(), celda.getCoordX(), celda.getCoordY())) {
            throw new GameException("Ya existe una celda en las coordenadas (" + celda.getCoordX() + ", " + celda.getCoordY() + ")");
        }
        
        return celdaRepository.save(celda);
    }
    
    @Transactional
    public Celda saveWithValidation(Celda celda, int tamFilas, int tamColumnas) {
        // Validar coordenadas
        if (celda.getCoordX() < 0 || celda.getCoordY() < 0) {
            throw new GameException("Las coordenadas de la celda deben ser no negativas");
        }
        
        // Validar que el mapa existe
        if (celda.getMapa() == null) {
            throw new GameException("La celda debe estar asociada a un mapa");
        }
        
        // Validar que las coordenadas estén dentro del mapa
        if (celda.getCoordX() >= tamFilas || celda.getCoordY() >= tamColumnas) {
            throw new GameException("Las coordenadas están fuera de los límites del mapa");
        }
        
        // Verificar si ya existe una celda en esas coordenadas
        if (celdaRepository.existsByMapaAndCoordXAndCoordY(celda.getMapa(), celda.getCoordX(), celda.getCoordY())) {
            throw new GameException("Ya existe una celda en las coordenadas (" + celda.getCoordX() + ", " + celda.getCoordY() + ")");
        }
        
        return celdaRepository.save(celda);
    }
    
    public void deleteById(Long id) {
        if (!celdaRepository.existsById(id)) {
            throw new GameException("No se encontró la celda con ID: " + id);
        }
        celdaRepository.deleteById(id);
    }
    
    public Celda update(Long id, Celda celda) {
        Celda existingCelda = celdaRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró la celda con ID: " + id));
        
        // Validar coordenadas
        if (celda.getCoordX() < 0 || celda.getCoordY() < 0) {
            throw new GameException("Las coordenadas de la celda deben ser no negativas");
        }
        
        // Validar que las coordenadas estén dentro del mapa
        if (celda.getCoordX() >= celda.getMapa().getTamFilas() || 
            celda.getCoordY() >= celda.getMapa().getTamColumnas()) {
            throw new GameException("Las coordenadas están fuera de los límites del mapa");
        }
        
        existingCelda.setCoordX(celda.getCoordX());
        existingCelda.setCoordY(celda.getCoordY());
        existingCelda.setTipo(celda.getTipo());
        
        return celdaRepository.save(existingCelda);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findByMapa(Mapa mapa) {
        return celdaRepository.findByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public Optional<Celda> findByMapaAndCoordenadas(Mapa mapa, Integer coordX, Integer coordY) {
        return celdaRepository.findByMapaAndCoordXAndCoordY(mapa, coordX, coordY);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findByMapaAndTipo(Mapa mapa, Celda.Tipo tipo) {
        return celdaRepository.findByMapaAndTipo(mapa, tipo);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findByTipo(Celda.Tipo tipo) {
        return celdaRepository.findByTipo(tipo);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasAguaByMapa(Mapa mapa) {
        return celdaRepository.findCeldasAguaByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasParedByMapa(Mapa mapa) {
        return celdaRepository.findCeldasParedByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasPartidaByMapa(Mapa mapa) {
        return celdaRepository.findCeldasPartidaByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasMetaByMapa(Mapa mapa) {
        return celdaRepository.findCeldasMetaByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findByMapaAndRangoCoordenadas(Mapa mapa, Integer minX, Integer maxX, Integer minY, Integer maxY) {
        return celdaRepository.findByMapaAndRangoCoordenadas(mapa, minX, maxX, minY, maxY);
    }
    
    @Transactional(readOnly = true)
    public Long countByMapaAndTipo(Mapa mapa, Celda.Tipo tipo) {
        return celdaRepository.countByMapaAndTipo(mapa, tipo);
    }
    
    @Transactional(readOnly = true)
    public Long countByTipo(Celda.Tipo tipo) {
        return celdaRepository.countByTipo(tipo);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findByMapaOrderByCoordenadas(Mapa mapa) {
        return celdaRepository.findByMapaOrderByCoordenadas(mapa);
    }
    
    public void deleteByMapa(Mapa mapa) {
        celdaRepository.deleteByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByMapaAndCoordenadas(Mapa mapa, Integer coordX, Integer coordY) {
        return celdaRepository.existsByMapaAndCoordXAndCoordY(mapa, coordX, coordY);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasNavegablesByMapa(Mapa mapa) {
        return celdaRepository.findCeldasNavegablesByMapa(mapa);
    }
    
    @Transactional(readOnly = true)
    public List<Celda> findCeldasDestinoByMapa(Mapa mapa) {
        return celdaRepository.findCeldasDestinoByMapa(mapa);
    }
    
    /**
     * Crear una grilla completa de celdas para un mapa
     */
    @Transactional
    public void crearGrillaCompleta(Mapa mapa) {
        // Eliminar celdas existentes si las hay
        deleteByMapa(mapa);
        
        // Guardar las dimensiones en variables locales para evitar LazyInitializationException
        int tamFilas = mapa.getTamFilas();
        int tamColumnas = mapa.getTamColumnas();
        
        // Crear todas las celdas del mapa
        for (int x = 0; x < tamFilas; x++) {
            for (int y = 0; y < tamColumnas; y++) {
                Celda celda = Celda.builder()
                        .coordX(x)
                        .coordY(y)
                        .tipo(Celda.Tipo.AGUA) // Por defecto todas son agua
                        .mapa(mapa)
                        .build();
                
                celdaRepository.save(celda);
            }
        }
    }
    
    /**
     * Crear un mapa con un patrón básico de juego
     */
    @Transactional
    public void crearMapaJugable(Mapa mapa) {
        // Guardar las dimensiones en variables locales para evitar LazyInitializationException
        int tamFilas = mapa.getTamFilas();
        int tamColumnas = mapa.getTamColumnas();
        
        // Crear grilla completa
        crearGrillaCompleta(mapa);
        
        // Crear bordes (paredes)
        for (int x = 0; x < tamFilas; x++) {
            for (int y = 0; y < tamColumnas; y++) {
                // Bordes del mapa son paredes
                if (x == 0 || x == tamFilas - 1 || y == 0 || y == tamColumnas - 1) {
                    Optional<Celda> celdaOpt = findByMapaAndCoordenadas(mapa, x, y);
                    if (celdaOpt.isPresent()) {
                        Celda celda = celdaOpt.get();
                        celda.setTipo(Celda.Tipo.PARED);
                        celdaRepository.save(celda);
                    }
                }
            }
        }
        
        // Agregar punto de partida (esquina superior izquierda, dentro de los bordes)
        if (tamFilas > 2 && tamColumnas > 2) {
            Optional<Celda> celdaPartida = findByMapaAndCoordenadas(mapa, 1, 1);
            if (celdaPartida.isPresent()) {
                Celda celda = celdaPartida.get();
                celda.setTipo(Celda.Tipo.PARTIDA);
                celdaRepository.save(celda);
            }
        }
        
        // Agregar meta (esquina inferior derecha, dentro de los bordes)
        if (tamFilas > 2 && tamColumnas > 2) {
            Optional<Celda> celdaMeta = findByMapaAndCoordenadas(mapa, tamFilas - 2, tamColumnas - 2);
            if (celdaMeta.isPresent()) {
                Celda celda = celdaMeta.get();
                celda.setTipo(Celda.Tipo.META);
                celdaRepository.save(celda);
            }
        }
    }
    
    /**
     * Validar que un mapa es jugable (tiene al menos una celda de partida y una de meta)
     */
    @Transactional(readOnly = true)
    public boolean esMapaJugable(Mapa mapa) {
        List<Celda> celdasPartida = findCeldasPartidaByMapa(mapa);
        List<Celda> celdasMeta = findCeldasMetaByMapa(mapa);
        
        return !celdasPartida.isEmpty() && !celdasMeta.isEmpty();
    }
}
