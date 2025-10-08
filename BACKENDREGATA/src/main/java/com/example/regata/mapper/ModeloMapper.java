package com.example.regata.mapper;

import com.example.regata.dto.ModeloDTO;
import com.example.regata.model.Modelo;
import org.springframework.stereotype.Component;

@Component
public class ModeloMapper {
    
    public ModeloDTO toDTO(Modelo modelo) {
        if (modelo == null) {
            return null;
        }
        
        ModeloDTO dto = new ModeloDTO();
        dto.setIdModelo(modelo.getIdModelo());
        dto.setNombre(modelo.getNombre());
        dto.setColorHex(modelo.getColorHex());
        
        // No incluimos relaciones para evitar referencias circulares
        
        return dto;
    }
    
    public Modelo toEntity(ModeloDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Modelo modelo = new Modelo();
        modelo.setIdModelo(dto.getIdModelo());
        modelo.setNombre(dto.getNombre());
        modelo.setColorHex(dto.getColorHex());
        
        return modelo;
    }
}
