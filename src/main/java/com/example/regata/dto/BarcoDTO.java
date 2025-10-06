package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class BarcoDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @PositiveOrZero(message = "Los puntos ganados deben ser positivos o cero")
    private Integer puntosGanados;
    
    @PositiveOrZero(message = "La velocidad actual debe ser positiva o cero")
    private Integer velocidadActual;
    
    @PositiveOrZero(message = "La resistencia actual debe ser positiva o cero")
    private Integer resistenciaActual;
    
    @PositiveOrZero(message = "La maniobrabilidad actual debe ser positiva o cero")
    private Integer maniobrabilidadActual;
    
    @NotNull(message = "El jugador es obligatorio")
    private Long jugadorId;
    
    private String jugadorNombre;
    
    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;
    
    private String modeloNombre;
    
    // Constructores
    public BarcoDTO() {}
    
    public BarcoDTO(String nombre, Long jugadorId, Long modeloId) {
        this.nombre = nombre;
        this.jugadorId = jugadorId;
        this.modeloId = modeloId;
        this.puntosGanados = 0;
        this.velocidadActual = 0;
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
    
    public Long getModeloId() {
        return modeloId;
    }
    
    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }
    
    public String getModeloNombre() {
        return modeloNombre;
    }
    
    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
    }
    
    @Override
    public String toString() {
        return "BarcoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", puntosGanados=" + puntosGanados +
                ", velocidadActual=" + velocidadActual +
                ", resistenciaActual=" + resistenciaActual +
                ", maniobrabilidadActual=" + maniobrabilidadActual +
                ", jugadorId=" + jugadorId +
                ", jugadorNombre='" + jugadorNombre + '\'' +
                ", modeloId=" + modeloId +
                ", modeloNombre='" + modeloNombre + '\'' +
                '}';
    }
}
