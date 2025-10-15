package com.example.regata.service.impl;

import com.example.regata.exception.GameException;
import com.example.regata.model.Movimiento;
import com.example.regata.model.Participacion;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.model.Partida;
import com.example.regata.repository.MovimientoRepository;
import com.example.regata.repository.ParticipacionRepository;
import com.example.regata.repository.CeldaRepository;
import com.example.regata.service.MovimientoService;
import com.example.regata.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Autowired
    private ParticipacionRepository participacionRepository;
    
    @Autowired
    private CeldaRepository celdaRepository;
    
    @Autowired
    private PartidaService partidaService;
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findAll() {
        return movimientoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> findById(Long id) {
        return movimientoRepository.findById(id);
    }
    
    @Override
    public Movimiento save(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }
    
    @Override
    public void deleteById(Long id) {
        if (!movimientoRepository.existsById(id)) {
            throw new GameException("No se encontró el movimiento con ID: " + id);
        }
        movimientoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByParticipacionId(Long participacionId) {
        return movimientoRepository.findByParticipacionId(participacionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> findByParticipacionIdAndTurno(Long participacionId, Integer turno) {
        return movimientoRepository.findByParticipacionIdAndTurno(participacionId, turno);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByParticipacionIdAndTurnoBetween(Long participacionId, Integer turnoInicio, Integer turnoFin) {
        return movimientoRepository.findByParticipacionIdAndTurnoBetween(participacionId, turnoInicio, turnoFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> obtenerEstadoActual(Long participacionId) {
        System.out.println("🔍 DEBUG - obtenerEstadoActual: buscando último movimiento para participacionId=" + participacionId);
        
        try {
            System.out.println("🔍 DEBUG - Llamando a findUltimoMovimiento...");
            Optional<Movimiento> resultado = movimientoRepository.findUltimoMovimiento(participacionId);
            System.out.println("🔍 DEBUG - findUltimoMovimiento completado, resultado encontrado=" + resultado.isPresent());
            
            if (resultado.isPresent()) {
                System.out.println("🔍 DEBUG - obtenerEstadoActual: movimiento encontrado - Turno=" + resultado.get().getTurno() + ", Pos=(" + resultado.get().getPosX() + "," + resultado.get().getPosY() + ")");
            }
            return resultado;
        } catch (Exception e) {
            System.out.println("❌ DEBUG - Excepción en obtenerEstadoActual: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerHistorialCompleto(Long participacionId) {
        return movimientoRepository.findHistorialCompleto(participacionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findMovimientosConColision() {
        return movimientoRepository.findMovimientosConColision();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findMovimientosConColisionByParticipacionId(Long participacionId) {
        return movimientoRepository.findMovimientosConColisionByParticipacionId(participacionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByResultado(Movimiento.Resultado resultado) {
        return movimientoRepository.findByResultado(resultado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByParticipacionIdAndResultado(Long participacionId, Movimiento.Resultado resultado) {
        return movimientoRepository.findByParticipacionIdAndResultado(participacionId, resultado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByParticipacionIdAndPosicion(Long participacionId, Integer posX, Integer posY) {
        return movimientoRepository.findByParticipacionIdAndPosicion(participacionId, posX, posY);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByParticipacionIdAndVelocidad(Long participacionId, Integer velX, Integer velY) {
        return movimientoRepository.findByParticipacionIdAndVelocidad(participacionId, velX, velY);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findByDeltaVelocidad(Integer deltaVx, Integer deltaVy) {
        return movimientoRepository.findByDeltaVelocidad(deltaVx, deltaVy);
    }
    
    @Override
    public Movimiento realizarMovimiento(Long participacionId, Integer deltaVx, Integer deltaVy) {
        System.out.println("🚀 DEBUG - Realizando movimiento: participacionId=" + participacionId + ", deltaVx=" + deltaVx + ", deltaVy=" + deltaVy);
        
        // Debug: Listar todas las participaciones disponibles
        List<Participacion> todasLasParticipaciones = participacionRepository.findAll();
        System.out.println("📋 DEBUG - Participaciones disponibles: " + todasLasParticipaciones.size());
        for (Participacion p : todasLasParticipaciones) {
            System.out.println("  - ID: " + p.getIdParticipacion() + ", Estado: " + p.getEstado());
        }
        
        // Validar delta de velocidad
        if (deltaVx < -1 || deltaVx > 1 || deltaVy < -1 || deltaVy > 1) {
            throw new GameException("Los cambios de velocidad deben estar entre -1 y 1");
        }
        
        // Obtener la participación
        System.out.println("🔍 DEBUG - Buscando participación con ID: " + participacionId);
        Participacion participacion = participacionRepository.findById(participacionId)
                .orElseThrow(() -> {
                    System.out.println("❌ DEBUG - No se encontró participación con ID: " + participacionId);
                    return new GameException("No se encontró la participación con ID: " + participacionId);
                });
        
        System.out.println("👤 DEBUG - Participación encontrada: ID=" + participacion.getIdParticipacion() + ", Estado=" + participacion.getEstado());
        
        // Verificar que la participación esté activa
        if (!participacion.estaActiva()) {
            System.out.println("❌ DEBUG - Participación no activa: " + participacion.getEstado());
            throw new GameException("La participación no está activa, no se puede realizar movimiento");
        }
        
        System.out.println("✅ DEBUG - Participación activa, continuando...");
        
        // Obtener el estado actual (último movimiento)
        System.out.println("🔍 DEBUG - Llamando a obtenerEstadoActual para participacionId=" + participacionId);
        Optional<Movimiento> estadoActualOpt = obtenerEstadoActual(participacionId);
        System.out.println("🔍 DEBUG - Estado actual encontrado: " + estadoActualOpt.isPresent());
        
        Movimiento estadoActual = estadoActualOpt
                .orElseThrow(() -> {
                    System.out.println("❌ DEBUG - No se encontró estado actual para participación: " + participacionId);
                    return new GameException("No se encontró el estado actual de la participación");
                });
        
        System.out.println("📍 DEBUG - Estado actual: Pos(" + estadoActual.getPosX() + ", " + estadoActual.getPosY() + ") Vel(" + estadoActual.getVelX() + ", " + estadoActual.getVelY() + ")");
        
        // Calcular nueva velocidad
        int nuevaVelX = estadoActual.getVelX() + deltaVx;
        int nuevaVelY = estadoActual.getVelY() + deltaVy;
        
        // Calcular nueva posición
        int nuevaPosX = estadoActual.getPosX() + nuevaVelX;
        int nuevaPosY = estadoActual.getPosY() + nuevaVelY;
        
        System.out.println("🧮 DEBUG - Nueva velocidad: (" + nuevaVelX + ", " + nuevaVelY + ") Nueva posición: (" + nuevaPosX + ", " + nuevaPosY + ")");
        
        // Obtener el mapa para validar límites
        Mapa mapa = participacion.getPartida().getMapa();
        
        // Validar límites del mapa
        Movimiento.Resultado resultado = Movimiento.Resultado.OK;
        if (nuevaPosX < 0 || nuevaPosX >= mapa.getTamFilas() || 
            nuevaPosY < 0 || nuevaPosY >= mapa.getTamColumnas()) {
            resultado = Movimiento.Resultado.FUERA_MAPA;
        }
        
        // Si no salió del mapa, verificar colisiones
        if (resultado == Movimiento.Resultado.OK) {
            System.out.println("🔍 DEBUG - Validando colisión en posición: (" + nuevaPosX + ", " + nuevaPosY + ")");
            
            // Buscar celda en la nueva posición
            Optional<Celda> celdaDestino = celdaRepository.findByMapaAndCoordXAndCoordY(mapa, nuevaPosX, nuevaPosY);
            
            if (celdaDestino.isPresent()) {
                Celda celda = celdaDestino.get();
                System.out.println("🔍 DEBUG - Celda encontrada: Tipo=" + celda.getTipo() + " en (" + nuevaPosX + ", " + nuevaPosY + ")");
                
                switch (celda.getTipo()) {
                    case PARED:
                        resultado = Movimiento.Resultado.COLISION_PARED;
                        System.out.println("❌ DEBUG - Colisión con PARED detectada");
                        break;
                    case META:
                        resultado = Movimiento.Resultado.COLISION_META;
                        System.out.println("🎯 DEBUG - Llegada a META");
                        break;
                    case AGUA:
                    case PARTIDA:
                        resultado = Movimiento.Resultado.OK;
                        System.out.println("✅ DEBUG - Movimiento válido");
                        break;
                }
            } else {
                System.out.println("⚠️ DEBUG - No se encontró celda en (" + nuevaPosX + ", " + nuevaPosY + ")");
                resultado = Movimiento.Resultado.FUERA_MAPA;
            }
        }
        
        // Crear nuevo movimiento
        Movimiento nuevoMovimiento = Movimiento.builder()
                .turno(getSiguienteTurno(participacionId))
                .posX(nuevaPosX)
                .posY(nuevaPosY)
                .velX(nuevaVelX)
                .velY(nuevaVelY)
                .deltaVx(deltaVx)
                .deltaVy(deltaVy)
                .resultado(resultado)
                .participacion(participacion)
                .celdaDestino(resultado == Movimiento.Resultado.OK ? 
                    celdaRepository.findByMapaAndCoordXAndCoordY(mapa, nuevaPosX, nuevaPosY).orElse(null) : null)
                .build();
        
        // Guardar el movimiento
        nuevoMovimiento = movimientoRepository.save(nuevoMovimiento);
        
        // Actualizar estado de la participación si es necesario
        if (resultado == Movimiento.Resultado.COLISION_PARED) {
            participacion.destruir();
            participacionRepository.save(participacion);
        } else if (resultado == Movimiento.Resultado.COLISION_META) {
            participacion.llegarAMeta();
            participacionRepository.save(participacion);
            
            // ¡VICTORIA! Terminar la partida con el ganador
            Partida partida = participacion.getPartida();
            partida.terminarPartida(participacion.getJugador(), participacion.getBarco());
            partidaService.save(partida);
            
            System.out.println("🏆 VICTORIA! Jugador " + participacion.getJugador().getNombre() + 
                             " con barco " + participacion.getBarco().getAlias() + 
                             " ganó la partida " + partida.getIdPartida());
        }
        
        return nuevoMovimiento;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer getSiguienteTurno(Long participacionId) {
        return movimientoRepository.getSiguienteTurno(participacionId);
    }
    
        @Override
        @Transactional(readOnly = true)
        public Long countByParticipacionId(Long participacionId) {
            return movimientoRepository.countByParticipacionId(participacionId);
        }
        
        @Override
        @Transactional(readOnly = true)
        public List<Map<String, Object>> obtenerDestinosPosibles(Long participacionId) {
            System.out.println("🎯 DEBUG - Calculando destinos posibles para participacionId=" + participacionId);
            
            // Obtener la participación
            Participacion participacion = participacionRepository.findById(participacionId)
                    .orElseThrow(() -> new GameException("No se encontró la participación con ID: " + participacionId));
            
            // Obtener el estado actual
            Optional<Movimiento> estadoActualOpt = obtenerEstadoActual(participacionId);
            Movimiento estadoActual = estadoActualOpt
                    .orElseThrow(() -> new GameException("No se encontró el estado actual de la participación"));
            
            // Obtener el mapa
            Mapa mapa = participacion.getPartida().getMapa();
            
            List<Map<String, Object>> destinos = new java.util.ArrayList<>();
            boolean colisionInevitable = true; // Asumimos colisión inevitable hasta probar lo contrario
            
            // Calcular todos los destinos posibles considerando los límites de delta [-1,0,1]
            for (int deltaVx = -1; deltaVx <= 1; deltaVx++) {
                for (int deltaVy = -1; deltaVy <= 1; deltaVy++) {
                    // Calcular nueva velocidad
                    int nuevaVelX = estadoActual.getVelX() + deltaVx;
                    int nuevaVelY = estadoActual.getVelY() + deltaVy;
                    
                    // Calcular nueva posición
                    int nuevaPosX = estadoActual.getPosX() + nuevaVelX;
                    int nuevaPosY = estadoActual.getPosY() + nuevaVelY;
                    
                    // Validar límites del mapa
                    if (nuevaPosX < 0 || nuevaPosX >= mapa.getTamFilas() || 
                        nuevaPosY < 0 || nuevaPosY >= mapa.getTamColumnas()) {
                        continue; // Fuera del mapa
                    }
                    
                    // Buscar la celda destino
                    Optional<Celda> celdaDestino = celdaRepository.findByMapaAndCoordXAndCoordY(mapa, nuevaPosX, nuevaPosY);
                    
                    if (celdaDestino.isPresent()) {
                        Celda celda = celdaDestino.get();
                        
                        // Si encontramos al menos un destino válido (no pared), no hay colisión inevitable
                        if (celda.getTipo() != Celda.Tipo.PARED) {
                            colisionInevitable = false;
                            
                            // Crear información del destino
                            Map<String, Object> destino = new HashMap<>();
                            destino.put("x", nuevaPosX);
                            destino.put("y", nuevaPosY);
                            destino.put("deltaVx", deltaVx);
                            destino.put("deltaVy", deltaVy);
                            destino.put("nuevaVelX", nuevaVelX);
                            destino.put("nuevaVelY", nuevaVelY);
                            destino.put("tipo", celda.getTipo().name());
                            destino.put("valido", true);
                            
                            // Determinar si es un movimiento especial
                            if (celda.getTipo() == Celda.Tipo.META) {
                                destino.put("esMeta", true);
                            } else {
                                destino.put("esMeta", false);
                            }
                            
                            destinos.add(destino);
                        }
                    }
                }
            }
            
            // Si hay colisión inevitable, agregar información especial
            if (colisionInevitable) {
                System.out.println("💥 DEBUG - Colisión inevitable detectada para participacionId=" + participacionId);
                
                // Encontrar la pared más cercana donde va a chocar
                int posColisionX = estadoActual.getPosX() + estadoActual.getVelX();
                int posColisionY = estadoActual.getPosY() + estadoActual.getVelY();
                
                // Si está fuera del mapa, buscar la pared más cercana en la dirección del movimiento
                if (posColisionX < 0 || posColisionX >= mapa.getTamFilas() || 
                    posColisionY < 0 || posColisionY >= mapa.getTamColumnas()) {
                    
                    // Buscar paredes en la dirección del movimiento
                    int velX = estadoActual.getVelX();
                    int velY = estadoActual.getVelY();
                    
                    if (velX < 0) {
                        // Moviéndose hacia la izquierda, buscar pared en x=0
                        posColisionX = 0;
                        posColisionY = estadoActual.getPosY();
                    } else if (velX > 0) {
                        // Moviéndose hacia la derecha, buscar pared en x=max
                        posColisionX = mapa.getTamFilas() - 1;
                        posColisionY = estadoActual.getPosY();
                    }
                    
                    if (velY < 0) {
                        // Moviéndose hacia arriba, buscar pared en y=0
                        posColisionY = 0;
                    } else if (velY > 0) {
                        // Moviéndose hacia abajo, buscar pared en y=max
                        posColisionY = mapa.getTamColumnas() - 1;
                    }
                }
                
                Map<String, Object> destinoColision = new HashMap<>();
                destinoColision.put("x", posColisionX);
                destinoColision.put("y", posColisionY);
                destinoColision.put("deltaVx", 0);
                destinoColision.put("deltaVy", 0);
                destinoColision.put("nuevaVelX", estadoActual.getVelX());
                destinoColision.put("nuevaVelY", estadoActual.getVelY());
                destinoColision.put("tipo", "PARED");
                destinoColision.put("valido", true); 
                destinoColision.put("colisionInevitable", true);
                destinoColision.put("esMeta", false);
                
                destinos.add(destinoColision);
                
                System.out.println("💥 DEBUG - Colisión inevitable en posición (" + posColisionX + ", " + posColisionY + ")");
            }
            
            System.out.println("🎯 DEBUG - Encontrados " + destinos.size() + " destinos posibles" + 
                             (colisionInevitable ? " (incluyendo colisión inevitable)" : ""));
            return destinos;
        }
    }
