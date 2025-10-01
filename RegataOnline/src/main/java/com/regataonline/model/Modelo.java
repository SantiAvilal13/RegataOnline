package com.regataonline.model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "modelos")
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "velocidad_maxima", nullable = false)
    private Integer velocidadMaxima;
    
    @Column(name = "resistencia", nullable = false)
    private Integer resistencia;
    
    @Column(name = "maniobrabilidad", nullable = false)
    private Integer maniobrabilidad;
    
    @Column(name = "capacidad_combustible", nullable = false)
    private Integer capacidadCombustible;
    
    @Column(length = 50)
    private String tipo; // Ejemplo: "Velero", "Lancha", "Yate", etc.
    
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    // Constructores
    public Modelo() {}
    
    public Modelo(String nombre, String descripcion, Integer velocidadMaxima, 
                  Integer resistencia, Integer maniobrabilidad, Integer capacidadCombustible, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.velocidadMaxima = velocidadMaxima;
        this.resistencia = resistencia;
        this.maniobrabilidad = maniobrabilidad;
        this.capacidadCombustible = capacidadCombustible;
        this.tipo = tipo;
    }
    
    // Constructor alternativo para DbInitializer (nombre, tipo, descripcion, stats...)
    public Modelo(String nombre, String tipo, String descripcion, 
                  Integer velocidadMaxima, Integer resistencia, Integer maniobrabilidad, Integer capacidadCombustible) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.velocidadMaxima = velocidadMaxima;
        this.resistencia = resistencia;
        this.maniobrabilidad = maniobrabilidad;
        this.capacidadCombustible = capacidadCombustible;
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
    
    public Integer getCapacidadCombustible() {
        return capacidadCombustible;
    }
    
    public void setCapacidadCombustible(Integer capacidadCombustible) {
        this.capacidadCombustible = capacidadCombustible;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
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
                ", tipo='" + tipo + '\'' +
                ", velocidadMaxima=" + velocidadMaxima +
                ", resistencia=" + resistencia +
                ", maniobrabilidad=" + maniobrabilidad +
                '}';
    }
}
