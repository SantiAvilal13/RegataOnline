package com.example.regata.dto;

import com.example.regata.model.Movimiento;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MovimientoDTO {
    
    private Long idMovimiento;
    
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
    private Integer deltaVx;
    
    @NotNull(message = "El cambio de velocidad Y es obligatorio")
    private Integer deltaVy;
    
    @NotNull(message = "El resultado es obligatorio")
    private Movimiento.Resultado resultado;
    
    private LocalDateTime fechaMovimiento;
    
    @NotNull(message = "La participación es obligatoria")
    private Long participacionId;
    
    private String participacionInfo;
    
    private Long celdaDestinoId;
    
    private String celdaDestinoInfo;
    
    // Propiedades calculadas para el frontend
    private Boolean colision;
    private Boolean llegoAMeta;
    private Boolean salioDelMapa;
    
    // Constructores
    public MovimientoDTO() {}
    
    public MovimientoDTO(Integer turno, Integer posX, Integer posY, Integer velX, Integer velY, 
                        Integer deltaVx, Integer deltaVy, Movimiento.Resultado resultado, Long participacionId) {
        this.turno = turno;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.deltaVx = deltaVx;
        this.deltaVy = deltaVy;
        this.resultado = resultado;
        this.participacionId = participacionId;
        this.fechaMovimiento = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
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
    
    public Integer getDeltaVx() {
        return deltaVx;
    }
    
    public void setDeltaVx(Integer deltaVx) {
        this.deltaVx = deltaVx;
    }
    
    public Integer getDeltaVy() {
        return deltaVy;
    }
    
    public void setDeltaVy(Integer deltaVy) {
        this.deltaVy = deltaVy;
    }
    
    public Movimiento.Resultado getResultado() {
        return resultado;
    }
    
    public void setResultado(Movimiento.Resultado resultado) {
        this.resultado = resultado;
    }
    
    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }
    
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
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
    
    public Long getCeldaDestinoId() {
        return celdaDestinoId;
    }
    
    public void setCeldaDestinoId(Long celdaDestinoId) {
        this.celdaDestinoId = celdaDestinoId;
    }
    
    public String getCeldaDestinoInfo() {
        return celdaDestinoInfo;
    }
    
    public void setCeldaDestinoInfo(String celdaDestinoInfo) {
        this.celdaDestinoInfo = celdaDestinoInfo;
    }
    
    public Boolean getColision() {
        return colision;
    }
    
    public void setColision(Boolean colision) {
        this.colision = colision;
    }
    
    public Boolean getLlegoAMeta() {
        return llegoAMeta;
    }
    
    public void setLlegoAMeta(Boolean llegoAMeta) {
        this.llegoAMeta = llegoAMeta;
    }
    
    public Boolean getSalioDelMapa() {
        return salioDelMapa;
    }
    
    public void setSalioDelMapa(Boolean salioDelMapa) {
        this.salioDelMapa = salioDelMapa;
    }
    
    // Métodos de utilidad
    public boolean fueExitoso() {
        return Movimiento.Resultado.OK.equals(this.resultado);
    }
    
    public boolean huboColision() {
        return Movimiento.Resultado.COLISION_PARED.equals(this.resultado);
    }
    
    public boolean llegoAMeta() {
        return Movimiento.Resultado.COLISION_META.equals(this.resultado);
    }
    
    public boolean salioDelMapa() {
        return Movimiento.Resultado.FUERA_MAPA.equals(this.resultado);
    }
    
    public String getPosicion() {
        return posX != null && posY != null ? "(" + posX + "," + posY + ")" : "(0,0)";
    }
    
    public String getVelocidad() {
        return velX != null && velY != null ? "(" + velX + "," + velY + ")" : "(0,0)";
    }
    
    public String getDeltaVelocidad() {
        return deltaVx != null && deltaVy != null ? 
               "(" + deltaVx + "," + deltaVy + ")" : "(0,0)";
    }
    
    @Override
    public String toString() {
        return "MovimientoDTO{" +
                "idMovimiento=" + idMovimiento +
                ", turno=" + turno +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", deltaVx=" + deltaVx +
                ", deltaVy=" + deltaVy +
                ", resultado=" + resultado +
                '}';
    }
}

