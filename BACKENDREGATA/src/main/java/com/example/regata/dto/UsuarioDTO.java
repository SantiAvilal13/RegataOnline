package com.example.regata.dto;

import com.example.regata.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UsuarioDTO {
    
    private Long idUsuario;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    @NotNull(message = "El rol es obligatorio")
    private Usuario.Rol rol;
    
    // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
    private Long totalPartidasGanadas;
    private Long totalBarcos;
    
    // Constructores
    public UsuarioDTO() {}
    
    public UsuarioDTO(String nombre, String email, String password, Usuario.Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    
    public UsuarioDTO(Long idUsuario, String nombre, String email, Usuario.Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
    
    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Usuario.Rol getRol() {
        return rol;
    }
    
    public void setRol(Usuario.Rol rol) {
        this.rol = rol;
    }
    
    // Métodos de relaciones removidos para evitar referencias circulares
    
    public Long getTotalPartidasGanadas() {
        return totalPartidasGanadas;
    }
    
    public void setTotalPartidasGanadas(Long totalPartidasGanadas) {
        this.totalPartidasGanadas = totalPartidasGanadas;
    }
    
    public Long getTotalBarcos() {
        return totalBarcos;
    }
    
    public void setTotalBarcos(Long totalBarcos) {
        this.totalBarcos = totalBarcos;
    }
    
    // Métodos de utilidad
    public boolean esJugador() {
        return Usuario.Rol.JUGADOR.equals(this.rol);
    }
    
    public boolean esAdmin() {
        return Usuario.Rol.ADMIN.equals(this.rol);
    }
    
    public String getRolDisplayName() {
        return rol != null ? rol.name() : "";
    }
    
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                '}';
    }
}

