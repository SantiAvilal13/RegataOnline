package com.example.regata.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class JugadorDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotNull(message = "Los puntos totales son obligatorios")
    @PositiveOrZero(message = "Los puntos totales deben ser positivos o cero")
    private Integer puntosTotales;
    
    private Long cantidadBarcos;
    
    // Constructores
    public JugadorDTO() {}
    
    public JugadorDTO(String nombre, String email, Integer puntosTotales) {
        this.nombre = nombre;
        this.email = email;
        this.puntosTotales = puntosTotales;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getPuntosTotales() {
        return puntosTotales;
    }
    
    public void setPuntosTotales(Integer puntosTotales) {
        this.puntosTotales = puntosTotales;
    }
    
    public Long getCantidadBarcos() {
        return cantidadBarcos;
    }
    
    public void setCantidadBarcos(Long cantidadBarcos) {
        this.cantidadBarcos = cantidadBarcos;
    }
    
    @Override
    public String toString() {
        return "JugadorDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", puntosTotales=" + puntosTotales +
                ", cantidadBarcos=" + cantidadBarcos +
                '}';
    }
}
