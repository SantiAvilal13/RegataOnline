package com.example.regata.mapper;

import com.example.regata.dto.BarcoDTO;
import com.example.regata.model.Barco;
import org.springframework.stereotype.Component;

@Component
public class BarcoMapper {
    
    public BarcoDTO toDTO(Barco barco) {
        if (barco == null) {
            return null;
        }
        
        BarcoDTO dto = new BarcoDTO();
        dto.setId(barco.getId());
        dto.setNombre(barco.getNombre());
        dto.setPuntosGanados(barco.getPuntosGanados());
        dto.setVelocidadActual(barco.getVelocidadActual());
        dto.setResistenciaActual(barco.getResistenciaActual());
        dto.setManiobrabilidadActual(barco.getManiobrabilidadActual());
        
        if (barco.getJugador() != null) {
            dto.setJugadorId(barco.getJugador().getId());
            dto.setJugadorNombre(barco.getJugador().getNombre());
        }
        
        if (barco.getModelo() != null) {
            dto.setModeloId(barco.getModelo().getId());
            dto.setModeloNombre(barco.getModelo().getNombre());
        }
        
        return dto;
    }
    
    public Barco toEntity(BarcoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Barco barco = new Barco();
        barco.setId(dto.getId());
        barco.setNombre(dto.getNombre());
        barco.setPuntosGanados(dto.getPuntosGanados());
        barco.setVelocidadActual(dto.getVelocidadActual());
        barco.setResistenciaActual(dto.getResistenciaActual());
        barco.setManiobrabilidadActual(dto.getManiobrabilidadActual());
        
        return barco;
    }
}
