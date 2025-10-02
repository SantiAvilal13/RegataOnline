package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "barcos")
public class Barco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "puntos_ganados")
    private Integer puntosGanados = 0;
    
    @Column(name = "velocidad_actual")
    private Integer velocidadActual = 0;
    
    @Column(name = "resistencia_actual")
    private Integer resistenciaActual;
    
    @Column(name = "maniobrabilidad_actual")
    private Integer maniobrabilidadActual;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    @NotNull(message = "El jugador es obligatorio")
    private Jugador jugador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    @NotNull(message = "El modelo es obligatorio")
    private Modelo modelo;
    
    // Constructores
    public Barco() {}
    
    public Barco(String nombre, Jugador jugador, Modelo modelo) {
        this.nombre = nombre;
        this.jugador = jugador;
        this.modelo = modelo;
        this.puntosGanados = 0;
        this.velocidadActual = 0;
        this.resistenciaActual = modelo.getResistencia();
        this.maniobrabilidadActual = modelo.getManiobrabilidad();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getPuntosGanados() {
        return puntosGanados;
    }
    
    public void setPuntosGanados(Integer puntosGanados) {
        this.puntosGanados = puntosGanados;
    }
    
    public Integer getVelocidadActual() {
        return velocidadActual;
    }
    
    public void setVelocidadActual(Integer velocidadActual) {
        this.velocidadActual = velocidadActual;
    }
    
    public Integer getResistenciaActual() {
        return resistenciaActual;
    }
    
    public void setResistenciaActual(Integer resistenciaActual) {
        this.resistenciaActual = resistenciaActual;
    }
    
    public Integer getManiobrabilidadActual() {
        return maniobrabilidadActual;
    }
    
    public void setManiobrabilidadActual(Integer maniobrabilidadActual) {
        this.maniobrabilidadActual = maniobrabilidadActual;
    }
    
    public Jugador getJugador() {
        return jugador;
    }
    
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    public Modelo getModelo() {
        return modelo;
    }
    
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    // Métodos de negocio
    public void ganarPuntos(Integer puntos) {
        this.puntosGanados += puntos;
        if (this.jugador != null) {
            this.jugador.setPuntosTotales(this.jugador.getPuntosTotales() + puntos);
        }
    }
    
    public void resetearEstadisticas() {
        this.velocidadActual = 0;
        this.resistenciaActual = this.modelo.getResistencia();
        this.maniobrabilidadActual = this.modelo.getManiobrabilidad();
    }
    
    @Override
    public String toString() {
        return "Barco{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", puntosGanados=" + puntosGanados +
                ", velocidadActual=" + velocidadActual +
                ", resistenciaActual=" + resistenciaActual +
                ", maniobrabilidadActual=" + maniobrabilidadActual +
                '}';
    }
}
