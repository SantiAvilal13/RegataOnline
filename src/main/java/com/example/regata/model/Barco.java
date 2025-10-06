package com.example.regata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "barcos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"usuario", "modelo", "participaciones"})
@ToString(exclude = {"participaciones"})
public class Barco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_barco")
    private UUID idBarco;
    
    @NotBlank(message = "El alias es obligatorio")
    @Column(nullable = false)
    private String alias;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "El usuario propietario es obligatorio")
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo", nullable = false)
    @NotNull(message = "El modelo es obligatorio")
    private Modelo modelo;
    
    @OneToMany(mappedBy = "barco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participacion> participaciones;
}
