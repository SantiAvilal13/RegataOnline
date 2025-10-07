package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class BarcoDTO {
    
    private Long idBarco;
    
    @NotBlank(message = "El alias es obligatorio")
    @Size(min = 2, max = 50, message = "El alias debe tener entre 2 y 50 caracteres")
    private String alias;
    
    @NotNull(message = "El usuario propietario es obligatorio")
    private Long usuarioId;
    
    private String usuarioNombre;
    
    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;
    
    private String modeloNombre;
    private String modeloColorHex;
    
    // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
    private Long totalParticipaciones;
    private Long partidasGanadas;
    
    // Constructores
    public BarcoDTO() {}
    
    public BarcoDTO(String alias, Long usuarioId, Long modeloId) {
        this.alias = alias;
        this.usuarioId = usuarioId;
        this.modeloId = modeloId;
    }
    
    // Getters y Setters
    public Long getIdBarco() {
        return idBarco;
    }

    public void setIdBarco(Long idBarco) {
        this.idBarco = idBarco;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }
    
    public String getModeloNombre() {
        return modeloNombre;
    }
    
    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
    }
    
    public String getModeloColorHex() {
        return modeloColorHex;
    }
    
    public void setModeloColorHex(String modeloColorHex) {
        this.modeloColorHex = modeloColorHex;
    }
    
    // Métodos de relaciones removidos para evitar referencias circulares
    
    public Long getTotalParticipaciones() {
        return totalParticipaciones;
    }
    
    public void setTotalParticipaciones(Long totalParticipaciones) {
        this.totalParticipaciones = totalParticipaciones;
    }
    
    public Long getPartidasGanadas() {
        return partidasGanadas;
    }
    
    public void setPartidasGanadas(Long partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }
    
    @Override
    public String toString() {
        return "BarcoDTO{" +
                "idBarco=" + idBarco +
                ", alias='" + alias + '\'' +
                ", usuarioId=" + usuarioId +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", modeloId=" + modeloId +
                ", modeloNombre='" + modeloNombre + '\'' +
                ", modeloColorHex='" + modeloColorHex + '\'' +
                '}';
    }
}
