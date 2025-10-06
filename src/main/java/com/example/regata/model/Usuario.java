package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario")
    private UUID idUsuario;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participacion> participaciones;
    
    @OneToMany(mappedBy = "ganadorUsuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidasGanadas;
    
    // Enum para los roles
    public enum Rol {
        ADMIN, JUGADOR
    }
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String nombre, String email, String passwordHash, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }
    
    // Getters y Setters
    public UUID getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
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
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public List<Barco> getBarcos() {
        return barcos;
    }
    
    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }
    
    public List<Participacion> getParticipaciones() {
        return participaciones;
    }
    
    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }
    
    public List<Partida> getPartidasGanadas() {
        return partidasGanadas;
    }
    
    public void setPartidasGanadas(List<Partida> partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                '}';
    }
}

