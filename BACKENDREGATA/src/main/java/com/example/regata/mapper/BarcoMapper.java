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
        dto.setIdBarco(barco.getIdBarco());
        dto.setAlias(barco.getAlias());
        
        if (barco.getUsuario() != null) {
            dto.setUsuarioId(barco.getUsuario().getIdUsuario());
            dto.setUsuarioNombre(barco.getUsuario().getNombre());
        }
        
        if (barco.getModelo() != null) {
            dto.setModeloId(barco.getModelo().getIdModelo());
            dto.setModeloNombre(barco.getModelo().getNombre());
            dto.setModeloColorHex(barco.getModelo().getColorHex());
        }
        
        return dto;
    }
    
    public Barco toEntity(BarcoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Barco barco = new Barco();
        barco.setIdBarco(dto.getIdBarco());
        barco.setAlias(dto.getAlias());
        
        // Nota: Los objetos Usuario y Modelo deben ser establecidos externamente
        // ya que el mapper no tiene acceso a los servicios para cargarlos
        
        return barco;
    }
}
