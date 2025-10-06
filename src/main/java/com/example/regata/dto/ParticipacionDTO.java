package com.example.regata.dto;

import com.example.regata.model.Participacion;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class ParticipacionDTO {
    
    private UUID idParticipacion;
    
    private Integer turnoActual;
    
    @NotNull(message = "La posición X es obligatoria")
    private Integer posX;
    
    @NotNull(message = "La posición Y es obligatoria")
    private Integer posY;
    
    private Integer velX;
    
    private Integer velY;
    
    @NotNull(message = "El estado es obligatorio")
    private Participacion.Estado estado;
    
    @NotNull(message = "La partida es obligatoria")
    private UUID partidaId;
    
    private String partidaNombre;
    
    @NotNull(message = "El barco es obligatorio")
    private UUID barcoId;
    
    private String barcoAlias;
    
    @NotNull(message = "El jugador es obligatorio")
    private UUID jugadorId;
    
    private String jugadorNombre;
    
    // Campos adicionales para la vista
    private List<MovimientoDTO> movimientos;
    private Long totalMovimientos;
    private Long duracionTurnos;
    private Boolean esGanador;
    
    // Constructores
    public ParticipacionDTO() {}
    
    public ParticipacionDTO(UUID partidaId, UUID barcoId, UUID jugadorId, Integer posX, Integer posY) {
        this.partidaId = partidaId;
        this.barcoId = barcoId;
        this.jugadorId = jugadorId;
        this.posX = posX;
        this.posY = posY;
        this.velX = 0;
        this.velY = 0;
        this.turnoActual = 0;
        this.estado = Participacion.Estado.ACTIVO;
    }
    
    // Getters y Setters
    public UUID getIdParticipacion() {
        return idParticipacion;
    }
    
    public void setIdParticipacion(UUID idParticipacion) {
        this.idParticipacion = idParticipacion;
    }
    
    public Integer getTurnoActual() {
        return turnoActual;
    }
    
    public void setTurnoActual(Integer turnoActual) {
        this.turnoActual = turnoActual;
    }
    
    public Integer getPosX() {
        return posX;
    }
    
    public void setPosX(Integer posX) {
        this.posX = posX;
    }
    
    public Integer getPosY() {
        return posY;
    }
    
    public void setPosY(Integer posY) {
        this.posY = posY;
    }
    
    public Integer getVelX() {
        return velX;
    }
    
    public void setVelX(Integer velX) {
        this.velX = velX;
    }
    
    public Integer getVelY() {
        return velY;
    }
    
    public void setVelY(Integer velY) {
        this.velY = velY;
    }
    
    public Participacion.Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Participacion.Estado estado) {
        this.estado = estado;
    }
    
    public UUID getPartidaId() {
        return partidaId;
    }
    
    public void setPartidaId(UUID partidaId) {
        this.partidaId = partidaId;
    }
    
    public String getPartidaNombre() {
        return partidaNombre;
    }
    
    public void setPartidaNombre(String partidaNombre) {
        this.partidaNombre = partidaNombre;
    }
    
    public UUID getBarcoId() {
        return barcoId;
    }
    
    public void setBarcoId(UUID barcoId) {
        this.barcoId = barcoId;
    }
    
    public String getBarcoAlias() {
        return barcoAlias;
    }
    
    public void setBarcoAlias(String barcoAlias) {
        this.barcoAlias = barcoAlias;
    }
    
    public UUID getJugadorId() {
        return jugadorId;
    }
    
    public void setJugadorId(UUID jugadorId) {
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
    
    public Long getDuracionTurnos() {
        return duracionTurnos;
    }
    
    public void setDuracionTurnos(Long duracionTurnos) {
        this.duracionTurnos = duracionTurnos;
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
    
    public String getPosicion() {
        return posX != null && posY != null ? "(" + posX + "," + posY + ")" : "(0,0)";
    }
    
    public String getVelocidad() {
        return velX != null && velY != null ? "(" + velX + "," + velY + ")" : "(0,0)";
    }
    
    @Override
    public String toString() {
        return "ParticipacionDTO{" +
                "idParticipacion=" + idParticipacion +
                ", turnoActual=" + turnoActual +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", estado=" + estado +
                ", jugadorNombre='" + jugadorNombre + '\'' +
                ", barcoAlias='" + barcoAlias + '\'' +
                '}';
    }
}

