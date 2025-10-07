package com.example.regata.dto;

import com.example.regata.model.Movimiento;
import jakarta.validation.constraints.NotNull;

public class MovimientoDTO {
    
    private Long idMov;
    
    @NotNull(message = "El turno es obligatorio")
    private Integer turno;
    
    @NotNull(message = "La posición X es obligatoria")
    private Integer posX;
    
    @NotNull(message = "La posición Y es obligatoria")
    private Integer posY;
    
    @NotNull(message = "La velocidad X es obligatoria")
    private Integer velX;
    
    @NotNull(message = "La velocidad Y es obligatoria")
    private Integer velY;
    
    @NotNull(message = "El cambio de velocidad X es obligatorio")
    private Movimiento.DeltaVelocidad deltaVx;
    
    @NotNull(message = "El cambio de velocidad Y es obligatorio")
    private Movimiento.DeltaVelocidad deltaVy;
    
    private Boolean colision;
    
    @NotNull(message = "La participación es obligatoria")
    private Long participacionId;
    
    private String participacionInfo;
    
    private Long celdaId;
    
    private String celdaInfo;
    
    // Constructores
    public MovimientoDTO() {}
    
    public MovimientoDTO(Integer turno, Integer posX, Integer posY, Integer velX, Integer velY, 
                        Movimiento.DeltaVelocidad deltaVx, Movimiento.DeltaVelocidad deltaVy, 
                        Long participacionId) {
        this.turno = turno;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.deltaVx = deltaVx;
        this.deltaVy = deltaVy;
        this.participacionId = participacionId;
        this.colision = false;
    }
    
    // Getters y Setters
    public Long getIdMov() {
        return idMov;
    }

    public void setIdMov(Long idMov) {
        this.idMov = idMov;
    }
    
    public Integer getTurno() {
        return turno;
    }
    
    public void setTurno(Integer turno) {
        this.turno = turno;
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
    
    public Movimiento.DeltaVelocidad getDeltaVx() {
        return deltaVx;
    }
    
    public void setDeltaVx(Movimiento.DeltaVelocidad deltaVx) {
        this.deltaVx = deltaVx;
    }
    
    public Movimiento.DeltaVelocidad getDeltaVy() {
        return deltaVy;
    }
    
    public void setDeltaVy(Movimiento.DeltaVelocidad deltaVy) {
        this.deltaVy = deltaVy;
    }
    
    public Boolean getColision() {
        return colision;
    }
    
    public void setColision(Boolean colision) {
        this.colision = colision;
    }
    
    public Long getParticipacionId() {
        return participacionId;
    }

    public void setParticipacionId(Long participacionId) {
        this.participacionId = participacionId;
    }
    
    public String getParticipacionInfo() {
        return participacionInfo;
    }
    
    public void setParticipacionInfo(String participacionInfo) {
        this.participacionInfo = participacionInfo;
    }
    
    public Long getCeldaId() {
        return celdaId;
    }

    public void setCeldaId(Long celdaId) {
        this.celdaId = celdaId;
    }
    
    public String getCeldaInfo() {
        return celdaInfo;
    }
    
    public void setCeldaInfo(String celdaInfo) {
        this.celdaInfo = celdaInfo;
    }
    
    // Métodos de utilidad
    public boolean tieneColision() {
        return Boolean.TRUE.equals(this.colision);
    }
    
    public String getPosicion() {
        return posX != null && posY != null ? "(" + posX + "," + posY + ")" : "(0,0)";
    }
    
    public String getVelocidad() {
        return velX != null && velY != null ? "(" + velX + "," + velY + ")" : "(0,0)";
    }
    
    public String getDeltaVelocidad() {
        return deltaVx != null && deltaVy != null ? 
               "(" + deltaVx.getValor() + "," + deltaVy.getValor() + ")" : "(0,0)";
    }
    
    public String getDeltaVxDisplayName() {
        return deltaVx != null ? deltaVx.name() : "";
    }
    
    public String getDeltaVyDisplayName() {
        return deltaVy != null ? deltaVy.name() : "";
    }
    
    @Override
    public String toString() {
        return "MovimientoDTO{" +
                "idMov=" + idMov +
                ", turno=" + turno +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", deltaVx=" + deltaVx +
                ", deltaVy=" + deltaVy +
                ", colision=" + colision +
                '}';
    }
}

