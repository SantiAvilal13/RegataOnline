package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Barco;
import com.example.regata.repository.BarcoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BarcoService {
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Transactional(readOnly = true)
    public List<Barco> findAll() {
        return barcoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Barco> findById(Long id) {
        return barcoRepository.findById(id);
    }
    
    public Barco save(Barco barco) {
        // Inicializar estadísticas basadas en el modelo si es un barco nuevo
        if (barco.getId() == null && barco.getModelo() != null) {
            barco.setVelocidadActual(barco.getModelo().getVelocidadMaxima());
            barco.setResistenciaActual(barco.getModelo().getResistencia());
            barco.setManiobrabilidadActual(barco.getModelo().getManiobrabilidad());
        }
        // Inicializar puntos ganados si es null
        if (barco.getPuntosGanados() == null) {
            barco.setPuntosGanados(0);
        }
        return barcoRepository.save(barco);
    }
    
    public void deleteById(Long id) {
        if (!barcoRepository.existsById(id)) {
            throw new GameException("No se encontró el barco con ID: " + id);
        }
        barcoRepository.deleteById(id);
    }
    
    public Barco update(Long id, Barco barco) {
        Barco existingBarco = barcoRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró el barco con ID: " + id));
        
        existingBarco.setNombre(barco.getNombre());
        existingBarco.setJugador(barco.getJugador());
        existingBarco.setModelo(barco.getModelo());
        
        // Actualizar estadísticas si cambió el modelo
        if (barco.getModelo() != null && !barco.getModelo().equals(existingBarco.getModelo())) {
            existingBarco.setResistenciaActual(barco.getModelo().getResistencia());
            existingBarco.setManiobrabilidadActual(barco.getModelo().getManiobrabilidad());
        }
        
        return barcoRepository.save(existingBarco);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByJugadorId(Long jugadorId) {
        return barcoRepository.findByJugadorId(jugadorId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByModeloId(Long modeloId) {
        return barcoRepository.findByModeloId(modeloId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByNombreContaining(String nombre) {
        return barcoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByJugadorIdOrderByPuntosGanadosDesc(Long jugadorId) {
        return barcoRepository.findByJugadorIdOrderByPuntosGanadosDesc(jugadorId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findAllOrderByPuntosGanadosDesc() {
        return barcoRepository.findAllOrderByPuntosGanadosDesc();
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findBarcosEnMovimiento() {
        return barcoRepository.findBarcosEnMovimiento();
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByJugadorAndModelo(Long jugadorId, Long modeloId) {
        return barcoRepository.findByJugadorAndModelo(jugadorId, modeloId);
    }
    
    public void ganarPuntos(Long barcoId, Integer puntos) {
        Barco barco = barcoRepository.findById(barcoId)
                .orElseThrow(() -> new GameException("No se encontró el barco con ID: " + barcoId));
        barco.ganarPuntos(puntos);
        barcoRepository.save(barco);
    }
    
    public void resetearEstadisticas(Long barcoId) {
        Barco barco = barcoRepository.findById(barcoId)
                .orElseThrow(() -> new GameException("No se encontró el barco con ID: " + barcoId));
        barco.resetearEstadisticas();
        barcoRepository.save(barco);
    }
}
