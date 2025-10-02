package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "jugadores")
public class Jugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Email(message = "El email debe ser válido")
    @Column(unique = true)
    private String email;
    
    @Column(name = "puntos_totales")
    private Integer puntosTotales = 0;
    
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    // Constructores
    public Jugador() {}
    
    public Jugador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.puntosTotales = 0;
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
    
    public List<Barco> getBarcos() {
        return barcos;
    }
    
    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }
    
    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", puntosTotales=" + puntosTotales +
                '}';
    }
}
