package com.regataonline.model;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "barcos")
public class Barco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 7)
    private String color; // Código hexadecimal del color
    
    @Column(name = "nivel_combustible", nullable = false)
    private Integer nivelCombustible;
    
    @Column(name = "estado_salud", nullable = false)
    private Integer estadoSalud = 100; // Porcentaje de salud del barco
    
    @Column(name = "experiencia", nullable = false)
    private Integer experiencia = 0;
    
    @Column(name = "carreras_ganadas", nullable = false)
    private Integer carrerasGanadas = 0;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;
    
    // Constructores
    public Barco() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Barco(String nombre, String color, Jugador jugador, Modelo modelo) {
        this.nombre = nombre;
        this.color = color;
        this.jugador = jugador;
        this.modelo = modelo;
        this.nivelCombustible = modelo != null ? modelo.getCapacidadCombustible() : 100;
        this.estadoSalud = 100;
        this.experiencia = 0;
        this.carrerasGanadas = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
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
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getNivelCombustible() {
        return nivelCombustible;
    }
    
    public void setNivelCombustible(Integer nivelCombustible) {
        this.nivelCombustible = nivelCombustible;
    }
    
    public Integer getEstadoSalud() {
        return estadoSalud;
    }
    
    public void setEstadoSalud(Integer estadoSalud) {
        this.estadoSalud = estadoSalud;
    }
    
    public Integer getExperiencia() {
        return experiencia;
    }
    
    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }
    
    public Integer getCarrerasGanadas() {
        return carrerasGanadas;
    }
    
    public void setCarrerasGanadas(Integer carrerasGanadas) {
        this.carrerasGanadas = carrerasGanadas;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
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
    
    @Override
    public String toString() {
        return "Barco{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", color='" + color + '\'' +
                ", estadoSalud=" + estadoSalud +
                ", experiencia=" + experiencia +
                ", carrerasGanadas=" + carrerasGanadas +
                '}';
    }
}
