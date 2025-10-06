package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

public class MapaDTO {
    
    private UUID idMapa;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @Positive(message = "El tamaño de filas debe ser positivo")
    private Integer tamFilas;
    
    @Positive(message = "El tamaño de columnas debe ser positivo")
    private Integer tamColumnas;
    
    // Campos adicionales para la vista
    private List<CeldaDTO> celdas;
    private List<PartidaDTO> partidas;
    private Long totalCeldas;
    private Long totalPartidas;
    private Long partidasActivas;
    private Long partidasTerminadas;
    
    // Constructores
    public MapaDTO() {}
    
    public MapaDTO(String nombre, Integer tamFilas, Integer tamColumnas) {
        this.nombre = nombre;
        this.tamFilas = tamFilas;
        this.tamColumnas = tamColumnas;
    }
    
    // Getters y Setters
    public UUID getIdMapa() {
        return idMapa;
    }
    
    public void setIdMapa(UUID idMapa) {
        this.idMapa = idMapa;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getTamFilas() {
        return tamFilas;
    }
    
    public void setTamFilas(Integer tamFilas) {
        this.tamFilas = tamFilas;
    }
    
    public Integer getTamColumnas() {
        return tamColumnas;
    }
    
    public void setTamColumnas(Integer tamColumnas) {
        this.tamColumnas = tamColumnas;
    }
    
    public List<CeldaDTO> getCeldas() {
        return celdas;
    }
    
    public void setCeldas(List<CeldaDTO> celdas) {
        this.celdas = celdas;
    }
    
    public List<PartidaDTO> getPartidas() {
        return partidas;
    }
    
    public void setPartidas(List<PartidaDTO> partidas) {
        this.partidas = partidas;
    }
    
    public Long getTotalCeldas() {
        return totalCeldas;
    }
    
    public void setTotalCeldas(Long totalCeldas) {
        this.totalCeldas = totalCeldas;
    }
    
    public Long getTotalPartidas() {
        return totalPartidas;
    }
    
    public void setTotalPartidas(Long totalPartidas) {
        this.totalPartidas = totalPartidas;
    }
    
    public Long getPartidasActivas() {
        return partidasActivas;
    }
    
    public void setPartidasActivas(Long partidasActivas) {
        this.partidasActivas = partidasActivas;
    }
    
    public Long getPartidasTerminadas() {
        return partidasTerminadas;
    }
    
    public void setPartidasTerminadas(Long partidasTerminadas) {
        this.partidasTerminadas = partidasTerminadas;
    }
    
    // Métodos de utilidad
    public Integer getTamañoTotal() {
        return tamFilas != null && tamColumnas != null ? tamFilas * tamColumnas : 0;
    }
    
    public boolean esCuadrado() {
        return tamFilas != null && tamColumnas != null && tamFilas.equals(tamColumnas);
    }
    
    public String getDimensiones() {
        return tamFilas != null && tamColumnas != null ? tamFilas + "x" + tamColumnas : "0x0";
    }
    
    @Override
    public String toString() {
        return "MapaDTO{" +
                "idMapa=" + idMapa +
                ", nombre='" + nombre + '\'' +
                ", tamFilas=" + tamFilas +
                ", tamColumnas=" + tamColumnas +
                '}';
    }
}

