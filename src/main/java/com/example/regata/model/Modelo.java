package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modelos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"barcos"})
@ToString(exclude = {"barcos"})
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_modelo")
    private UUID idModelo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El color es obligatorio")
    @Column(name = "color_hex", nullable = false)
    private String colorHex;
    
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Barco> barcos;
}
