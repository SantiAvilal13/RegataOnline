package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Barco;
import com.example.regata.repository.BarcoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Barco> findById(UUID id) {
        return barcoRepository.findById(id);
    }
    
    public Barco save(Barco barco) {
        return barcoRepository.save(barco);
    }
    
    public void deleteById(UUID id) {
        if (!barcoRepository.existsById(id)) {
            throw new GameException("No se encontró el barco con ID: " + id);
        }
        barcoRepository.deleteById(id);
    }
    
    public Barco update(UUID id, Barco barco) {
        Barco existingBarco = barcoRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró el barco con ID: " + id));
        
        existingBarco.setAlias(barco.getAlias());
        existingBarco.setUsuario(barco.getUsuario());
        existingBarco.setModelo(barco.getModelo());
        
        return barcoRepository.save(existingBarco);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByUsuarioId(UUID usuarioId) {
        return barcoRepository.findByUsuarioId(usuarioId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByModeloId(UUID modeloId) {
        return barcoRepository.findByModeloId(modeloId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByAliasContainingIgnoreCase(String alias) {
        return barcoRepository.findByAliasContainingIgnoreCase(alias);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByUsuarioIdOrderByAliasAsc(UUID usuarioId) {
        return barcoRepository.findByUsuarioIdOrderByAliasAsc(usuarioId);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findBarcosConParticipaciones() {
        return barcoRepository.findBarcosConParticipaciones();
    }
    
    @Transactional(readOnly = true)
    public List<Barco> findByUsuarioAndModelo(UUID usuarioId, UUID modeloId) {
        return barcoRepository.findByUsuarioAndModelo(usuarioId, modeloId);
    }
    
    @Transactional(readOnly = true)
    public Long countByUsuarioId(UUID usuarioId) {
        return barcoRepository.countByUsuarioId(usuarioId);
    }
}
