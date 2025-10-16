package com.example.regata.dto;

import com.example.regata.model.Partida;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class PartidaDTO {
    
    private Long idPartida;
    
    private LocalDateTime fechaInicio;
    
    private LocalDateTime fechaFin;
    
    @NotNull(message = "El estado es obligatorio")
    private Partida.Estado estado;
    
    @NotNull(message = "El mapa es obligatorio")
    private Long mapaId;
    
    private String mapaNombre;
    
    private Long ganadorUsuarioId;
    
    private String ganadorUsuarioNombre;
    
    private Long ganadorBarcoId;
    
    private String ganadorBarcoAlias;
    
    // Campos adicionales para la vista
    private List<ParticipacionDTO> participaciones;
    private Integer totalParticipantes;
    private Long duracionMinutos;
    
    // Constructores
    public PartidaDTO() {}
    
    public PartidaDTO(Long mapaId, Partida.Estado estado) {
        this.mapaId = mapaId;
        this.estado = estado;
        this.fechaInicio = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }
    
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Partida.Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Partida.Estado estado) {
        this.estado = estado;
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
    
    public Long getGanadorUsuarioId() {
        return ganadorUsuarioId;
    }

    public void setGanadorUsuarioId(Long ganadorUsuarioId) {
        this.ganadorUsuarioId = ganadorUsuarioId;
    }
    
    public String getGanadorUsuarioNombre() {
        return ganadorUsuarioNombre;
    }
    
    public void setGanadorUsuarioNombre(String ganadorUsuarioNombre) {
        this.ganadorUsuarioNombre = ganadorUsuarioNombre;
    }
    
    public Long getGanadorBarcoId() {
        return ganadorBarcoId;
    }

    public void setGanadorBarcoId(Long ganadorBarcoId) {
        this.ganadorBarcoId = ganadorBarcoId;
    }
    
    public String getGanadorBarcoAlias() {
        return ganadorBarcoAlias;
    }
    
    public void setGanadorBarcoAlias(String ganadorBarcoAlias) {
        this.ganadorBarcoAlias = ganadorBarcoAlias;
    }
    
    public List<ParticipacionDTO> getParticipaciones() {
        return participaciones;
    }
    
    public void setParticipaciones(List<ParticipacionDTO> participaciones) {
        this.participaciones = participaciones;
    }
    
    public Integer getTotalParticipantes() {
        return totalParticipantes;
    }
    
    public void setTotalParticipantes(Integer totalParticipantes) {
        this.totalParticipantes = totalParticipantes;
    }
    
    public Long getDuracionMinutos() {
        return duracionMinutos;
    }
    
    public void setDuracionMinutos(Long duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
    
    // Métodos de utilidad
    public boolean esActiva() {
        return Partida.Estado.EN_JUEGO.equals(this.estado);
    }
    
    public boolean estaTerminada() {
        return Partida.Estado.TERMINADA.equals(this.estado);
    }
    
    public boolean estaEsperando() {
        return Partida.Estado.ESPERANDO.equals(this.estado);
    }
    
    public String getEstadoDisplayName() {
        return estado != null ? estado.name() : "";
    }
    
    @Override
    public String toString() {
        return "PartidaDTO{" +
                "idPartida=" + idPartida +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                ", mapaNombre='" + mapaNombre + '\'' +
                '}';
    }
}

