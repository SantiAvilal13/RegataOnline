import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap, map } from 'rxjs';
import { MapaJuegoService } from '../../../shared/services/juego/mapa-juego.service';
import { MovimientoJuegoService } from '../../../shared/services/juego/movimiento-juego.service';
import { Mapa, Celda, Movimiento } from '../../../models';
import { CeldaTipo } from '../../../models/enums/celda-tipo';

@Component({
  selector: 'app-game-board',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.css'
})
export class GameBoardComponent implements OnInit {
  mapaService = inject(MapaJuegoService);
  movimientoService = inject(MovimientoJuegoService);
  route = inject(ActivatedRoute);

      // Estados del juego
      mapa = signal<Mapa | null>(null);
      celdas = signal<Celda[]>([]);
      estadoActual = signal<Movimiento | null>(null);
      estadoAnterior = signal<Movimiento | null>(null);
      loading = signal(false);
      error = signal<string | null>(null);
  
  // Controles de movimiento
  deltaVx = signal(0);
  deltaVy = signal(0);

  // Opciones de velocidad
  velocidades = [-1, 0, 1];
  
  // Estado de validación
  movimientoValido = signal(true);
  mensajeValidacion = signal<string | null>(null);
  
  // Selección de casillas
  celdaSeleccionada = signal<{x: number, y: number} | null>(null);

