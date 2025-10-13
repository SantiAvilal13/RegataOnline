package com.example.regata.service.impl;

import com.example.regata.exception.GameException;
import com.example.regata.model.Participacion;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.model.Celda;
import com.example.regata.model.Movimiento;
import com.example.regata.repository.ParticipacionRepository;
import com.example.regata.repository.MovimientoRepository;
import com.example.regata.service.ParticipacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParticipacionServiceImpl implements ParticipacionService {
    
    @Autowired
    private ParticipacionRepository participacionRepository;
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findAll() {
        return participacionRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Participacion> findById(Long id) {
        return participacionRepository.findById(id);
    }
    
    @Override
    public Participacion save(Participacion participacion) {
        return participacionRepository.save(participacion);
    }
    
    @Override
    public void deleteById(Long id) {
        if (!participacionRepository.existsById(id)) {
            throw new GameException("No se encontró la participación con ID: " + id);
        }
        participacionRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findByPartidaId(Long partidaId) {
        return participacionRepository.findByPartidaId(partidaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findByJugadorId(Long usuarioId) {
        return participacionRepository.findByJugadorId(usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findByBarcoId(Long barcoId) {
        return participacionRepository.findByBarcoId(barcoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findByEstado(Participacion.Estado estado) {
        return participacionRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findParticipacionesActivasByPartidaId(Long partidaId) {
        return participacionRepository.findParticipacionesActivasByPartidaId(partidaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Participacion> findByPartidaIdAndJugadorId(Long partidaId, Long usuarioId) {
        return participacionRepository.findByPartidaIdAndJugadorId(partidaId, usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Participacion> findByPartidaIdAndBarcoId(Long partidaId, Long barcoId) {
        return participacionRepository.findByPartidaIdAndBarcoId(partidaId, barcoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findByPartidaIdOrderByOrdenTurnoAsc(Long partidaId) {
        return participacionRepository.findByPartidaIdOrderByOrdenTurnoAsc(partidaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Participacion> findParticipacionesEnMetaByPartidaId(Long partidaId) {
        return participacionRepository.findParticipacionesEnMetaByPartidaId(partidaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPartidaIdAndJugadorId(Long partidaId, Long usuarioId) {
        return participacionRepository.existsByPartidaIdAndJugadorId(partidaId, usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPartidaIdAndBarcoId(Long partidaId, Long barcoId) {
        return participacionRepository.existsByPartidaIdAndBarcoId(partidaId, barcoId);
    }
    
    @Override
    public Participacion crearParticipacion(Partida partida, Usuario jugador, Barco barco, Celda celdaInicial, Integer ordenTurno) {
        // Validar que no existe ya una participación para este jugador en esta partida
        if (existsByPartidaIdAndJugadorId(partida.getIdPartida(), jugador.getIdUsuario())) {
            throw new GameException("El jugador " + jugador.getNombre() + " ya participa en la partida " + partida.getIdPartida());
        }
        
        // Validar que no existe ya una participación para este barco en esta partida
        if (existsByPartidaIdAndBarcoId(partida.getIdPartida(), barco.getIdBarco())) {
            throw new GameException("El barco " + barco.getAlias() + " ya participa en la partida " + partida.getIdPartida());
        }
        
        // Crear la participación
        Participacion participacion = Participacion.builder()
                .partida(partida)
                .jugador(jugador)
                .barco(barco)
                .celdaInicial(celdaInicial)
                .ordenTurno(ordenTurno)
                .estado(Participacion.Estado.ACTIVO)
                .build();
        
        // Guardar la participación
        participacion = participacionRepository.save(participacion);
        
        // Crear el movimiento inicial (turno 0)
        Movimiento movimientoInicial = Movimiento.movimientoInicial(participacion, celdaInicial);
        movimientoRepository.save(movimientoInicial);
        
        return participacion;
    }
    
    @Override
    public void destruirParticipacion(Long participacionId) {
        Participacion participacion = participacionRepository.findById(participacionId)
                .orElseThrow(() -> new GameException("No se encontró la participación con ID: " + participacionId));
        
        participacion.destruir();
        participacionRepository.save(participacion);
    }
    
    @Override
    public void marcarComoEnMeta(Long participacionId) {
        Participacion participacion = participacionRepository.findById(participacionId)
                .orElseThrow(() -> new GameException("No se encontró la participación con ID: " + participacionId));
        
        participacion.llegarAMeta();
        participacionRepository.save(participacion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByPartidaIdAndEstado(Long partidaId, Participacion.Estado estado) {
        return participacionRepository.countByPartidaIdAndEstado(partidaId, estado);
    }
}
