package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "participaciones",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_partida", "id_barco"}),
           @UniqueConstraint(columnNames = {"id_partida", "id_jugador"})
       })
public class Participacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_participacion")
    private UUID idParticipacion;
    
    @Column(name = "turno_actual", nullable = false)
    private Integer turnoActual = 0;
    
    @Column(name = "pos_x", nullable = false)
    private Integer posX;
    
    @Column(name = "pos_y", nullable = false)
    private Integer posY;
    
    @Column(name = "vel_x", nullable = false)
    private Integer velX = 0;
    
    @Column(name = "vel_y", nullable = false)
    private Integer velY = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida", nullable = false)
    @NotNull(message = "La partida es obligatoria")
    private Partida partida;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barco", nullable = false)
    @NotNull(message = "El barco es obligatorio")
    private Barco barco;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    @NotNull(message = "El jugador es obligatorio")
    private Usuario jugador;
    
    @OneToMany(mappedBy = "participacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos;
    
    // Enum para los estados
    public enum Estado {
        ACTIVO, DESTRUIDO, EN_META
    }
    
    // Constructores
    public Participacion() {}
    
    public Participacion(Partida partida, Barco barco, Usuario jugador, Integer posX, Integer posY) {
        this.partida = partida;
        this.barco = barco;
        this.jugador = jugador;
        this.posX = posX;
        this.posY = posY;
        this.velX = 0;
        this.velY = 0;
        this.turnoActual = 0;
        this.estado = Estado.ACTIVO;
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
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public Partida getPartida() {
        return partida;
    }
    
    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    
    public Barco getBarco() {
        return barco;
    }
    
    public void setBarco(Barco barco) {
        this.barco = barco;
    }
    
    public Usuario getJugador() {
        return jugador;
    }
    
    public void setJugador(Usuario jugador) {
        this.jugador = jugador;
    }
    
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
    
    // Métodos de negocio
    public void avanzarTurno() {
        this.turnoActual++;
    }
    
    public void mover(Integer nuevaPosX, Integer nuevaPosY) {
        this.posX = nuevaPosX;
        this.posY = nuevaPosY;
    }
    
    public void actualizarVelocidad(Integer nuevaVelX, Integer nuevaVelY) {
        this.velX = nuevaVelX;
        this.velY = nuevaVelY;
    }
    
    public void destruir() {
        this.estado = Estado.DESTRUIDO;
    }
    
    public void llegarAMeta() {
        this.estado = Estado.EN_META;
    }
    
    @Override
    public String toString() {
        return "Participacion{" +
                "idParticipacion=" + idParticipacion +
                ", turnoActual=" + turnoActual +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", estado=" + estado +
                ", jugador=" + (jugador != null ? jugador.getNombre() : "null") +
                ", barco=" + (barco != null ? barco.getAlias() : "null") +
                '}';
    }
}

