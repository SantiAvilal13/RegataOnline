package com.example.regata.mapper;

import com.example.regata.dto.ParticipacionDTO;
import com.example.regata.dto.EstadoActualDTO;
import com.example.regata.model.Participacion;
import com.example.regata.model.Movimiento;
import org.springframework.stereotype.Component;

@Component
public class ParticipacionMapper {
    
    public ParticipacionDTO toDTO(Participacion participacion) {
        if (participacion == null) {
            return null;
        }
        
        ParticipacionDTO dto = new ParticipacionDTO();
        dto.setIdParticipacion(participacion.getIdParticipacion());
        dto.setEstado(participacion.getEstado());
        dto.setOrdenTurno(participacion.getOrdenTurno());
        dto.setFechaInicio(participacion.getFechaInicio());
        dto.setFechaFin(participacion.getFechaFin());
        
        // Información de la partida
        if (participacion.getPartida() != null) {
            dto.setPartidaId(participacion.getPartida().getIdPartida());
            dto.setPartidaNombre(participacion.getPartida().getMapa() != null ? 
                participacion.getPartida().getMapa().getNombre() : null);
        }
        
        // Información del barco
        if (participacion.getBarco() != null) {
            dto.setBarcoId(participacion.getBarco().getIdBarco());
            dto.setBarcoAlias(participacion.getBarco().getAlias());
        }
        
        // Información del jugador
        if (participacion.getJugador() != null) {
            dto.setJugadorId(participacion.getJugador().getIdUsuario());
            dto.setJugadorNombre(participacion.getJugador().getNombre());
        }
        
        // Información de la celda inicial
        if (participacion.getCeldaInicial() != null) {
            dto.setCeldaInicialId(participacion.getCeldaInicial().getIdCelda());
            dto.setCeldaInicialInfo("(" + participacion.getCeldaInicial().getCoordX() + 
                                  "," + participacion.getCeldaInicial().getCoordY() + ")");
        }
        
        return dto;
    }
    
    public Participacion toEntity(ParticipacionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Participacion participacion = Participacion.builder()
                .idParticipacion(dto.getIdParticipacion())
                .estado(dto.getEstado())
                .ordenTurno(dto.getOrdenTurno())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .build();
        
        return participacion;
    }
    
    public EstadoActualDTO toEstadoActualDTO(Participacion participacion, Movimiento ultimoMovimiento) {
        if (participacion == null) {
            return null;
        }
        
        EstadoActualDTO dto = new EstadoActualDTO(participacion, ultimoMovimiento);
        
        // Calcular total de movimientos si se proporciona
        if (participacion.getMovimientos() != null) {
            dto.setTotalMovimientos((long) participacion.getMovimientos().size());
        }
        
        return dto;
    }
}
