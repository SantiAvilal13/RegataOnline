package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "celdas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_mapa", "coord_x", "coord_y"}))
public class Celda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_celda")
    private UUID idCelda;
    
    @Column(name = "coord_x", nullable = false)
    private Integer coordX;
    
    @Column(name = "coord_y", nullable = false)
    private Integer coordY;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mapa", nullable = false)
    @NotNull(message = "El mapa es obligatorio")
    private Mapa mapa;
    
    @OneToMany(mappedBy = "celda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos;
    
    // Enum para los tipos de celda
    public enum Tipo {
        AGUA, PARED, PARTIDA, META
    }
    
    // Constructores
    public Celda() {}
    
    public Celda(Integer coordX, Integer coordY, Tipo tipo, Mapa mapa) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.tipo = tipo;
        this.mapa = mapa;
    }
    
    // Getters y Setters
    public UUID getIdCelda() {
        return idCelda;
    }
    
    public void setIdCelda(UUID idCelda) {
        this.idCelda = idCelda;
    }
    
    public Integer getCoordX() {
        return coordX;
    }
    
    public void setCoordX(Integer coordX) {
        this.coordX = coordX;
    }
    
    public Integer getCoordY() {
        return coordY;
    }
    
    public void setCoordY(Integer coordY) {
        this.coordY = coordY;
    }
    
    public Tipo getTipo() {
        return tipo;
    }
    
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Mapa getMapa() {
        return mapa;
    }
    
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
    
    @Override
    public String toString() {
        return "Celda{" +
                "idCelda=" + idCelda +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                ", tipo=" + tipo +
                ", mapa=" + (mapa != null ? mapa.getNombre() : "null") +
                '}';
    }
}

