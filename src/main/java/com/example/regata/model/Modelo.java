package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Entity
@Table(name = "modelos")
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @NotNull(message = "La velocidad máxima es obligatoria")
    @Positive(message = "La velocidad máxima debe ser positiva")
    @Column(name = "velocidad_maxima", nullable = false)
    private Integer velocidadMaxima;
    
    @NotNull(message = "La resistencia es obligatoria")
    @Positive(message = "La resistencia debe ser positiva")
    @Column(nullable = false)
    private Integer resistencia;
    
    @NotNull(message = "La maniobrabilidad es obligatoria")
    @Positive(message = "La maniobrabilidad debe ser positiva")
    @Column(nullable = false)
    private Integer maniobrabilidad;
    
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    // Constructores
    public Modelo() {}
    
    public Modelo(String nombre, String descripcion, Integer velocidadMaxima, 
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
    
    public List<Barco> getBarcos() {
        return barcos;
    }
    
    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }
    
    @Override
    public String toString() {
        return "Modelo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", velocidadMaxima=" + velocidadMaxima +
                ", resistencia=" + resistencia +
                ", maniobrabilidad=" + maniobrabilidad +
                '}';
    }
}
