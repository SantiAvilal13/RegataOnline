package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ModeloDTO {
    
    private Long idModelo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "El color hexadecimal es obligatorio")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "El color debe ser un código hexadecimal válido (ej: #FF0000)")
    private String colorHex;
    
    // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
    private Long cantidadBarcos;
    private Long totalParticipaciones;
    
    // Constructores
    public ModeloDTO() {}
    
    public ModeloDTO(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex = colorHex;
    }
    
    // Getters y Setters
    public Long getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Long idModelo) {
        this.idModelo = idModelo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getColorHex() {
        return colorHex;
    }
    
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    
    // Métodos de relaciones removidos para evitar referencias circulares
    
    public Long getCantidadBarcos() {
        return cantidadBarcos;
    }
    
    public void setCantidadBarcos(Long cantidadBarcos) {
        this.cantidadBarcos = cantidadBarcos;
    }
    
    public Long getTotalParticipaciones() {
        return totalParticipaciones;
    }
    
    public void setTotalParticipaciones(Long totalParticipaciones) {
        this.totalParticipaciones = totalParticipaciones;
    }
    
    @Override
    public String toString() {
        return "ModeloDTO{" +
                "idModelo=" + idModelo +
                ", nombre='" + nombre + '\'' +
                ", colorHex='" + colorHex + '\'' +
                ", cantidadBarcos=" + cantidadBarcos +
                '}';
    }
}
