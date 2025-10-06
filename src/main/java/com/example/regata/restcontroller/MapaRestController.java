package com.example.regata.restcontroller;

import com.example.regata.model.Mapa;
import com.example.regata.model.Celda;
import com.example.regata.dto.MapaDTO;
import com.example.regata.service.MapaService;
import com.example.regata.service.CeldaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mapas")
public class MapaRestController {
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    @GetMapping
    public ResponseEntity<List<MapaDTO>> listarMapas() {
        try {
            List<Mapa> mapas = mapaService.findAll();
            List<MapaDTO> mapasDTO = mapas.stream()
                .map(this::convertirAMapaDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(mapasDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MapaDTO> obtenerMapa(@PathVariable String id) {
        try {
            return mapaService.findById(java.util.UUID.fromString(id))
                .map(this::convertirAMapaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}/celdas")
    public ResponseEntity<List<MapaDTO.CeldaDTO>> obtenerCeldas(@PathVariable String id) {
        try {
            return mapaService.findById(java.util.UUID.fromString(id))
                    .map(mapa -> {
                        List<MapaDTO.CeldaDTO> celdasDTO = mapa.getCeldas().stream()
                            .map(this::convertirACeldaDTO)
                            .collect(Collectors.toList());
                        return ResponseEntity.ok(celdasDTO);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}/matriz")
    public ResponseEntity<Map<String, Object>> obtenerMatriz(@PathVariable String id) {
        try {
            return mapaService.findById(java.util.UUID.fromString(id))
                    .map(mapa -> {
                        List<Celda> celdas = celdaService.findByMapa(mapa);
                        
                        // Crear matriz 2D
                        String[][] matriz = new String[mapa.getTamFilas()][mapa.getTamColumnas()];
                        
                        for (Celda celda : celdas) {
                            String tipo = switch (celda.getTipo()) {
                                case AGUA -> " ";
                                case PARED -> "x";
                                case PARTIDA -> "P";
                                case META -> "M";
                                default -> "?";
                            };
                            matriz[celda.getCoordX()][celda.getCoordY()] = tipo;
                        }
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("mapa", mapa);
                        response.put("matriz", matriz);
                        response.put("filas", mapa.getTamFilas());
                        response.put("columnas", mapa.getTamColumnas());
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Métodos de conversión para evitar referencias circulares
    private MapaDTO convertirAMapaDTO(Mapa mapa) {
        return MapaDTO.builder()
                .idMapa(mapa.getIdMapa())
                .nombre(mapa.getNombre())
                .tamFilas(mapa.getTamFilas())
                .tamColumnas(mapa.getTamColumnas())
                .celdas(mapa.getCeldas().stream()
                    .map(this::convertirACeldaDTO)
                    .collect(Collectors.toList()))
                .build();
    }
    
    private MapaDTO.CeldaDTO convertirACeldaDTO(Celda celda) {
        return MapaDTO.CeldaDTO.builder()
                .idCelda(celda.getIdCelda())
                .coordX(celda.getCoordX())
                .coordY(celda.getCoordY())
                .tipo(celda.getTipo())
                .build();
    }
}
