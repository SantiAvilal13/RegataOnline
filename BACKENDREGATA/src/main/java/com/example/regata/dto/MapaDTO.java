package com.example.regata.dto;

import com.example.regata.model.Celda;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapaDTO {
    
    private Long idMapa;
    private String nombre;
    private Integer tamFilas;
    private Integer tamColumnas;
    private List<CeldaDTO> celdas;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CeldaDTO {
        private Long idCelda;
        private Integer coordX;
        private Integer coordY;
        private Celda.Tipo tipo;
        // NO incluimos la referencia al mapa para evitar referencias circulares
    }
}