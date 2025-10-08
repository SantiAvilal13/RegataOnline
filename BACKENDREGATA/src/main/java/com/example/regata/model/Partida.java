package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "partidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"mapa", "ganadorUsuario", "ganadorBarco", "participaciones"})
@ToString(exclude = {"participaciones"})
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida")
    private Long idPartida;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mapa", nullable = false)
    @NotNull(message = "El mapa es obligatorio")
    private Mapa mapa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ganador_usuario")
    private Usuario ganadorUsuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ganador_barco")
    private Barco ganadorBarco;
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participacion> participaciones;
    
    // Enum para los estados
    public enum Estado {
        ESPERANDO, EN_JUEGO, TERMINADA
    }
    
    // Métodos de negocio
    public void iniciarPartida() {
        this.estado = Estado.EN_JUEGO;
    }
    
    public void terminarPartida(Usuario ganadorUsuario, Barco ganadorBarco) {
        this.estado = Estado.TERMINADA;
        this.fechaFin = LocalDateTime.now();
        this.ganadorUsuario = ganadorUsuario;
        this.ganadorBarco = ganadorBarco;
    }
}

