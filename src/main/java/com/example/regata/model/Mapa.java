package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mapas")
public class Mapa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mapa")
    private UUID idMapa;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Positive(message = "El tamaño de filas debe ser positivo")
    @Column(name = "tam_filas", nullable = false)
    private Integer tamFilas;
    
    @Positive(message = "El tamaño de columnas debe ser positivo")
    @Column(name = "tam_columnas", nullable = false)
    private Integer tamColumnas;
    
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Celda> celdas;
    
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidas;
    
    // Constructores
    public Mapa() {}
    
    public Mapa(String nombre, Integer tamFilas, Integer tamColumnas) {
        this.nombre = nombre;
        this.tamFilas = tamFilas;
        this.tamColumnas = tamColumnas;
    }
    
    // Getters y Setters
    public UUID getIdMapa() {
        return idMapa;
    }
    
    public void setIdMapa(UUID idMapa) {
        this.idMapa = idMapa;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getTamFilas() {
        return tamFilas;
    }
    
    public void setTamFilas(Integer tamFilas) {
        this.tamFilas = tamFilas;
    }
    
    public Integer getTamColumnas() {
        return tamColumnas;
    }
    
    public void setTamColumnas(Integer tamColumnas) {
        this.tamColumnas = tamColumnas;
    }
    
    public List<Celda> getCeldas() {
        return celdas;
    }
    
    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }
    
    public List<Partida> getPartidas() {
        return partidas;
    }
    
    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
    
    @Override
    public String toString() {
        return "Mapa{" +
                "idMapa=" + idMapa +
                ", nombre='" + nombre + '\'' +
                ", tamFilas=" + tamFilas +
                ", tamColumnas=" + tamColumnas +
                '}';
    }
}

