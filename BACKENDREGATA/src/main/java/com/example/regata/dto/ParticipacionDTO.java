package com.example.regata.dto;

import com.example.regata.model.Participacion;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ParticipacionDTO {
    
    private Long idParticipacion;
    
    @NotNull(message = "El estado es obligatorio")
    private Participacion.Estado estado;
    
    @NotNull(message = "El orden de turno es obligatorio")
    private Integer ordenTurno;
    
    private LocalDateTime fechaInicio;
    
    private LocalDateTime fechaFin;
    
    @NotNull(message = "La partida es obligatoria")
    private Long partidaId;
    
    private String partidaNombre;
    
    @NotNull(message = "El barco es obligatorio")
    private Long barcoId;
    
    private String barcoAlias;
    
    @NotNull(message = "El jugador es obligatorio")
    private Long jugadorId;
    
    private String jugadorNombre;
    
    @NotNull(message = "La celda inicial es obligatoria")
    private Long celdaInicialId;
    
    private String celdaInicialInfo;
    
    // Campos adicionales para la vista
    private List<MovimientoDTO> movimientos;
    private Long totalMovimientos;
    private Boolean esGanador;
    
    // Constructores
    public ParticipacionDTO() {}
    
    public ParticipacionDTO(Long partidaId, Long barcoId, Long jugadorId, Long celdaInicialId, Integer ordenTurno) {
        this.partidaId = partidaId;
        this.barcoId = barcoId;
        this.jugadorId = jugadorId;
        this.celdaInicialId = celdaInicialId;
        this.ordenTurno = ordenTurno;
        this.estado = Participacion.Estado.ACTIVO;
        this.fechaInicio = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getIdParticipacion() {
        return idParticipacion;
    }

    public void setIdParticipacion(Long idParticipacion) {
        this.idParticipacion = idParticipacion;
    }
    
    public Integer getOrdenTurno() {
        return ordenTurno;
    }
    
    public void setOrdenTurno(Integer ordenTurno) {
        this.ordenTurno = ordenTurno;
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
    
    public Long getCeldaInicialId() {
        return celdaInicialId;
    }
    
    public void setCeldaInicialId(Long celdaInicialId) {
        this.celdaInicialId = celdaInicialId;
    }
    
    public String getCeldaInicialInfo() {
        return celdaInicialInfo;
    }
    
    public void setCeldaInicialInfo(String celdaInicialInfo) {
        this.celdaInicialInfo = celdaInicialInfo;
    }
    
    public Participacion.Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Participacion.Estado estado) {
        this.estado = estado;
    }
    
    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }
    
    public String getPartidaNombre() {
        return partidaNombre;
    }
    
    public void setPartidaNombre(String partidaNombre) {
        this.partidaNombre = partidaNombre;
    }
    
    public Long getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }
    
    public String getBarcoAlias() {
        return barcoAlias;
    }
    
    public void setBarcoAlias(String barcoAlias) {
        this.barcoAlias = barcoAlias;
    }
    
    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }
    
    public String getJugadorNombre() {
        return jugadorNombre;
    }
    
    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
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
    
    public Boolean getEsGanador() {
        return esGanador;
    }
    
    public void setEsGanador(Boolean esGanador) {
        this.esGanador = esGanador;
    }
    
    // Métodos de utilidad
    public boolean esActiva() {
        return Participacion.Estado.ACTIVO.equals(this.estado);
    }
    
    public boolean estaDestruida() {
        return Participacion.Estado.DESTRUIDO.equals(this.estado);
    }
    
    public boolean llegoAMeta() {
        return Participacion.Estado.EN_META.equals(this.estado);
    }
    
    public String getEstadoDisplayName() {
        return estado != null ? estado.name() : "";
    }
    
    @Override
    public String toString() {
        return "ParticipacionDTO{" +
                "idParticipacion=" + idParticipacion +
                ", ordenTurno=" + ordenTurno +
                ", estado=" + estado +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", jugadorNombre='" + jugadorNombre + '\'' +
                ", barcoAlias='" + barcoAlias + '\'' +
                '}';
    }
}

