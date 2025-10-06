package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partidas")
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_partida")
    private UUID idPartida;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mapa", nullable = false)
    @NotNull(message = "El mapa es obligatorio")
    private Mapa mapa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ganador_usuario")
    private Usuario ganadorUsuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ganador_barco")
    private Barco ganadorBarco;
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participacion> participaciones;
    
    // Enum para los estados
    public enum Estado {
        ESPERANDO, EN_JUEGO, TERMINADA
    }
    
    // Constructores
    public Partida() {}
    
    public Partida(Mapa mapa) {
        this.mapa = mapa;
        this.estado = Estado.ESPERANDO;
        this.fechaInicio = LocalDateTime.now();
    }
    
    // Getters y Setters
    public UUID getIdPartida() {
        return idPartida;
    }
    
    public void setIdPartida(UUID idPartida) {
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
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public Mapa getMapa() {
        return mapa;
    }
    
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    
    public Usuario getGanadorUsuario() {
        return ganadorUsuario;
    }
    
    public void setGanadorUsuario(Usuario ganadorUsuario) {
        this.ganadorUsuario = ganadorUsuario;
    }
    
    public Barco getGanadorBarco() {
        return ganadorBarco;
    }
    
    public void setGanadorBarco(Barco ganadorBarco) {
        this.ganadorBarco = ganadorBarco;
    }
    
    public List<Participacion> getParticipaciones() {
        return participaciones;
    }
    
    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }
    
    // Métodos de negocio
    public void iniciarPartida() {
        this.estado = Estado.EN_JUEGO;
    }
    
    public void terminarPartida(Usuario ganadorUsuario, Barco ganadorBarco) {
        this.estado = Estado.TERMINADA;
        this.fechaFin = LocalDateTime.now();
        this.ganadorUsuario = ganadorUsuario;
        this.ganadorBarco = ganadorBarco;
    }
    
    @Override
    public String toString() {
        return "Partida{" +
                "idPartida=" + idPartida +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                ", mapa=" + (mapa != null ? mapa.getNombre() : "null") +
                '}';
    }
}

