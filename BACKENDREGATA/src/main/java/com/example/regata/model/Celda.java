package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "celdas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_mapa", "coord_x", "coord_y"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"mapa"})
public class Celda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_celda")
    private Long idCelda;
    
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
    
    // Relación eliminada - Los movimientos no están directamente asociados a celdas
    // sino que representan el estado del barco en cada turno
    
    // Enum para los tipos de celda
    public enum Tipo {
        AGUA, PARED, PARTIDA, META
    }
}

