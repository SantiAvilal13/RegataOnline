package com.example.regata.mapper;

import com.example.regata.dto.MovimientoDTO;
import com.example.regata.model.Movimiento;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {
    
    public MovimientoDTO toDTO(Movimiento movimiento) {
        if (movimiento == null) {
            return null;
        }
        
        MovimientoDTO dto = new MovimientoDTO();
        dto.setIdMovimiento(movimiento.getIdMovimiento());
        dto.setTurno(movimiento.getTurno());
        dto.setPosX(movimiento.getPosX());
        dto.setPosY(movimiento.getPosY());
        dto.setVelX(movimiento.getVelX());
        dto.setVelY(movimiento.getVelY());
        dto.setDeltaVx(movimiento.getDeltaVx());
        dto.setDeltaVy(movimiento.getDeltaVy());
        dto.setResultado(movimiento.getResultado());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        
        // Información de la participación
        if (movimiento.getParticipacion() != null) {
            dto.setParticipacionId(movimiento.getParticipacion().getIdParticipacion());
            dto.setParticipacionInfo(movimiento.getParticipacion().getJugador() != null ? 
                movimiento.getParticipacion().getJugador().getNombre() + " - " + 
                movimiento.getParticipacion().getBarco().getAlias() : null);
        }
        
        // Información de la celda destino
        if (movimiento.getCeldaDestino() != null) {
            dto.setCeldaDestinoId(movimiento.getCeldaDestino().getIdCelda());
            dto.setCeldaDestinoInfo("(" + movimiento.getCeldaDestino().getCoordX() + 
                                  "," + movimiento.getCeldaDestino().getCoordY() + ") - " + 
                                  movimiento.getCeldaDestino().getTipo());
        }
        
        return dto;
    }
    
    public Movimiento toEntity(MovimientoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Movimiento movimiento = Movimiento.builder()
                .idMovimiento(dto.getIdMovimiento())
                .turno(dto.getTurno())
                .posX(dto.getPosX())
                .posY(dto.getPosY())
                .velX(dto.getVelX())
                .velY(dto.getVelY())
                .deltaVx(dto.getDeltaVx())
                .deltaVy(dto.getDeltaVy())
                .resultado(dto.getResultado())
                .fechaMovimiento(dto.getFechaMovimiento())
                .build();
        
        return movimiento;
    }
}
