package com.example.regata.mapper;

import com.example.regata.dto.PartidaDTO;
import com.example.regata.model.Partida;
import org.springframework.stereotype.Component;

@Component
public class PartidaMapper {

    public PartidaDTO toDTO(Partida partida) {
        if (partida == null) {
            return null;
        }

        PartidaDTO dto = new PartidaDTO();
        dto.setIdPartida(partida.getIdPartida());
        dto.setFechaInicio(partida.getFechaInicio());
        dto.setFechaFin(partida.getFechaFin());
        dto.setEstado(partida.getEstado());
        
        // Mapear información del mapa
        if (partida.getMapa() != null) {
            dto.setMapaId(partida.getMapa().getIdMapa());
            dto.setMapaNombre(partida.getMapa().getNombre());
        }
        
        // Mapear información del ganador
        if (partida.getGanadorUsuario() != null) {
            dto.setGanadorUsuarioId(partida.getGanadorUsuario().getIdUsuario());
            dto.setGanadorUsuarioNombre(partida.getGanadorUsuario().getNombre());
        }
        
        if (partida.getGanadorBarco() != null) {
            dto.setGanadorBarcoId(partida.getGanadorBarco().getIdBarco());
            dto.setGanadorBarcoAlias(partida.getGanadorBarco().getAlias());
        }

        return dto;
    }

    public Partida toEntity(PartidaDTO dto) {
        if (dto == null) {
            return null;
        }

        Partida partida = new Partida();
        partida.setIdPartida(dto.getIdPartida());
        partida.setFechaInicio(dto.getFechaInicio());
        partida.setFechaFin(dto.getFechaFin());
        partida.setEstado(dto.getEstado());
        
        // Nota: Los objetos relacionados (mapa, ganadorUsuario, ganadorBarco) 
        // deberían ser cargados desde el servicio usando los IDs del DTO
        // para evitar problemas de referencias circulares

        return partida;
    }
}
