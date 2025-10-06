package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "movimientos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_participacion", "turno"}))
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mov")
    private UUID idMov;
    
    @Column(nullable = false)
    private Integer turno;
    
    @Column(name = "pos_x", nullable = false)
    private Integer posX;
    
    @Column(name = "pos_y", nullable = false)
    private Integer posY;
    
    @Column(name = "vel_x", nullable = false)
    private Integer velX;
    
    @Column(name = "vel_y", nullable = false)
    private Integer velY;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delta_vx", nullable = false)
    private DeltaVelocidad deltaVx;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delta_vy", nullable = false)
    private DeltaVelocidad deltaVy;
    
    @Column(nullable = false)
    private Boolean colision = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participacion", nullable = false)
    @NotNull(message = "La participación es obligatoria")
    private Participacion participacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_celda")
    private Celda celda;
    
    // Enum para los cambios de velocidad
    public enum DeltaVelocidad {
        DECREMENTO(-1), MANTENER(0), INCREMENTO(1);
        
        private final int valor;
        
        DeltaVelocidad(int valor) {
            this.valor = valor;
        }
        
        public int getValor() {
            return valor;
        }
    }
    
    // Constructores
    public Movimiento() {}
    
    public Movimiento(Integer turno, Integer posX, Integer posY, Integer velX, Integer velY, 
                     DeltaVelocidad deltaVx, DeltaVelocidad deltaVy, Participacion participacion) {
        this.turno = turno;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.deltaVx = deltaVx;
        this.deltaVy = deltaVy;
        this.participacion = participacion;
        this.colision = false;
    }
    
    // Getters y Setters
    public UUID getIdMov() {
        return idMov;
    }
    
    public void setIdMov(UUID idMov) {
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
    
    public DeltaVelocidad getDeltaVx() {
        return deltaVx;
    }
    
    public void setDeltaVx(DeltaVelocidad deltaVx) {
        this.deltaVx = deltaVx;
    }
    
    public DeltaVelocidad getDeltaVy() {
        return deltaVy;
    }
    
    public void setDeltaVy(DeltaVelocidad deltaVy) {
        this.deltaVy = deltaVy;
    }
    
    public Boolean getColision() {
        return colision;
    }
    
    public void setColision(Boolean colision) {
        this.colision = colision;
    }
    
    public Participacion getParticipacion() {
        return participacion;
    }
    
    public void setParticipacion(Participacion participacion) {
        this.participacion = participacion;
    }
    
    public Celda getCelda() {
        return celda;
    }
    
    public void setCelda(Celda celda) {
        this.celda = celda;
    }
    
    @Override
    public String toString() {
        return "Movimiento{" +
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

