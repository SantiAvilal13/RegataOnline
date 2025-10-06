package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "barcos")
public class Barco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_barco")
    private UUID idBarco;
    
    @NotBlank(message = "El alias es obligatorio")
    @Column(nullable = false)
    private String alias;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "El usuario propietario es obligatorio")
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo", nullable = false)
    @NotNull(message = "El modelo es obligatorio")
    private Modelo modelo;
    
    @OneToMany(mappedBy = "barco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participacion> participaciones;
    
    // Constructores
    public Barco() {}
    
    public Barco(String alias, Usuario usuario, Modelo modelo) {
        this.alias = alias;
        this.usuario = usuario;
        this.modelo = modelo;
    }
    
    // Getters y Setters
    public UUID getIdBarco() {
        return idBarco;
    }
    
    public void setIdBarco(UUID idBarco) {
        this.idBarco = idBarco;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Modelo getModelo() {
        return modelo;
    }
    
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public List<Participacion> getParticipaciones() {
        return participaciones;
    }
    
    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }
    
    @Override
    public String toString() {
        return "Barco{" +
                "idBarco=" + idBarco +
                ", alias='" + alias + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                ", modelo=" + (modelo != null ? modelo.getNombre() : "null") +
                '}';
    }
}
