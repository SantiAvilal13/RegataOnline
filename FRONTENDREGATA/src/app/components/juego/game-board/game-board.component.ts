import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, map } from 'rxjs';
import { MapaJuegoService } from '../../../shared/services/juego/mapa-juego.service';
import { MovimientoJuegoService } from '../../../shared/services/juego/movimiento-juego.service';
import { PartidaService } from '../../../shared/services/partidas/partida.service';
import { Mapa, Celda, Movimiento } from '../../../models';
import { CeldaTipo } from '../../../models/enums/celda-tipo';

@Component({
  selector: 'app-game-board',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.css'
})
export class GameBoardComponent implements OnInit, OnDestroy {
  mapaService = inject(MapaJuegoService);
  movimientoService = inject(MovimientoJuegoService);
  partidaService = inject(PartidaService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  // Estados del juego
  mapa = signal<Mapa | null>(null);
  celdas = signal<Celda[]>([]);
  estadoActual = signal<Movimiento | null>(null);
  estadoAnterior = signal<Movimiento | null>(null);
  destinosPosibles = signal<any[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  
  // Estado multijugador
  todosLosJugadores = signal<any[]>([]);
  partidaId = signal<number | null>(null);
  miParticipacionId = signal<number | null>(null);
  private autoRefreshInterval: any = null;
  private readonly REFRESH_INTERVAL = 3000; // 3 segundos
  
  // Sistema de notificaciones
  notificaciones = signal<{id: number, mensaje: string, tipo: 'ganador' | 'perdedor' | 'info'}[]>([]);
  private notificacionIdCounter = 0;
  private eventosNotificados = new Set<string>(); // Para evitar notificaciones duplicadas
  
  // Controles de movimiento
  deltaVx = signal(0);
  deltaVy = signal(0);

  // Opciones de velocidad - Ya no se usan, el backend calcula todo
  
  // Estado de validación
  movimientoValido = signal(true);
  mensajeValidacion = signal<string | null>(null);

  // Estado del juego
  juegoTerminado = signal(false);
  resultadoJuego = signal<'ganado' | 'perdido' | null>(null);
  
  // Selección de casillas
  celdaSeleccionada = signal<{x: number, y: number} | null>(null);
  
  // Cache para optimizar rendimiento
  private cacheSeleccionable = new Map<string, boolean>();

  ngOnInit() {
    const participacionId = this.route.snapshot.params['participacionId'];
    
    if (participacionId) {
      this.miParticipacionId.set(participacionId);
      this.loading.set(true);
      
      // Cargar estado inicial
      this.movimientoService.obtenerEstadoActual(participacionId).subscribe({
        next: (movimiento) => {
          console.log('📍 Estado inicial recibido:', movimiento);
          this.estadoActual.set(movimiento);
          
          // Obtener partidaId desde el movimiento
          if (movimiento.partidaId) {
            this.partidaId.set(movimiento.partidaId);
            console.log('🎮 Partida ID:', movimiento.partidaId);
          }
          
          // Cargar mapa y luego destinos posibles
          this.cargarMapaYDestinos(movimiento, participacionId);
        },
        error: (err) => {
          console.error('❌ Error al cargar estado:', err);
          this.error.set('Error al cargar el estado: ' + err.message);
          this.loading.set(false);
        }
      });
    } else {
      // Modo solo visualización de mapa
      const mapaId = this.route.snapshot.params['mapaId'];
      if (mapaId) {
        this.loading.set(true);
        this.mapaService.getMapa(mapaId).subscribe({
          next: (mapa) => {
            this.mapa.set(mapa);
            this.cargarCeldas(mapa.idMapa!);
            this.loading.set(false);
          },
          error: (err) => {
            this.error.set('Error al cargar el mapa: ' + err.message);
            this.loading.set(false);
          }
        });
      }
    }
  }

  ngOnDestroy() {
    if (this.autoRefreshInterval) {
      clearInterval(this.autoRefreshInterval);
    }
  }

  iniciarAutoRefresh() {
    // Limpiar cualquier interval existente
    if (this.autoRefreshInterval) {
      clearInterval(this.autoRefreshInterval);
    }

    // Cargar inmediatamente
    this.cargarEstadoCompleto();

    // Configurar refresh automático
    this.autoRefreshInterval = setInterval(() => {
      this.cargarEstadoCompleto();
    }, this.REFRESH_INTERVAL);
  }

  cargarEstadoCompleto() {
    const partidaId = this.partidaId();
    if (!partidaId) return;

    this.partidaService.obtenerEstadoCompleto(partidaId).subscribe({
      next: (estadoCompleto) => {
        console.log('🔄 Estado completo actualizado:', estadoCompleto);
        
        // Actualizar lista de todos los jugadores
        this.todosLosJugadores.set(estadoCompleto.participaciones || []);
        
        // Actualizar mi estado actual
        const miParticipacion = estadoCompleto.participaciones?.find(
          (p: any) => p.participacionId === this.miParticipacionId()
        );
        
        if (miParticipacion && miParticipacion.ultimoMovimiento) {
          const estadoAnterior = this.estadoActual();
          if (estadoAnterior) {
            this.estadoAnterior.set(estadoAnterior);
          }
          this.estadoActual.set(miParticipacion.ultimoMovimiento);
          this.verificarEstadoJuego();
        }
        
        // Verificar si algún otro jugador ganó o perdió
        this.verificarResultadosOtrosJugadores(estadoCompleto.participaciones || []);
      },
      error: (err) => {
        console.error('Error al cargar estado completo:', err);
      }
    });
  }

  verificarResultadosOtrosJugadores(participaciones: any[]) {
    const miId = this.miParticipacionId();
    
    for (const participacion of participaciones) {
      if (participacion.participacionId === miId) continue; // Saltar mi propia participación
      
      const movimiento = participacion.ultimoMovimiento;
      if (!movimiento) continue;
      
      // Crear ID único para este evento
      const eventoIdGanador = `ganador-${participacion.participacionId}`;
      const eventoIdPerdedor = `perdedor-${participacion.participacionId}`;
      
      // Verificar si ganó
      if (movimiento.llegoAMeta && !this.eventosNotificados.has(eventoIdGanador)) {
        this.mostrarNotificacion(
          `🏆 ${participacion.usuarioNombre} (${participacion.barcoAlias}) ha llegado a la meta!`,
          'ganador'
        );
        this.eventosNotificados.add(eventoIdGanador);
      }
      
      // Verificar si perdió
      if ((movimiento.colision || movimiento.salioDelMapa) && !this.eventosNotificados.has(eventoIdPerdedor)) {
        const razon = movimiento.colision ? 'chocó con una pared' : 'salió del mapa';
        this.mostrarNotificacion(
          `💥 ${participacion.usuarioNombre} (${participacion.barcoAlias}) ha perdido: ${razon}`,
          'perdedor'
        );
        this.eventosNotificados.add(eventoIdPerdedor);
      }
    }
  }

  mostrarNotificacion(mensaje: string, tipo: 'ganador' | 'perdedor' | 'info') {
    const id = this.notificacionIdCounter++;
    const notificacionesActuales = this.notificaciones();
    
    // Agregar nueva notificación
    this.notificaciones.set([...notificacionesActuales, { id, mensaje, tipo }]);
    
    // Auto-eliminar después de 5 segundos
    setTimeout(() => {
      this.eliminarNotificacion(id);
    }, 5000);
  }

  eliminarNotificacion(id: number) {
    const notificacionesActuales = this.notificaciones();
    this.notificaciones.set(notificacionesActuales.filter(n => n.id !== id));
  }

  cargarMapaYDestinos(movimiento: Movimiento, participacionId: number) {
    console.log('🗺️ Cargando mapa y destinos...');
    // Obtener el mapa de la partida (por ahora usamos el primer mapa disponible)
    // TODO: En el futuro, obtener el mapaId desde la partida
    this.mapaService.getMapas().subscribe({
      next: (mapas) => {
        console.log('📦 Mapas disponibles:', mapas.length);
        if (mapas.length > 0) {
          this.mapa.set(mapas[0]);
          console.log('🗺️ Mapa seleccionado:', mapas[0].nombre);
          
          // Cargar celdas del mapa
          this.cargarCeldas(mapas[0].idMapa!);
          
          // Cargar el estado anterior si hay un turno mayor a 0
          if (movimiento.turno && movimiento.turno > 0) {
            this.cargarEstadoAnterior(movimiento.participacionId!, movimiento.turno - 1);
          }
          
          // Cargar destinos posibles
          this.cargarDestinosPosibles(participacionId);
          
          // Verificar estado del juego
          this.verificarEstadoJuego();
          
          // Iniciar auto-refresh solo si tenemos partidaId
          if (this.partidaId()) {
            console.log('🔄 Iniciando auto-refresh...');
            this.iniciarAutoRefresh();
          }
          
          // Marcar como cargado
          this.loading.set(false);
          console.log('✅ Tablero cargado completamente');
        } else {
          this.error.set('No hay mapas disponibles');
          this.loading.set(false);
        }
      },
      error: (err) => {
        console.error('❌ Error al cargar mapas:', err);
        this.error.set('Error al cargar el mapa: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  cargarMapaDesdeParticipacion(movimiento: Movimiento) {
    // Método legacy - redirige al nuevo método
    this.cargarMapaYDestinos(movimiento, movimiento.participacionId!);
  }

  cargarEstadoAnterior(participacionId: number, turnoAnterior: number) {
    // Obtener el historial completo y encontrar el movimiento del turno anterior
    this.movimientoService.obtenerHistorialCompleto(participacionId).subscribe({
      next: (historial) => {
        const movimientoAnterior = historial.find(mov => mov.turno === turnoAnterior);
        if (movimientoAnterior) {
          this.estadoAnterior.set(movimientoAnterior);
          console.log('📜 Estado anterior cargado:', movimientoAnterior);
        } else {
          console.log('📜 No se encontró estado anterior para el turno:', turnoAnterior);
        }
      },
      error: (err) => {
        console.log('⚠️ Error al cargar estado anterior:', err.message);
        // No es crítico si no se puede cargar el estado anterior
      }
    });
  }

  cargarDestinosPosibles(participacionId: number) {
    console.log('🎯 Cargando destinos posibles para participacionId:', participacionId);
    this.movimientoService.obtenerDestinosPosibles(participacionId).subscribe({
      next: (destinos) => {
        this.destinosPosibles.set(destinos);
        // Limpiar cache cuando se cargan nuevos destinos
        this.cacheSeleccionable.clear();
        console.log('🎯 Destinos posibles cargados:', destinos.length, 'opciones');
        console.log('🎯 Destinos detalles:', destinos);
        
        // Debug específico para colisiones inevitables
        const colisionesInev = destinos.filter(d => d.colisionInevitable === true);
        console.log('💥 Colisiones inevitables encontradas:', colisionesInev);
      },
      error: (err) => {
        console.log('⚠️ Error al cargar destinos posibles:', err.message);
        this.destinosPosibles.set([]);
      }
    });
  }

  cargarCeldas(mapaId: number) {
    this.mapaService.getMapaMatriz(mapaId).subscribe({
      next: (data) => {
        // Convertir la matriz en celdas
        const celdas: Celda[] = [];
        const matriz = data.matriz;
        for (let x = 0; x < data.filas; x++) {
          for (let y = 0; y < data.columnas; y++) {
            const tipo = matriz[x][y];
            celdas.push(new Celda({
              coordX: x,
              coordY: y,
              tipo: tipo === ' ' ? CeldaTipo.AGUA : 
                   tipo === 'x' ? CeldaTipo.PARED : 
                   tipo === 'P' ? CeldaTipo.PARTIDA : 
                   tipo === 'M' ? CeldaTipo.META : CeldaTipo.AGUA
            }));
          }
        }
        this.celdas.set(celdas);
      },
      error: (err) => {
        this.error.set('Error al cargar las celdas: ' + err.message);
      }
    });
  }

  getCelda(x: number, y: number): Celda | undefined {
    return this.celdas().find(celda => celda.coordX === x && celda.coordY === y);
  }

  getTipoCelda(x: number, y: number): string {
    const celda = this.getCelda(x, y);
    return celda ? celda.tipo : 'AGUA';
  }

  getColorCelda(tipo: string): string {
    switch (tipo) {
      case 'AGUA': return '#87CEEB'; // Azul cielo
      case 'PARED': return '#8B4513'; // Marrón
      case 'PARTIDA': return '#90EE90'; // Verde claro
      case 'META': return '#FFD700'; // Dorado
      default: return '#CCCCCC'; // Gris
    }
  }

  getSimboloCelda(tipo: string): string {
    switch (tipo) {
      case 'AGUA': return ' ';
      case 'PARED': return 'X';
      case 'PARTIDA': return 'P';
      case 'META': return 'M';
      default: return '?';
    }
  }

  realizarMovimiento() {
    const participacionId = this.route.snapshot.params['participacionId'];
    if (!participacionId) {
      this.error.set('No hay una partida activa. Crea una partida para poder jugar.');
      return;
    }

    // Validar que hay una casilla seleccionada
    if (!this.celdaSeleccionada()) {
      this.error.set('Por favor selecciona una casilla de destino en el mapa');
      return;
    }

    // Validar movimiento antes de enviarlo
    if (!this.movimientoValido()) {
      this.error.set(this.mensajeValidacion() || 'Movimiento inválido');
      return;
    }

    console.log('🚀 Enviando movimiento: deltaVx=', this.deltaVx(), 'deltaVy=', this.deltaVy());
    
    this.loading.set(true);
    this.movimientoService.realizarMovimiento(
      participacionId,
      this.deltaVx(),
      this.deltaVy()
        ).subscribe({
          next: (movimiento) => {
            console.log('Movimiento exitoso:', movimiento);
            // Guardar el estado actual como estado anterior
            const estadoActualAnterior = this.estadoActual();
            if (estadoActualAnterior) {
              this.estadoAnterior.set(estadoActualAnterior);
            }
            // Actualizar el estado actual con el nuevo movimiento
            this.estadoActual.set(movimiento);
            this.loading.set(false);
            this.error.set(null);
            
            // Verificar si el juego ha terminado
            this.verificarEstadoJuego();
            
            // Recargar destinos posibles para el nuevo estado
            this.cargarDestinosPosibles(participacionId);
            // Limpiar selección después del movimiento
            this.limpiarSeleccion();
          },
      error: (err) => {
        console.error('Error en movimiento:', err);
        this.error.set('Error al realizar movimiento: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  getVelocidadActual(): string {
    const estado = this.estadoActual();
    if (!estado) return '(0, 0)';
    return `(${estado.velX}, ${estado.velY})`;
  }

  getPosicionActual(): string {
    const estado = this.estadoActual();
    if (!estado) return '(0, 0)';
    return `(${estado.posX}, ${estado.posY})`;
  }

  getVelocidadAnterior(): string {
    const estado = this.estadoAnterior();
    if (!estado) return 'N/A';
    return `(${estado.velX}, ${estado.velY})`;
  }

  getPosicionAnterior(): string {
    const estado = this.estadoAnterior();
    if (!estado) return 'N/A';
    return `(${estado.posX}, ${estado.posY})`;
  }

  // Verificar si estamos en modo de juego activo
  isModoJuego(): boolean {
    const participacionId = this.route.snapshot.params['participacionId'];
    return !!participacionId;
  }

  // Métodos para selección de casillas
  seleccionarCelda(x: number, y: number) {
    if (!this.esCeldaSeleccionable(x, y)) {
      return;
    }

    console.log('🎯 Seleccionando celda:', x, y);
    this.celdaSeleccionada.set({ x, y });
    this.calcularMovimientoHaciaDestino(x, y);
    this.validarMovimiento();
  }

  limpiarSeleccion() {
    this.celdaSeleccionada.set(null);
    this.deltaVx.set(0);
    this.deltaVy.set(0);
    this.mensajeValidacion.set(null);
  }

  esCeldaSeleccionable(x: number, y: number): boolean {
    const key = `${x},${y}`;
    
    // Verificar cache primero
    if (this.cacheSeleccionable.has(key)) {
      return this.cacheSeleccionable.get(key)!;
    }
    
    // Usar los destinos calculados por el backend para mayor seguridad
    const destinos = this.destinosPosibles();
    
    // Buscar si esta celda está en los destinos posibles
    const destino = destinos.find(d => d.x === x && d.y === y);
    
    let esSeleccionable = false;
    if (destino) {
      esSeleccionable = destino.valido === true;
      // Log temporal para debuggear
      if (destino.colisionInevitable) {
        console.log(`🔍 DEBUG - Celda (${x},${y}) con colisión inevitable, valido: ${destino.valido}`);
      }
    }
    
    // Guardar en cache
    this.cacheSeleccionable.set(key, esSeleccionable);
    
    return esSeleccionable;
  }

  esColisionInevitableEn(x: number, y: number): boolean {
    const destinos = this.destinosPosibles();
    const destino = destinos.find(d => d.x === x && d.y === y);
    const esColision = destino ? destino.colisionInevitable : false;
    
    // Log temporal para debuggear
    if (destino && destino.colisionInevitable) {
      console.log(`💥 DEBUG - Celda (${x},${y}) ES colisión inevitable:`, destino);
    }
    
    return esColision;
  }

  verificarEstadoJuego() {
    const estado = this.estadoActual();
    if (!estado) return;

    console.log('🎮 DEBUG - Verificando estado del juego:', {
      llegoAMeta: estado.llegoAMeta,
      colision: estado.colision,
      salioDelMapa: estado.salioDelMapa
    });

    // Verificar si llegó a la meta
    if (estado.llegoAMeta && !this.juegoTerminado()) {
      console.log('🏆 DEBUG - Juego ganado!');
      this.juegoTerminado.set(true);
      this.resultadoJuego.set('ganado');
      this.mostrarNotificacion('🏆 ¡Felicidades! Has llegado a la meta', 'ganador');
      return;
    }

    // Verificar si hubo colisión o salió del mapa
    if ((estado.colision || estado.salioDelMapa) && !this.juegoTerminado()) {
      console.log('💥 DEBUG - Juego perdido!', { colision: estado.colision, salioDelMapa: estado.salioDelMapa });
      this.juegoTerminado.set(true);
      this.resultadoJuego.set('perdido');
      const razon = estado.colision ? 'Has chocado con una pared' : 'Has salido del mapa';
      this.mostrarNotificacion(`💥 ¡Perdiste! ${razon}`, 'perdedor');
      return;
    }

    // El juego continúa
    if (!estado.llegoAMeta && !estado.colision && !estado.salioDelMapa) {
      console.log('🎮 DEBUG - Juego continúa...');
      this.juegoTerminado.set(false);
      this.resultadoJuego.set(null);
    }
  }

  reiniciarJuego() {
    // Navegar de vuelta al selector de mapas
    this.router.navigate(['/game']);
  }

  calcularMovimientoHaciaDestino(destinoX: number, destinoY: number) {
    // Buscar el destino en la lista de destinos posibles del backend
    const destinos = this.destinosPosibles();
    const destino = destinos.find(d => d.x === destinoX && d.y === destinoY);
    
    if (destino) {
      console.log('🎯 Destino encontrado en backend:', destino);
      this.deltaVx.set(destino.deltaVx);
      this.deltaVy.set(destino.deltaVy);
    } else {
      console.log('❌ Destino no válido según el backend');
      this.deltaVx.set(0);
      this.deltaVy.set(0);
    }
  }

  getDestinoPreview(): string {
    const seleccionada = this.celdaSeleccionada();
    if (!seleccionada) {
      return "Sin destino seleccionado";
    }

    // Buscar el destino en la lista de destinos posibles del backend
    const destinos = this.destinosPosibles();
    const destino = destinos.find(d => d.x === seleccionada.x && d.y === seleccionada.y);
    
    if (destino) {
      const estado = this.estadoActual();
      if (!estado) {
        return `Destino: (${seleccionada.x}, ${seleccionada.y})`;
      }
      
      // Verificar si es una colisión inevitable
      if (destino.colisionInevitable) {
        return `💥 COLISIÓN INEVITABLE - Chocarás con una pared en (${destino.x}, ${destino.y}) - La velocidad actual (${estado.velX},${estado.velY}) te llevará directamente a una pared`;
      }
      
      let direccion = "";
      if (destino.deltaVy < 0) direccion += "Arriba";
      if (destino.deltaVy > 0) direccion += "Abajo";
      if (destino.deltaVx < 0) direccion += (direccion ? "-" : "") + "Izquierda";
      if (destino.deltaVx > 0) direccion += (direccion ? "-" : "") + "Derecha";
      
      let mensaje = `✅ Llegarás exactamente a (${destino.x}, ${destino.y})`;
      mensaje += ` - Velocidad: (${estado.velX},${estado.velY}) → (${destino.nuevaVelX},${destino.nuevaVelY})`;
      mensaje += ` - Cambio: ${direccion || "Mantener"}`;
      
      if (destino.esMeta) {
        mensaje += " 🏁 ¡META!";
      }
      
      return mensaje;
    } else {
      return "❌ Destino no válido según las reglas del juego";
    }
  }

  getTooltipCelda(x: number, y: number, tipo: string): string {
    const estado = this.estadoActual();
    if (!estado) {
      return `(${x}, ${y}) - ${tipo}`;
    }

    if (estado.posX === x && estado.posY === y) {
      return `Tu barco está aquí - (${x}, ${y})`;
    }

    // Verificar si es una colisión inevitable
    const destinos = this.destinosPosibles();
    const destino = destinos.find(d => d.x === x && d.y === y);
    
    if (destino && destino.colisionInevitable) {
      return `💥 COLISIÓN INEVITABLE - (${x}, ${y}) - Chocarás aquí con la velocidad actual`;
    }

    if (tipo === 'PARED') {
      return `Pared - (${x}, ${y}) - No navegable`;
    }

    if (this.esCeldaSeleccionable(x, y)) {
      return `Destino posible - (${x}, ${y}) - Clic para navegar aquí`;
    }

    return `(${x}, ${y}) - ${tipo}`;
  }

  validarMovimiento() {
    // La validación ahora se hace completamente en el backend
    // Solo validamos que hay un destino seleccionado
    const seleccionada = this.celdaSeleccionada();
    if (!seleccionada) {
      this.movimientoValido.set(false);
      this.mensajeValidacion.set("⚠️ Selecciona un destino primero");
      return;
    }

    // Si hay un destino seleccionado y está en la lista de destinos válidos del backend, es válido
    const destinos = this.destinosPosibles();
    const destinoValido = destinos.some(d => d.x === seleccionada.x && d.y === seleccionada.y);
    
    if (destinoValido) {
      this.movimientoValido.set(true);
      this.mensajeValidacion.set(null);
    } else {
      this.movimientoValido.set(false);
      this.mensajeValidacion.set("⚠️ Destino no válido según las reglas del juego");
    }
  }

  // Método para obtener jugadores en una celda específica (excepto yo)
  obtenerJugadoresEnCelda(x: number, y: number): any[] {
    const jugadores = this.todosLosJugadores();
    const miId = this.miParticipacionId();
    
    // Debug: mostrar todos los jugadores una sola vez (cuando x=0 y y=0)
    if (x === 0 && y === 0 && jugadores.length > 0) {
      console.log('👥 Total jugadores:', jugadores.length);
      console.log('👤 Mi participación ID:', miId);
      jugadores.forEach((j, idx) => {
        console.log(`  Jugador ${idx + 1}:`, {
          participacionId: j.participacionId,
          usuario: j.usuarioNombre,
          barco: j.barcoAlias,
          color: j.barcoColor,
          tieneMovimiento: !!j.ultimoMovimiento,
          posicion: j.ultimoMovimiento ? `(${j.ultimoMovimiento.posX}, ${j.ultimoMovimiento.posY})` : 'Sin movimiento'
        });
      });
    }
    
    return jugadores.filter(jugador => {
      // Filtrar mi propio barco
      if (jugador.participacionId === miId) return false;
      
      // Verificar si el jugador tiene un movimiento actual
      const movimiento = jugador.ultimoMovimiento;
      if (!movimiento) {
        return false;
      }
      
      // Verificar si está en esta celda
      const estaAqui = movimiento.posX === x && movimiento.posY === y;
      if (estaAqui) {
        console.log(`⛵ Barco encontrado en (${x},${y}):`, jugador.barcoAlias);
      }
      return estaAqui;
    });
  }
}
