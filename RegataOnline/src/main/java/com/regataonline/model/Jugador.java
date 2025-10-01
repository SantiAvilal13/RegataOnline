package com.regataonline.model;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jugadores")
public class Jugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(name = "experiencia", nullable = false)
    private Integer experiencia = 0;
    
    @Column(name = "victorias", nullable = false)
    private Integer victorias = 0;
    
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
    
    // Constructores
    public Jugador() {}
    
    public Jugador(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.experiencia = 0;
        this.victorias = 0;
    }
    
    // Constructor con experiencia y victorias para DbInitializer
    public Jugador(String nombre, String email, String telefono, Integer experiencia, Integer victorias) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.experiencia = experiencia;
        this.victorias = victorias;
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
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Integer getExperiencia() {
        return experiencia;
    }
    
    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }
    
    public Integer getVictorias() {
        return victorias;
    }
    
    public void setVictorias(Integer victorias) {
        this.victorias = victorias;
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
                ", experiencia=" + experiencia +
                ", victorias=" + victorias +
                '}';
    }
}
