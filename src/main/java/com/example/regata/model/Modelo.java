package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modelos")
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_modelo")
    private UUID idModelo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El color es obligatorio")
    @Column(name = "color_hex", nullable = false)
    private String colorHex;
    
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    // Constructores
    public Modelo() {}
    
    public Modelo(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex = colorHex;
    }
    
    // Getters y Setters
    public UUID getIdModelo() {
        return idModelo;
    }
    
    public void setIdModelo(UUID idModelo) {
        this.idModelo = idModelo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getColorHex() {
        return colorHex;
    }
    
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
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
                "idModelo=" + idModelo +
                ", nombre='" + nombre + '\'' +
                ", colorHex='" + colorHex + '\'' +
                '}';
    }
}
