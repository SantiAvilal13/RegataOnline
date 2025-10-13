package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
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
@EqualsAndHashCode(exclude = {"partida", "barco", "jugador", "movimientos", "celdaInicial"})
@ToString(exclude = {"movimientos"})
public class Participacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacion")
    private Long idParticipacion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;
    
    @Column(name = "orden_turno", nullable = false)
    private Integer ordenTurno;
    
    @Column(name = "fecha_inicio")
    @Builder.Default
    private LocalDateTime fechaInicio = LocalDateTime.now();
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_celda_inicial", nullable = false)
    @NotNull(message = "La celda inicial es obligatoria")
    private Celda celdaInicial;
    
    @OneToMany(mappedBy = "participacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos;
    
    // Enum para los estados
    public enum Estado {
        ACTIVO, DESTRUIDO, EN_META
    }
    
    // Métodos de negocio - solo para cambios de estado
    public void destruir() {
        this.estado = Estado.DESTRUIDO;
        this.fechaFin = LocalDateTime.now();
    }
    
    public void llegarAMeta() {
        this.estado = Estado.EN_META;
        this.fechaFin = LocalDateTime.now();
    }
    
    public boolean estaActiva() {
        return Estado.ACTIVO.equals(this.estado);
    }
    
    public boolean estaDestruida() {
        return Estado.DESTRUIDO.equals(this.estado);
    }
    
    public boolean llegoAMeta() {
        return Estado.EN_META.equals(this.estado);
    }
}

