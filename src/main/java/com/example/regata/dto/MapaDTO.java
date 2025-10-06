package com.example.regata.dto;

import com.example.regata.model.Celda;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapaDTO {
    
    private UUID idMapa;
    private String nombre;
    private Integer tamFilas;
    private Integer tamColumnas;
    private List<CeldaDTO> celdas;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CeldaDTO {
        private UUID idCelda;
        private Integer coordX;
        private Integer coordY;
        private Celda.Tipo tipo;
        // NO incluimos la referencia al mapa para evitar referencias circulares
    }
}