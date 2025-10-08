package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "participaciones",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_partida", "id_barco"}),
           @UniqueConstraint(columnNames = {"id_partida", "id_jugador"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"partida", "barco", "jugador", "movimientos"})
@ToString(exclude = {"movimientos"})
public class Participacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacion")
    private Long idParticipacion;
    
    @Column(name = "turno_actual", nullable = false)
    @Builder.Default
    private Integer turnoActual = 0;
    
    @Column(name = "pos_x", nullable = false)
    private Integer posX;
    
    @Column(name = "pos_y", nullable = false)
    private Integer posY;
    
    @Column(name = "vel_x", nullable = false)
    @Builder.Default
    private Integer velX = 0;
    
    @Column(name = "vel_y", nullable = false)
    @Builder.Default
    private Integer velY = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida", nullable = false)
    @NotNull(message = "La partida es obligatoria")
    private Partida partida;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barco", nullable = false)
    @NotNull(message = "El barco es obligatorio")
    private Barco barco;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    @NotNull(message = "El jugador es obligatorio")
    private Usuario jugador;
    
    @OneToMany(mappedBy = "participacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos;
    
    // Enum para los estados
    public enum Estado {
        ACTIVO, DESTRUIDO, EN_META
    }
    
    // Métodos de negocio
    public void avanzarTurno() {
        this.turnoActual++;
    }
    
    public void mover(Integer nuevaPosX, Integer nuevaPosY) {
        this.posX = nuevaPosX;
        this.posY = nuevaPosY;
    }
    
    public void actualizarVelocidad(Integer nuevaVelX, Integer nuevaVelY) {
        this.velX = nuevaVelX;
        this.velY = nuevaVelY;
    }
    
    public void destruir() {
        this.estado = Estado.DESTRUIDO;
    }
    
    public void llegarAMeta() {
        this.estado = Estado.EN_META;
    }
}

