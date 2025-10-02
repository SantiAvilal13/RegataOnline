package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Modelo;
import com.example.regata.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModeloService {
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    @Transactional(readOnly = true)
    public List<Modelo> findAll() {
        return modeloRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Modelo> findById(Long id) {
        return modeloRepository.findById(id);
    }
    
    public Modelo save(Modelo modelo) {
        return modeloRepository.save(modelo);
    }
    
    public void deleteById(Long id) {
        if (!modeloRepository.existsById(id)) {
            throw new GameException("No se encontró el modelo con ID: " + id);
        }
        
        // Verificar si hay barcos usando este modelo
        Long barcosCount = countBarcosByModeloId(id);
        if (barcosCount > 0) {
            throw new GameException("No se puede eliminar el modelo porque tiene " + barcosCount + " barcos asociados");
        }
        
        modeloRepository.deleteById(id);
    }
    
    public Modelo update(Long id, Modelo modelo) {
        Modelo existingModelo = modeloRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró el modelo con ID: " + id));
        
        existingModelo.setNombre(modelo.getNombre());
        existingModelo.setDescripcion(modelo.getDescripcion());
        existingModelo.setVelocidadMaxima(modelo.getVelocidadMaxima());
        existingModelo.setResistencia(modelo.getResistencia());
        existingModelo.setManiobrabilidad(modelo.getManiobrabilidad());
        
        return modeloRepository.save(existingModelo);
    }
    
    @Transactional(readOnly = true)
    public List<Modelo> findByNombreContaining(String nombre) {
        return modeloRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Modelo> findByVelocidadMaximaGreaterThanEqual(Integer velocidadMinima) {
        return modeloRepository.findByVelocidadMaximaGreaterThanEqual(velocidadMinima);
    }
    
    @Transactional(readOnly = true)
    public List<Modelo> findByResistenciaGreaterThanEqual(Integer resistenciaMinima) {
        return modeloRepository.findByResistenciaGreaterThanEqual(resistenciaMinima);
    }
    
    @Transactional(readOnly = true)
    public List<Modelo> findByManiobrabilidadGreaterThanEqual(Integer maniobrabilidadMinima) {
        return modeloRepository.findByManiobrabilidadGreaterThanEqual(maniobrabilidadMinima);
    }
    
    @Transactional(readOnly = true)
    public Long countBarcosByModeloId(Long modeloId) {
        return modeloRepository.countBarcosByModeloId(modeloId);
    }
}
