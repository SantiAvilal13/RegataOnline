package com.example.regata.repository;

import com.example.regata.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT u FROM Usuario u ORDER BY u.nombre ASC")
    List<Usuario> findAllOrderByNombreAsc();
    
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.usuario.idUsuario = :usuarioId")
    Long countBarcosByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByRol(Usuario.Rol rol);
    
    Long countByRol(Usuario.Rol rol);
}