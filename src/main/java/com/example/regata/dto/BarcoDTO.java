package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public class BarcoDTO {
    
    private UUID idBarco;
    
    @NotBlank(message = "El alias es obligatorio")
    @Size(min = 2, max = 50, message = "El alias debe tener entre 2 y 50 caracteres")
    private String alias;
    
    @NotNull(message = "El usuario propietario es obligatorio")
    private UUID usuarioId;
    
    private String usuarioNombre;
    
    @NotNull(message = "El modelo es obligatorio")
    private UUID modeloId;
    
    private String modeloNombre;
    private String modeloColorHex;
    
    // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
    private Long totalParticipaciones;
    private Long partidasGanadas;
    
    // Constructores
    public BarcoDTO() {}
    
    public BarcoDTO(String alias, UUID usuarioId, UUID modeloId) {
        this.alias = alias;
        this.usuarioId = usuarioId;
        this.modeloId = modeloId;
    }
    
    // Getters y Setters
    public UUID getIdBarco() {
        return idBarco;
    }
    
    public void setIdBarco(UUID idBarco) {
        this.idBarco = idBarco;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public UUID getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public UUID getModeloId() {
        return modeloId;
    }
    
    public void setModeloId(UUID modeloId) {
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
