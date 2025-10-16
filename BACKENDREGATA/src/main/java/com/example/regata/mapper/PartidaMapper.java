package com.example.regata.mapper;

import com.example.regata.dto.PartidaDTO;
import com.example.regata.model.Partida;
import com.example.regata.model.Mapa;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.service.MapaService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.BarcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PartidaMapper {

    @Autowired
    private MapaService mapaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BarcoService barcoService;

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
        
        // Cargar el mapa si se proporciona el ID
        if (dto.getMapaId() != null) {
            mapaService.findById(dto.getMapaId()).ifPresent(partida::setMapa);
        }
        
        // Cargar el usuario ganador si se proporciona el ID
        if (dto.getGanadorUsuarioId() != null) {
            usuarioService.findById(dto.getGanadorUsuarioId()).ifPresent(partida::setGanadorUsuario);
        }
        
        // Cargar el barco ganador si se proporciona el ID
        if (dto.getGanadorBarcoId() != null) {
            barcoService.findById(dto.getGanadorBarcoId()).ifPresent(partida::setGanadorBarco);
        }

        return partida;
    }
}
