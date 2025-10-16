package com.example.regata.dto;

import com.example.regata.model.Celda;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CeldaDTO {
    
    private Long idCelda;
    
    @NotNull(message = "La coordenada X es obligatoria")
    private Integer coordX;
    
    @NotNull(message = "La coordenada Y es obligatoria")
    private Integer coordY;
    
    @NotNull(message = "El tipo es obligatorio")
    private Celda.Tipo tipo;
    
    @NotNull(message = "El mapa es obligatorio")
    private Long mapaId;
    
    private String mapaNombre;
    
    // Campos adicionales para la vista
    private List<MovimientoDTO> movimientos;
    private Long totalMovimientos;
    private String colorDisplay;
    
    // Constructores
    public CeldaDTO() {}
    
    public CeldaDTO(Integer coordX, Integer coordY, Celda.Tipo tipo, Long mapaId) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.tipo = tipo;
        this.mapaId = mapaId;
    }
    
    // Getters y Setters
    public Long getIdCelda() {
        return idCelda;
    }

    public void setIdCelda(Long idCelda) {
        this.idCelda = idCelda;
    }
    
    public Integer getCoordX() {
        return coordX;
    }
    
    public void setCoordX(Integer coordX) {
        this.coordX = coordX;
    }
    
    public Integer getCoordY() {
        return coordY;
    }
    
    public void setCoordY(Integer coordY) {
        this.coordY = coordY;
    }
    
    public Celda.Tipo getTipo() {
        return tipo;
    }
    
    public void setTipo(Celda.Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Long getMapaId() {
        return mapaId;
    }

    public void setMapaId(Long mapaId) {
        this.mapaId = mapaId;
    }
    
    public String getMapaNombre() {
        return mapaNombre;
    }
    
    public void setMapaNombre(String mapaNombre) {
        this.mapaNombre = mapaNombre;
    }
    
    public List<MovimientoDTO> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<MovimientoDTO> movimientos) {
        this.movimientos = movimientos;
    }
    
    public Long getTotalMovimientos() {
        return totalMovimientos;
    }
    
    public void setTotalMovimientos(Long totalMovimientos) {
        this.totalMovimientos = totalMovimientos;
    }
    
    public String getColorDisplay() {
        return colorDisplay;
    }
    
    public void setColorDisplay(String colorDisplay) {
        this.colorDisplay = colorDisplay;
    }
    
    // Métodos de utilidad
    public boolean esAgua() {
        return Celda.Tipo.AGUA.equals(this.tipo);
    }
    
    public boolean esPared() {
        return Celda.Tipo.PARED.equals(this.tipo);
    }
    
    public boolean esPartida() {
        return Celda.Tipo.PARTIDA.equals(this.tipo);
    }
    
    public boolean esMeta() {
        return Celda.Tipo.META.equals(this.tipo);
    }
    
    public String getTipoDisplayName() {
        return tipo != null ? tipo.name() : "";
    }
    
    public String getCoordenadas() {
        return coordX != null && coordY != null ? "(" + coordX + "," + coordY + ")" : "(0,0)";
    }
    
    @Override
    public String toString() {
        return "CeldaDTO{" +
                "idCelda=" + idCelda +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                ", tipo=" + tipo +
                ", mapaNombre='" + mapaNombre + '\'' +
                '}';
    }
}

