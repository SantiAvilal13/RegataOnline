package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mapas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"celdas", "partidas"})
@ToString(exclude = {"celdas", "partidas"})
public class Mapa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mapa")
    private UUID idMapa;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Positive(message = "El tamaño de filas debe ser positivo")
    @Column(name = "tam_filas", nullable = false)
    private Integer tamFilas;
    
    @Positive(message = "El tamaño de columnas debe ser positivo")
    @Column(name = "tam_columnas", nullable = false)
    private Integer tamColumnas;
    
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Celda> celdas;
    
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidas;
}

