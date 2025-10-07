package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
 

@Entity
@Table(name = "movimientos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_participacion", "turno"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"participacion", "celda"})
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mov")
    private Long idMov;
    
    @Column(nullable = false)
    private Integer turno;
    
    @Column(name = "pos_x", nullable = false)
    private Integer posX;
    
    @Column(name = "pos_y", nullable = false)
    private Integer posY;
    
    @Column(name = "vel_x", nullable = false)
    private Integer velX;
    
    @Column(name = "vel_y", nullable = false)
    private Integer velY;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delta_vx", nullable = false)
    private DeltaVelocidad deltaVx;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delta_vy", nullable = false)
    private DeltaVelocidad deltaVy;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean colision = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participacion", nullable = false)
    @NotNull(message = "La participación es obligatoria")
    private Participacion participacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_celda")
    private Celda celda;
    
    // Enum para los cambios de velocidad
    public enum DeltaVelocidad {
        DECREMENTO(-1), MANTENER(0), INCREMENTO(1);
        
        private final int valor;
        
        DeltaVelocidad(int valor) {
            this.valor = valor;
        }
        
        public int getValor() {
            return valor;
        }
    }
}

