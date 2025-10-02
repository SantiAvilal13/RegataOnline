package com.example.regata.service;

import com.example.regata.exception.GameException;
import com.example.regata.model.Jugador;
import com.example.regata.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JugadorService {
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Transactional(readOnly = true)
    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }
    
    public Jugador save(Jugador jugador) {
        // Solo verificar email duplicado para nuevos jugadores (ID == null)
        if (jugador.getId() == null && jugador.getEmail() != null && existsByEmail(jugador.getEmail())) {
            throw new GameException("Ya existe un jugador con este email: " + jugador.getEmail());
        }
        return jugadorRepository.save(jugador);
    }
    
    public void deleteById(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new GameException("No se encontró el jugador con ID: " + id);
        }
        jugadorRepository.deleteById(id);
    }
    
    public Jugador update(Long id, Jugador jugador) {
        Jugador existingJugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new GameException("No se encontró el jugador con ID: " + id));
        
        // Verificar si el email ya existe en otro jugador
        if (jugador.getEmail() != null && !jugador.getEmail().equals(existingJugador.getEmail())) {
            if (existsByEmail(jugador.getEmail())) {
                throw new GameException("Ya existe un jugador con este email: " + jugador.getEmail());
            }
        }
        
        existingJugador.setNombre(jugador.getNombre());
        existingJugador.setEmail(jugador.getEmail());
        existingJugador.setPuntosTotales(jugador.getPuntosTotales());
        
        return jugadorRepository.save(existingJugador);
    }
    
    @Transactional(readOnly = true)
    public Optional<Jugador> findByEmail(String email) {
        return jugadorRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Jugador> findByNombreContaining(String nombre) {
        return jugadorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Jugador> findAllOrderByPuntosTotalesDesc() {
        return jugadorRepository.findAllOrderByPuntosTotalesDesc();
    }
    
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jugadorRepository.existsByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Long countBarcosByJugadorId(Long jugadorId) {
        return jugadorRepository.countBarcosByJugadorId(jugadorId);
    }
}
