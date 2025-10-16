package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_participacion", "turno"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"participacion", "celdaDestino"})
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;
    
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
    
    @Column(name = "delta_vx", nullable = false)
    private Integer deltaVx;
    
    @Column(name = "delta_vy", nullable = false)
    private Integer deltaVy;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Resultado resultado = Resultado.OK;
    
    @Column(name = "fecha_movimiento")
    @Builder.Default
    private LocalDateTime fechaMovimiento = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participacion", nullable = false)
    @NotNull(message = "La participación es obligatoria")
    private Participacion participacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_celda_destino")
    private Celda celdaDestino;
    
    // Enum para los resultados del movimiento
    public enum Resultado {
        OK, COLISION_PARED, COLISION_META, FUERA_MAPA
    }
    
    // Métodos de utilidad
    public boolean fueExitoso() {
        return Resultado.OK.equals(this.resultado);
    }
    
    public boolean huboColision() {
        return Resultado.COLISION_PARED.equals(this.resultado);
    }
    
    public boolean llegoAMeta() {
        return Resultado.COLISION_META.equals(this.resultado);
    }
    
    public boolean salioDelMapa() {
        return Resultado.FUERA_MAPA.equals(this.resultado);
    }
    
    // Constructor para movimiento inicial (turno 0)
    public static Movimiento movimientoInicial(Participacion participacion, Celda celdaInicial) {
        return Movimiento.builder()
                .turno(0)
                .posX(celdaInicial.getCoordX())
                .posY(celdaInicial.getCoordY())
                .velX(0)
                .velY(0)
                .deltaVx(0)
                .deltaVy(0)
                .resultado(Resultado.OK)
                .participacion(participacion)
                .celdaDestino(celdaInicial)
                .build();
    }
}

