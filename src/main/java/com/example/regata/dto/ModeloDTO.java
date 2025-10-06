package com.example.regata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ModeloDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "La velocidad máxima es obligatoria")
    @Positive(message = "La velocidad máxima debe ser positiva")
    private Integer velocidadMaxima;
    
    @NotNull(message = "La resistencia es obligatoria")
    @Positive(message = "La resistencia debe ser positiva")
    private Integer resistencia;
    
    @NotNull(message = "La maniobrabilidad es obligatoria")
    @Positive(message = "La maniobrabilidad debe ser positiva")
    private Integer maniobrabilidad;
    
    private Long cantidadBarcos;
    
    // Constructores
    public ModeloDTO() {}
    
    public ModeloDTO(String nombre, String descripcion, Integer velocidadMaxima, 
                     Integer resistencia, Integer maniobrabilidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.velocidadMaxima = velocidadMaxima;
        this.resistencia = resistencia;
        this.maniobrabilidad = maniobrabilidad;
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getVelocidadMaxima() {
        return velocidadMaxima;
    }
    
    public void setVelocidadMaxima(Integer velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }
    
    public Integer getResistencia() {
        return resistencia;
    }
    
    public void setResistencia(Integer resistencia) {
        this.resistencia = resistencia;
    }
    
    public Integer getManiobrabilidad() {
        return maniobrabilidad;
    }
    
    public void setManiobrabilidad(Integer maniobrabilidad) {
        this.maniobrabilidad = maniobrabilidad;
    }
    
    public Long getCantidadBarcos() {
        return cantidadBarcos;
    }
    
    public void setCantidadBarcos(Long cantidadBarcos) {
        this.cantidadBarcos = cantidadBarcos;
    }
    
    @Override
    public String toString() {
        return "ModeloDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", velocidadMaxima=" + velocidadMaxima +
                ", resistencia=" + resistencia +
                ", maniobrabilidad=" + maniobrabilidad +
                ", cantidadBarcos=" + cantidadBarcos +
                '}';
    }
}
