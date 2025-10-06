package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Mapa;
import com.example.regata.repository.MapaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MapaService {
    
    @Autowired
    private MapaRepository mapaRepository;
    
    @Transactional(readOnly = true)
    public List<Mapa> findAll() {
        return mapaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Mapa> findById(UUID id) {
        return mapaRepository.findById(id);
    }
    
    public Mapa save(Mapa mapa) {
        // Validar que el mapa tenga dimensiones válidas
        if (mapa.getTamFilas() <= 0 || mapa.getTamColumnas() <= 0) {
            throw new GameException("Las dimensiones del mapa deben ser positivas");
        }
        
        // Validar que el nombre no esté vacío
        if (mapa.getNombre() == null || mapa.getNombre().trim().isEmpty()) {
            throw new GameException("El nombre del mapa no puede estar vacío");
        }
        
        // Verificar si ya existe un mapa con el mismo nombre
        if (mapaRepository.findByNombre(mapa.getNombre()).isPresent()) {
            throw new GameException("Ya existe un mapa con el nombre: " + mapa.getNombre());
        }
        
        return mapaRepository.save(mapa);
    }
    
    public void deleteById(UUID id) {
        if (!mapaRepository.existsById(id)) {
            throw new GameException("No se encontró el mapa con ID: " + id);
        }
        
        // Verificar si el mapa tiene partidas asociadas
        List<Mapa> mapasConPartidas = mapaRepository.findMapasConPartidas();
        if (mapasConPartidas.stream().anyMatch(mapa -> mapa.getIdMapa().equals(id))) {
            throw new GameException("No se puede eliminar el mapa porque tiene partidas asociadas");
        }
        
        mapaRepository.deleteById(id);
    }
    
    public Mapa update(UUID id, Mapa mapa) {
        Mapa existingMapa = mapaRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró el mapa con ID: " + id));
        
        // Validar que el nombre no esté vacío
        if (mapa.getNombre() == null || mapa.getNombre().trim().isEmpty()) {
            throw new GameException("El nombre del mapa no puede estar vacío");
        }
        
        // Verificar si ya existe otro mapa con el mismo nombre
        Optional<Mapa> mapaConMismoNombre = mapaRepository.findByNombre(mapa.getNombre());
        if (mapaConMismoNombre.isPresent() && !mapaConMismoNombre.get().getIdMapa().equals(id)) {
            throw new GameException("Ya existe otro mapa con el nombre: " + mapa.getNombre());
        }
        
        // Validar dimensiones
        if (mapa.getTamFilas() <= 0 || mapa.getTamColumnas() <= 0) {
            throw new GameException("Las dimensiones del mapa deben ser positivas");
        }
        
        existingMapa.setNombre(mapa.getNombre());
        existingMapa.setTamFilas(mapa.getTamFilas());
        existingMapa.setTamColumnas(mapa.getTamColumnas());
        
        return mapaRepository.save(existingMapa);
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findByNombre(String nombre) {
        return mapaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findByTamano(Integer tamFilas, Integer tamColumnas) {
        return mapaRepository.findByTamFilasAndTamColumnas(tamFilas, tamColumnas);
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findByTamanoRango(Integer minFilas, Integer maxFilas, Integer minColumnas, Integer maxColumnas) {
        return mapaRepository.findByTamanoRango(minFilas, maxFilas, minColumnas, maxColumnas);
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findAllOrderByNombre() {
        return mapaRepository.findAllByOrderByNombreAsc();
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findAllOrderByTamano() {
        return mapaRepository.findAllOrderByTamanoAsc();
    }
    
    @Transactional(readOnly = true)
    public Long countByTamano(Integer filas, Integer columnas) {
        return mapaRepository.countByTamano(filas, columnas);
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findMapasConPartidas() {
        return mapaRepository.findMapasConPartidas();
    }
    
    @Transactional(readOnly = true)
    public List<Mapa> findMapasSinPartidas() {
        return mapaRepository.findMapasSinPartidas();
    }
    
    @Transactional(readOnly = true)
    public Optional<Mapa> findByNombreExacto(String nombre) {
        return mapaRepository.findByNombre(nombre);
    }
    
    /**
     * Crear un mapa vacío con celdas de agua por defecto
     */
    public Mapa crearMapaVacio(String nombre, Integer filas, Integer columnas) {
        Mapa mapa = Mapa.builder()
                .nombre(nombre)
                .tamFilas(filas)
                .tamColumnas(columnas)
                .build();
        
        return save(mapa);
    }
    
    /**
     * Validar que un mapa es jugable (tiene al menos una celda de partida y una de meta)
     */
    @Transactional(readOnly = true)
    public boolean esMapaJugable(UUID mapaId) {
        Optional<Mapa> mapaOpt = findById(mapaId);
        if (!mapaOpt.isPresent()) {
            return false;
        }
        
        Mapa mapa = mapaOpt.get();
        // Esta validación se completará cuando implementemos CeldaService
        // Por ahora retornamos true si el mapa existe
        return true;
    }
}