  ngOnInit() {
    this.route.params.pipe(
      switchMap(params => {
        const mapaId = params['mapaId'];
        const participacionId = params['participacionId'];
        
        if (mapaId) {
          this.loading.set(true);
          console.log('Cargando mapa con ID:', mapaId);
          return this.mapaService.getMapa(mapaId);
        } else if (participacionId) {
          // Si tenemos participacionId, cargar el estado actual primero
          this.loading.set(true);
          return this.movimientoService.obtenerEstadoActual(participacionId);
        } else {
          // Cargar el primer mapa disponible por defecto
          this.loading.set(true);
          return this.mapaService.getMapas().pipe(
            map(mapas => mapas.length > 0 ? mapas[0] : null)
          );
        }
      })
    ).subscribe({
      next: (data) => {
        console.log('Datos recibidos:', data);
        if (data instanceof Mapa) {
          this.mapa.set(data);
          this.cargarCeldas(data.idMapa!);
        } else if (data instanceof Movimiento) {
          // Si recibimos un movimiento, significa que tenemos una participación
          this.estadoActual.set(data);
          // Necesitamos obtener el mapa de la partida
          this.cargarMapaDesdeParticipacion(data);
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar los datos: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  cargarMapaDesdeParticipacion(movimiento: Movimiento) {
    // Aquí necesitaríamos obtener el mapa desde la partida
    // Por ahora, cargaremos el primer mapa disponible
    this.mapaService.getMapas().subscribe({
      next: (mapas) => {
        if (mapas.length > 0) {
          this.mapa.set(mapas[0]);
          this.cargarCeldas(mapas[0].idMapa!);
          // Cargar el estado anterior si hay un turno mayor a 0
          if (movimiento.turno && movimiento.turno > 0) {
            this.cargarEstadoAnterior(movimiento.participacionId!, movimiento.turno - 1);
          }
        }
      },
      error: (err) => {
        this.error.set('Error al cargar el mapa: ' + err.message);
      }
    });
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
    const estado = this.estadoActual();
    if (!estado || !this.mapa()) {
      return false;
    }

    // No se puede seleccionar la posición actual
    if (estado.posX === x && estado.posY === y) {
      return false;
    }

    // No se puede seleccionar paredes
    const celda = this.getCelda(x, y);
    if (celda && celda.tipo === 'PARED') {
      return false;
    }

    // Verificar que esté dentro de los límites del mapa
    if (x < 0 || x >= this.mapa()!.getTamFilas() || y < 0 || y >= this.mapa()!.getTamColumnas()) {
      return false;
    }

    // Calcular el delta necesario para llegar al destino
    const deltaX = x - estado.posX - estado.velX;
    const deltaY = y - estado.posY - estado.velY;
    
    // Si el delta es 0,0, no es un movimiento válido (no hay cambio de velocidad)
    if (deltaX === 0 && deltaY === 0) {
      return false;
    }

    // Limitar delta a -1, 0, o 1 según las reglas del juego
    const deltaVx = Math.max(-1, Math.min(1, deltaX));
    const deltaVy = Math.max(-1, Math.min(1, deltaY));

    // Calcular la nueva velocidad (puede ser cualquier valor, no limitado a [-1,1])
    const nuevaVelX = estado.velX + deltaVx;
    const nuevaVelY = estado.velY + deltaVy;

    // NO limitar la velocidad final - solo el delta por turno está limitado a [-1,0,1]

    // Calcular la posición final con la nueva velocidad
    const nuevaPosX = estado.posX + nuevaVelX;
    const nuevaPosY = estado.posY + nuevaVelY;

    // Validar si la posición final está fuera del mapa
    if (nuevaPosX < 0 || nuevaPosX >= this.mapa()!.getTamFilas() ||
        nuevaPosY < 0 || nuevaPosY >= this.mapa()!.getTamColumnas()) {
      return false;
    }

    // Validar si la posición final es una pared
    const celdaFinal = this.getCelda(nuevaPosX, nuevaPosY);
    if (celdaFinal && celdaFinal.tipo === 'PARED') {
      return false;
    }

    return true;
  }

  calcularMovimientoHaciaDestino(destinoX: number, destinoY: number) {
    const estado = this.estadoActual();
    if (!estado) {
      return;
    }

    // Según las reglas del juego:
    // Nueva posición = Posición actual + Nueva velocidad
    // Nueva velocidad = Velocidad actual + Delta velocidad (limitado a -1,0,1)
    // Delta velocidad = (Destino - Posición actual - Velocidad actual)
    
    const deltaX = destinoX - estado.posX - estado.velX;
    const deltaY = destinoY - estado.posY - estado.velY;

    console.log('🧮 Cálculo movimiento:');
    console.log('  Estado actual: Pos(', estado.posX, ',', estado.posY, ') Vel(', estado.velX, ',', estado.velY, ')');
    console.log('  Destino: (', destinoX, ',', destinoY, ')');
    console.log('  Delta necesario: (', deltaX, ',', deltaY, ')');

    // Limitar delta a -1, 0, o 1 según las reglas del juego
    const deltaVx = Math.max(-1, Math.min(1, deltaX));
    const deltaVy = Math.max(-1, Math.min(1, deltaY));
    
    console.log('  Delta limitado: (', deltaVx, ',', deltaVy, ')');
    
    // Calcular posición final con el delta limitado
    const nuevaVelX = estado.velX + deltaVx;
    const nuevaVelY = estado.velY + deltaVy;
    const posFinalX = estado.posX + nuevaVelX;
    const posFinalY = estado.posY + nuevaVelY;
    
    console.log('  ✅ Nueva velocidad: (', nuevaVelX, ',', nuevaVelY, ') - ¡PUEDE SER MAYOR QUE 1!');
    console.log('  🎯 Posición final: (', posFinalX, ',', posFinalY, ')');
    console.log('  📏 Diferencia con destino: (', destinoX - posFinalX, ',', destinoY - posFinalY, ')');
    
    this.deltaVx.set(deltaVx);
    this.deltaVy.set(deltaVy);
  }

  getDestinoPreview(): string {
    const seleccionada = this.celdaSeleccionada();
    if (!seleccionada) {
      return "Sin destino seleccionado";
    }

    const estado = this.estadoActual();
    if (!estado) {
      return `Destino: (${seleccionada.x}, ${seleccionada.y})`;
    }

    // Calcular delta según las reglas correctas
    const deltaX = seleccionada.x - estado.posX - estado.velX;
    const deltaY = seleccionada.y - estado.posY - estado.velY;

    // Limitar delta a -1, 0, o 1 según las reglas del juego
    const deltaVx = Math.max(-1, Math.min(1, deltaX));
    const deltaVy = Math.max(-1, Math.min(1, deltaY));

    // Calcular posición final
    const nuevaVelX = estado.velX + deltaVx;
    const nuevaVelY = estado.velY + deltaVy;
    const posFinalX = estado.posX + nuevaVelX;
    const posFinalY = estado.posY + nuevaVelY;

    let direccion = "";
    if (deltaVy < 0) direccion += "Arriba";
    if (deltaVy > 0) direccion += "Abajo";
    if (deltaVx < 0) direccion += (direccion ? "-" : "") + "Izquierda";
    if (deltaVx > 0) direccion += (direccion ? "-" : "") + "Derecha";

    // Mostrar información detallada
    const diferenciaX = seleccionada.x - posFinalX;
    const diferenciaY = seleccionada.y - posFinalY;
    
    if (diferenciaX === 0 && diferenciaY === 0) {
      return `✅ Llegarás exactamente a (${seleccionada.x}, ${seleccionada.y}) - Velocidad: (${estado.velX},${estado.velY}) → (${nuevaVelX},${nuevaVelY})`;
    } else {
      return `🎯 Te acercarás a (${seleccionada.x}, ${seleccionada.y}) - Llegarás a (${posFinalX}, ${posFinalY}) - Velocidad: (${estado.velX},${estado.velY}) → (${nuevaVelX},${nuevaVelY}) - Quedarán (${diferenciaX}, ${diferenciaY}) casillas`;
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

    if (tipo === 'PARED') {
      return `Pared - (${x}, ${y}) - No navegable`;
    }

    if (this.esCeldaSeleccionable(x, y)) {
      // Calcular delta según las reglas correctas
      const deltaX = x - estado.posX - estado.velX;
      const deltaY = y - estado.posY - estado.velY;
      
      return `Destino posible - (${x}, ${y}) - Clic para navegar aquí`;
    }

    return `(${x}, ${y}) - ${tipo}`;
  }

  validarMovimiento() {
    const estado = this.estadoActual();
    if (!estado || !this.mapa()) {
      this.movimientoValido.set(true);
      this.mensajeValidacion.set(null);
      return;
    }

    // Según las reglas del juego:
    // Nueva velocidad = Velocidad actual + Delta velocidad
    // Nueva posición = Posición actual + Nueva velocidad
    const nuevaVelX = estado.velX + this.deltaVx();
    const nuevaVelY = estado.velY + this.deltaVy();
    const nuevaPosX = estado.posX + nuevaVelX;
    const nuevaPosY = estado.posY + nuevaVelY;
    
    // Validar límites del mapa
    if (nuevaPosX < 0 || nuevaPosX >= this.mapa()!.getTamFilas() || 
        nuevaPosY < 0 || nuevaPosY >= this.mapa()!.getTamColumnas()) {
      this.movimientoValido.set(false);
      this.mensajeValidacion.set("⚠️ Te saldrías del mapa con este movimiento");
      return;
    }

    // Validar si la nueva posición es una pared
    const nuevaCelda = this.getCelda(nuevaPosX, nuevaPosY);
    if (nuevaCelda && nuevaCelda.tipo === 'PARED') {
      this.movimientoValido.set(false);
      this.mensajeValidacion.set("⚠️ Chocarías con una pared");
      return;
    }

    // Movimiento válido
    this.movimientoValido.set(true);
    this.mensajeValidacion.set(null);
  }
}
