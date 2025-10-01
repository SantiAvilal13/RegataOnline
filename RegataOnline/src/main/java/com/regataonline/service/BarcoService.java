package com.regataonline.service;

import com.regataonline.model.Barco;
import com.regataonline.model.Jugador;
import com.regataonline.model.Modelo;
import com.regataonline.repository.BarcoRepository;
import com.regataonline.repository.JugadorRepository;
import com.regataonline.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BarcoService {
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    // Crear un nuevo barco
    public Barco crearBarco(Barco barco) {
        // Validaciones básicas
        if (barco.getNombre() == null || barco.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del barco es obligatorio");
        }
        
        if (barco.getJugador() == null || barco.getJugador().getId() == null) {
            throw new RuntimeException("El jugador es obligatorio");
        }
        
        if (barco.getModelo() == null || barco.getModelo().getId() == null) {
            throw new RuntimeException("El modelo es obligatorio");
        }
        
        // Verificar que el jugador existe
        Optional<Jugador> jugador = jugadorRepository.findById(barco.getJugador().getId());
        if (jugador.isEmpty()) {
            throw new RuntimeException("Jugador no encontrado con ID: " + barco.getJugador().getId());
        }
        
        // Verificar que el modelo existe
        Optional<Modelo> modelo = modeloRepository.findById(barco.getModelo().getId());
        if (modelo.isEmpty()) {
            throw new RuntimeException("Modelo no encontrado con ID: " + barco.getModelo().getId());
        }
        
        // Establecer relaciones
        barco.setJugador(jugador.get());
        barco.setModelo(modelo.get());
        
        // Inicializar valores por defecto
        if (barco.getNivelCombustible() == null) {
            barco.setNivelCombustible(modelo.get().getCapacidadCombustible());
        }
        
        if (barco.getEstadoSalud() == null) {
            barco.setEstadoSalud(100);
        }
        
        if (barco.getExperiencia() == null) {
            barco.setExperiencia(0);
        }
        
        if (barco.getCarrerasGanadas() == null) {
            barco.setCarrerasGanadas(0);
        }
        
        if (barco.getFechaCreacion() == null) {
            barco.setFechaCreacion(LocalDateTime.now());
        }
        
        if (barco.getActivo() == null) {
            barco.setActivo(true);
        }
        
        return barcoRepository.save(barco);
    }
    
    // Obtener todos los barcos
    @Transactional(readOnly = true)
    public List<Barco> obtenerTodosLosBarcos() {
        return barcoRepository.findAll();
    }
    
    // Obtener barco por ID
    @Transactional(readOnly = true)
    public Optional<Barco> obtenerBarcoPorId(Long id) {
        return barcoRepository.findById(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public Optional<Barco> obtenerPorId(Long id) {
        return obtenerBarcoPorId(id);
    }
    
    // Buscar barcos por nombre
    @Transactional(readOnly = true)
    public List<Barco> buscarBarcosPorNombre(String nombre) {
        return barcoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Obtener barcos por jugador
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosPorJugador(Long jugadorId) {
        return barcoRepository.findByJugadorId(jugadorId);
    }
    
    // Obtener barcos por modelo
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosPorModelo(Long modeloId) {
        return barcoRepository.findByModeloId(modeloId);
    }
    
    // Obtener barcos activos
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosActivos() {
        return barcoRepository.findByActivoTrue();
    }
    
    // Obtener barcos inactivos
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosInactivos() {
        return barcoRepository.findByActivoFalse();
    }
    
    // Actualizar barco
    public Barco actualizarBarco(Long id, Barco barcoActualizado) {
        Optional<Barco> barcoExistente = barcoRepository.findById(id);
        
        if (barcoExistente.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoExistente.get();
        
        // Validar y actualizar jugador si se proporciona
        if (barcoActualizado.getJugador() != null && barcoActualizado.getJugador().getId() != null) {
            Optional<Jugador> jugador = jugadorRepository.findById(barcoActualizado.getJugador().getId());
            if (jugador.isEmpty()) {
                throw new RuntimeException("Jugador no encontrado con ID: " + barcoActualizado.getJugador().getId());
            }
            barco.setJugador(jugador.get());
        }
        
        // Validar y actualizar modelo si se proporciona
        if (barcoActualizado.getModelo() != null && barcoActualizado.getModelo().getId() != null) {
            Optional<Modelo> modelo = modeloRepository.findById(barcoActualizado.getModelo().getId());
            if (modelo.isEmpty()) {
                throw new RuntimeException("Modelo no encontrado con ID: " + barcoActualizado.getModelo().getId());
            }
            barco.setModelo(modelo.get());
        }
        
        // Actualizar campos básicos
        if (barcoActualizado.getNombre() != null) {
            barco.setNombre(barcoActualizado.getNombre());
        }
        
        if (barcoActualizado.getColor() != null) {
            barco.setColor(barcoActualizado.getColor());
        }
        
        if (barcoActualizado.getNivelCombustible() != null) {
            barco.setNivelCombustible(barcoActualizado.getNivelCombustible());
        }
        
        if (barcoActualizado.getEstadoSalud() != null) {
            barco.setEstadoSalud(barcoActualizado.getEstadoSalud());
        }
        
        if (barcoActualizado.getExperiencia() != null) {
            barco.setExperiencia(barcoActualizado.getExperiencia());
        }
        
        if (barcoActualizado.getCarrerasGanadas() != null) {
            barco.setCarrerasGanadas(barcoActualizado.getCarrerasGanadas());
        }
        
        if (barcoActualizado.getActivo() != null) {
            barco.setActivo(barcoActualizado.getActivo());
        }
        
        return barcoRepository.save(barco);
    }
    
    // Eliminar barco
    public void eliminarBarco(Long id) {
        if (!barcoRepository.existsById(id)) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        barcoRepository.deleteById(id);
    }
    
    // Activar barco
    public Barco activarBarco(Long id) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setActivo(true);
        
        return barcoRepository.save(barco);
    }
    
    // Desactivar barco
    public Barco desactivarBarco(Long id) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setActivo(false);
        
        return barcoRepository.save(barco);
    }
    
    // Reparar barco (restaurar salud al 100%)
    public Barco repararBarco(Long id) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setEstadoSalud(100);
        
        return barcoRepository.save(barco);
    }
    
    // Repostar combustible
    public Barco repostarCombustible(Long id) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setNivelCombustible(barco.getModelo().getCapacidadCombustible());
        
        return barcoRepository.save(barco);
    }
    
    // Incrementar experiencia del barco
    public Barco incrementarExperiencia(Long id, Integer experienciaGanada) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setExperiencia(barco.getExperiencia() + experienciaGanada);
        
        return barcoRepository.save(barco);
    }
    
    // Incrementar carreras ganadas
    public Barco incrementarCarrerasGanadas(Long id) {
        Optional<Barco> barcoOpt = barcoRepository.findById(id);
        
        if (barcoOpt.isEmpty()) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        
        Barco barco = barcoOpt.get();
        barco.setCarrerasGanadas(barco.getCarrerasGanadas() + 1);
        
        return barcoRepository.save(barco);
    }
    
    // Obtener top barcos por experiencia
    @Transactional(readOnly = true)
    public List<Barco> obtenerTopBarcosPorExperiencia() {
        return barcoRepository.findTopBarcosByExperiencia();
    }
    
    // Obtener barcos con poco combustible
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosConPocoCombustible() {
        return barcoRepository.findByNivelCombustibleLessThan(20);
    }
    
    // Obtener barcos dañados
    @Transactional(readOnly = true)
    public List<Barco> obtenerBarcosDanados() {
        return barcoRepository.findByEstadoSaludLessThan(50.0);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> obtenerDañados() {
        return obtenerBarcosDanados();
    }
    
    // Métodos para buscar por entidades
    @Transactional(readOnly = true)
    public List<Barco> buscarPorJugador(Jugador jugador) {
        return barcoRepository.findByJugador(jugador);
    }
    
    @Transactional(readOnly = true)
    public List<Barco> buscarPorModelo(Modelo modelo) {
        return barcoRepository.findByModelo(modelo);
    }
    
    // Buscar barcos por color
    @Transactional(readOnly = true)
    public List<Barco> buscarBarcosPorColor(String color) {
        return barcoRepository.findByColor(color);
    }
    
    // Contar barcos por jugador
    @Transactional(readOnly = true)
    public Long contarBarcosPorJugador(Long jugadorId) {
        return barcoRepository.countByJugadorId(jugadorId);
    }
    
    // Contar barcos por modelo
    @Transactional(readOnly = true)
    public Long contarBarcosPorModelo(Long modeloId) {
        return barcoRepository.countByModeloId(modeloId);
    }
    
    // Alias para compatibilidad con controlador
    public Barco recargarCombustible(Long id) {
        return repostarCombustible(id);
    }
    
    // Alias para compatibilidad con controlador
    public Barco incrementarVictorias(Long id) {
        return incrementarCarrerasGanadas(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> obtenerTopPorExperiencia(int limite) {
        return barcoRepository.findTopByOrderByExperienciaDesc().stream()
                .limit(limite)
                .toList();
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> obtenerTopPorVictorias(int limite) {
        return barcoRepository.findTopByOrderByCarrerasGanadasDesc().stream()
                .limit(limite)
                .toList();
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> obtenerConCombustibleBajo() {
        return obtenerBarcosConPocoCombustible();
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> obtenerTodos() {
        return obtenerTodosLosBarcos();
    }
    
    // Alias para compatibilidad con controlador
    public Barco activar(Long id) {
        return activarBarco(id);
    }
    
    // Alias para compatibilidad con controlador
    public Barco desactivar(Long id) {
        return desactivarBarco(id);
    }
    
    // Alias para compatibilidad con controlador
    public Barco reparar(Long id) {
        return repararBarco(id);
    }
    
    // Alias para compatibilidad con controlador
    public void eliminar(Long id) {
        eliminarBarco(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> buscarPorNombre(String nombre) {
        return buscarBarcosPorNombre(nombre);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> buscarPorColor(String color) {
        return buscarBarcosPorColor(color);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> buscarActivos() {
        return obtenerBarcosActivos();
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Barco> buscarInactivos() {
        return obtenerBarcosInactivos();
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public Long contarBarcosActivos() {
        return barcoRepository.countByActivoTrue();
    }
    
    // Alias para compatibilidad con controlador
    public Barco crear(Barco barco) {
        return crearBarco(barco);
    }
    
    // Alias para compatibilidad con controlador
    public Barco actualizar(Barco barco) {
        return actualizarBarco(barco.getId(), barco);
    }
}
