package com.regataonline.service;

import com.regataonline.model.Jugador;
import com.regataonline.repository.JugadorRepository;
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
    
    // Crear un nuevo jugador
    public Jugador crearJugador(Jugador jugador) {
        // Validar que el email no exista
        if (jugadorRepository.existsByEmail(jugador.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con el email: " + jugador.getEmail());
        }
        
        // Validaciones básicas
        if (jugador.getNombre() == null || jugador.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del jugador es obligatorio");
        }
        
        if (jugador.getEmail() == null || jugador.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email del jugador es obligatorio");
        }
        
        // Inicializar valores por defecto si no están establecidos
        if (jugador.getExperiencia() == null) {
            jugador.setExperiencia(0);
        }
        
        if (jugador.getVictorias() == null) {
            jugador.setVictorias(0);
        }
        
        return jugadorRepository.save(jugador);
    }
    
    // Obtener todos los jugadores
    @Transactional(readOnly = true)
    public List<Jugador> obtenerTodosLosJugadores() {
        return jugadorRepository.findAll();
    }
    
    // Obtener jugador por ID
    @Transactional(readOnly = true)
    public Optional<Jugador> obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public Optional<Jugador> obtenerPorId(Long id) {
        return obtenerJugadorPorId(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Jugador> obtenerTodos() {
        return obtenerTodosLosJugadores();
    }
    
    // Obtener jugador por email
    @Transactional(readOnly = true)
    public Optional<Jugador> obtenerJugadorPorEmail(String email) {
        return jugadorRepository.findByEmail(email);
    }
    
    // Buscar jugadores por nombre
    @Transactional(readOnly = true)
    public List<Jugador> buscarJugadoresPorNombre(String nombre) {
        return jugadorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Actualizar jugador
    public Jugador actualizarJugador(Long id, Jugador jugadorActualizado) {
        Optional<Jugador> jugadorExistente = jugadorRepository.findById(id);
        
        if (jugadorExistente.isEmpty()) {
            throw new RuntimeException("Jugador no encontrado con ID: " + id);
        }
        
        Jugador jugador = jugadorExistente.get();
        
        // Validar email único si se está cambiando
        if (!jugador.getEmail().equals(jugadorActualizado.getEmail()) && 
            jugadorRepository.existsByEmail(jugadorActualizado.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con el email: " + jugadorActualizado.getEmail());
        }
        
        // Actualizar campos
        jugador.setNombre(jugadorActualizado.getNombre());
        jugador.setEmail(jugadorActualizado.getEmail());
        jugador.setTelefono(jugadorActualizado.getTelefono());
        
        // Solo actualizar experiencia y victorias si se proporcionan valores válidos
        if (jugadorActualizado.getExperiencia() != null) {
            jugador.setExperiencia(jugadorActualizado.getExperiencia());
        }
        
        if (jugadorActualizado.getVictorias() != null) {
            jugador.setVictorias(jugadorActualizado.getVictorias());
        }
        
        return jugadorRepository.save(jugador);
    }
    
    // Eliminar jugador
    public void eliminarJugador(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new RuntimeException("Jugador no encontrado con ID: " + id);
        }
        
        // Verificar si el jugador tiene barcos activos
        Long barcosActivos = jugadorRepository.countBarcosActivosByJugadorId(id);
        if (barcosActivos > 0) {
            throw new RuntimeException("No se puede eliminar el jugador porque tiene " + barcosActivos + " barcos activos");
        }
        
        jugadorRepository.deleteById(id);
    }
    
    // Incrementar experiencia del jugador
    public Jugador incrementarExperiencia(Long id, Integer experienciaGanada) {
        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);
        
        if (jugadorOpt.isEmpty()) {
            throw new RuntimeException("Jugador no encontrado con ID: " + id);
        }
        
        Jugador jugador = jugadorOpt.get();
        jugador.setExperiencia(jugador.getExperiencia() + experienciaGanada);
        
        return jugadorRepository.save(jugador);
    }
    
    // Incrementar victorias del jugador
    public Jugador incrementarVictorias(Long id) {
        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);
        
        if (jugadorOpt.isEmpty()) {
            throw new RuntimeException("Jugador no encontrado con ID: " + id);
        }
        
        Jugador jugador = jugadorOpt.get();
        jugador.setVictorias(jugador.getVictorias() + 1);
        
        return jugadorRepository.save(jugador);
    }
    
    // Obtener top jugadores por victorias
    @Transactional(readOnly = true)
    public List<Jugador> obtenerTopJugadoresPorVictorias() {
        return jugadorRepository.findTopJugadoresByVictorias();
    }
    
    // Obtener jugadores con barcos activos
    @Transactional(readOnly = true)
    public List<Jugador> obtenerJugadoresConBarcosActivos() {
        return jugadorRepository.findJugadoresConBarcosActivos();
    }
    
    // Contar barcos activos de un jugador
    @Transactional(readOnly = true)
    public Long contarBarcosActivos(Long jugadorId) {
        return jugadorRepository.countBarcosActivosByJugadorId(jugadorId);
    }
}
