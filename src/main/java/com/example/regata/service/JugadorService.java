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
        System.out.println("\n" + "🔥".repeat(40));
        System.out.println("🚀 JUGADOR SERVICE - MÉTODO SAVE INICIADO");
        System.out.println("🔥".repeat(40));
        System.out.println("📅 Timestamp: " + java.time.LocalDateTime.now());
        System.out.println("🎯 JUGADOR RECIBIDO PARA GUARDAR:");
        System.out.println("   ├── Clase: " + jugador.getClass().getSimpleName());
        System.out.println("   ├── Hash: " + jugador.hashCode());
        System.out.println("   ├── ID: " + jugador.getId() + " (es nuevo: " + (jugador.getId() == null) + ")");
        System.out.println("   ├── Nombre: '" + jugador.getNombre() + "' (length: " + (jugador.getNombre() != null ? jugador.getNombre().length() : 0) + ")");
        System.out.println("   ├── Email: '" + jugador.getEmail() + "' (length: " + (jugador.getEmail() != null ? jugador.getEmail().length() : 0) + ")");
        System.out.println("   └── Puntos: " + jugador.getPuntosTotales() + " (tipo: " + (jugador.getPuntosTotales() != null ? jugador.getPuntosTotales().getClass().getSimpleName() : "null") + ")");

        System.out.println("🔍 VALIDACIONES DE NEGOCIO:");
        boolean isNewPlayer = jugador.getId() == null;
        boolean hasEmail = jugador.getEmail() != null && !jugador.getEmail().trim().isEmpty();
        System.out.println("   ├── Es jugador nuevo: " + isNewPlayer);
        System.out.println("   ├── Tiene email: " + hasEmail);
        
        if (isNewPlayer && hasEmail) {
            System.out.println("   ├── Verificando email duplicado...");
            long checkStartTime = System.currentTimeMillis();
            boolean emailExists = existsByEmail(jugador.getEmail());
            long checkEndTime = System.currentTimeMillis();
            System.out.println("   ├── Tiempo verificación email: " + (checkEndTime - checkStartTime) + "ms");
            System.out.println("   └── Email duplicado: " + emailExists);
            
            if (emailExists) {
                System.out.println("💥 ERROR: EMAIL DUPLICADO");
                System.out.println("   └── Email conflictivo: '" + jugador.getEmail() + "'");
                throw new GameException("Ya existe un jugador con este email: " + jugador.getEmail());
            }
        } else {
            System.out.println("   └── Saltando verificación de email (actualización o email vacío)");
        }

        try {
            System.out.println("💾 GUARDANDO EN REPOSITORIO:");
            System.out.println("   ├── Llamando jugadorRepository.save()...");
            long saveStartTime = System.currentTimeMillis();
            Jugador saved = jugadorRepository.save(jugador);
            long saveEndTime = System.currentTimeMillis();
            System.out.println("   ├── Tiempo de guardado: " + (saveEndTime - saveStartTime) + "ms");
            System.out.println("   └── ✅ GUARDADO EXITOSO EN REPOSITORIO");
            
            System.out.println("🎉 JUGADOR GUARDADO COMPLETAMENTE:");
            System.out.println("   ├── ID asignado/mantenido: " + saved.getId());
            System.out.println("   ├── Nombre final: '" + saved.getNombre() + "'");
            System.out.println("   ├── Email final: '" + saved.getEmail() + "'");
            System.out.println("   ├── Puntos finales: " + saved.getPuntosTotales());
            System.out.println("   └── Hash del objeto guardado: " + saved.hashCode());
            
            System.out.println("✅ SERVICE SAVE COMPLETADO EXITOSAMENTE");
            System.out.println("🔥".repeat(40) + "\n");
            return saved;
            
        } catch (Exception e) {
            System.out.println("💥 ERROR CRÍTICO EN REPOSITORIO:");
            System.out.println("   ├── Tipo de excepción: " + e.getClass().getSimpleName());
            System.out.println("   ├── Mensaje: " + e.getMessage());
            System.out.println("   ├── Causa raíz: " + (e.getCause() != null ? e.getCause().getMessage() : "No hay causa raíz"));
            System.out.println("   └── Stack trace completo:");
            e.printStackTrace();
            System.out.println("❌ SERVICE SAVE TERMINADO CON ERROR");
            System.out.println("🔥".repeat(40) + "\n");
            throw e;
        }
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
