package com.regataonline.service;

import com.regataonline.model.Modelo;
import com.regataonline.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModeloService {
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    // Crear un nuevo modelo
    public Modelo crearModelo(Modelo modelo) {
        // Validaciones básicas
        if (modelo.getNombre() == null || modelo.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del modelo es obligatorio");
        }
        
        // Verificar que no exista un modelo con el mismo nombre
        Optional<Modelo> modeloExistente = modeloRepository.findByNombre(modelo.getNombre());
        if (modeloExistente.isPresent()) {
            throw new RuntimeException("Ya existe un modelo con el nombre: " + modelo.getNombre());
        }
        
        // Validar valores numéricos
        if (modelo.getVelocidadMaxima() == null || modelo.getVelocidadMaxima() <= 0) {
            throw new RuntimeException("La velocidad máxima debe ser mayor a 0");
        }
        
        if (modelo.getResistencia() == null || modelo.getResistencia() <= 0) {
            throw new RuntimeException("La resistencia debe ser mayor a 0");
        }
        
        if (modelo.getManiobrabilidad() == null || modelo.getManiobrabilidad() <= 0) {
            throw new RuntimeException("La maniobrabilidad debe ser mayor a 0");
        }
        
        if (modelo.getCapacidadCombustible() == null || modelo.getCapacidadCombustible() <= 0) {
            throw new RuntimeException("La capacidad de combustible debe ser mayor a 0");
        }
        
        return modeloRepository.save(modelo);
    }
    
    // Obtener todos los modelos
    @Transactional(readOnly = true)
    public List<Modelo> obtenerTodosLosModelos() {
        return modeloRepository.findAll();
    }
    
    // Obtener modelo por ID
    @Transactional(readOnly = true)
    public Optional<Modelo> obtenerModeloPorId(Long id) {
        return modeloRepository.findById(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public Optional<Modelo> obtenerPorId(Long id) {
        return obtenerModeloPorId(id);
    }
    
    // Alias para compatibilidad con controlador
    @Transactional(readOnly = true)
    public List<Modelo> obtenerTodos() {
        return obtenerTodosLosModelos();
    }
    
    // Obtener modelo por nombre
    @Transactional(readOnly = true)
    public Optional<Modelo> obtenerModeloPorNombre(String nombre) {
        return modeloRepository.findByNombre(nombre);
    }
    
    // Buscar modelos por tipo
    @Transactional(readOnly = true)
    public List<Modelo> buscarModelosPorTipo(String tipo) {
        return modeloRepository.findByTipoContainingIgnoreCase(tipo);
    }
    
    // Buscar modelos por rango de velocidad
    @Transactional(readOnly = true)
    public List<Modelo> buscarModelosPorRangoVelocidad(Double velocidadMin, Double velocidadMax) {
        return modeloRepository.findByVelocidadMaximaBetween(velocidadMin, velocidadMax);
    }
    
    // Actualizar modelo
    public Modelo actualizarModelo(Long id, Modelo modeloActualizado) {
        Optional<Modelo> modeloExistente = modeloRepository.findById(id);
        
        if (modeloExistente.isEmpty()) {
            throw new RuntimeException("Modelo no encontrado con ID: " + id);
        }
        
        Modelo modelo = modeloExistente.get();
        
        // Validar nombre único si se está cambiando
        if (!modelo.getNombre().equals(modeloActualizado.getNombre())) {
            Optional<Modelo> modeloConMismoNombre = modeloRepository.findByNombre(modeloActualizado.getNombre());
            if (modeloConMismoNombre.isPresent()) {
                throw new RuntimeException("Ya existe un modelo con el nombre: " + modeloActualizado.getNombre());
            }
        }
        
        // Validar valores numéricos
        if (modeloActualizado.getVelocidadMaxima() <= 0) {
            throw new RuntimeException("La velocidad máxima debe ser mayor a 0");
        }
        
        if (modeloActualizado.getResistencia() <= 0) {
            throw new RuntimeException("La resistencia debe ser mayor a 0");
        }
        
        if (modeloActualizado.getManiobrabilidad() <= 0) {
            throw new RuntimeException("La maniobrabilidad debe ser mayor a 0");
        }
        
        if (modeloActualizado.getCapacidadCombustible() <= 0) {
            throw new RuntimeException("La capacidad de combustible debe ser mayor a 0");
        }
        
        // Actualizar campos
        modelo.setNombre(modeloActualizado.getNombre());
        modelo.setDescripcion(modeloActualizado.getDescripcion());
        modelo.setVelocidadMaxima(modeloActualizado.getVelocidadMaxima());
        modelo.setResistencia(modeloActualizado.getResistencia());
        modelo.setManiobrabilidad(modeloActualizado.getManiobrabilidad());
        modelo.setCapacidadCombustible(modeloActualizado.getCapacidadCombustible());
        modelo.setTipo(modeloActualizado.getTipo());
        
        return modeloRepository.save(modelo);
    }
    
    // Eliminar modelo
    public void eliminarModelo(Long id) {
        if (!modeloRepository.existsById(id)) {
            throw new RuntimeException("Modelo no encontrado con ID: " + id);
        }
        
        // Verificar si el modelo tiene barcos activos
        Long barcosActivos = modeloRepository.countBarcosActivosByModeloId(id);
        if (barcosActivos > 0) {
            throw new RuntimeException("No se puede eliminar el modelo porque tiene " + barcosActivos + " barcos activos");
        }
        
        modeloRepository.deleteById(id);
    }
    
    // Obtener modelos más populares
    @Transactional(readOnly = true)
    public List<Modelo> obtenerModelosPopulares() {
        return modeloRepository.findModelosMasPopulares();
    }
    
    // Obtener modelos equilibrados
    @Transactional(readOnly = true)
    public List<Modelo> obtenerModelosEquilibrados() {
        return modeloRepository.findModelosBalanceados(50, 50, 50);
    }
    
    // Obtener todos los tipos únicos de modelos
    @Transactional(readOnly = true)
    public List<String> obtenerTiposUnicos() {
        return modeloRepository.findAllTipos();
    }
    
    // Buscar modelos por velocidad mínima
    @Transactional(readOnly = true)
    public List<Modelo> buscarModelosPorVelocidadMinima(Double velocidadMinima) {
        return modeloRepository.findByVelocidadMaximaGreaterThanEqual(velocidadMinima);
    }
    
    // Buscar modelos por resistencia mínima
    @Transactional(readOnly = true)
    public List<Modelo> buscarModelosPorResistenciaMinima(Double resistenciaMinima) {
        return modeloRepository.findByResistenciaGreaterThanEqual(resistenciaMinima);
    }
    
    // Buscar modelos por maniobrabilidad mínima
    @Transactional(readOnly = true)
    public List<Modelo> buscarModelosPorManiobrabilidadMinima(Double maniobrabilidadMinima) {
        return modeloRepository.findByManiobrabilidadGreaterThanEqual(maniobrabilidadMinima);
    }
    
    // Contar barcos activos por modelo
    @Transactional(readOnly = true)
    public Long contarBarcosActivos(Long modeloId) {
        return modeloRepository.countBarcosActivosByModeloId(modeloId);
    }
    
    // Calcular puntuación total de un modelo
    @Transactional(readOnly = true)
    public Double calcularPuntuacionTotal(Long modeloId) {
        Optional<Modelo> modeloOpt = modeloRepository.findById(modeloId);
        
        if (modeloOpt.isEmpty()) {
            throw new RuntimeException("Modelo no encontrado con ID: " + modeloId);
        }
        
        Modelo modelo = modeloOpt.get();
        return modelo.getVelocidadMaxima().doubleValue() + modelo.getResistencia().doubleValue() + modelo.getManiobrabilidad().doubleValue();
    }
}
