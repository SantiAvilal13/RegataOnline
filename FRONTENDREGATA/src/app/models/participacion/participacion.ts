import { ParticipacionEstado } from '../enums/participacion-estado';
import { Movimiento } from '../movimiento/movimiento';

export class Participacion {
  idParticipacion?: number;
  turnoActual?: number;
  posX: number = 0;
  posY: number = 0;
  velX?: number;
  velY?: number;
  estado: ParticipacionEstado = ParticipacionEstado.ACTIVO;
  partidaId: number = 0;
  partidaNombre?: string;
  barcoId: number = 0;
  barcoAlias?: string;
  jugadorId: number = 0;
  jugadorNombre?: string;
  
  // Campos adicionales para la vista
  movimientos?: Movimiento[];
  totalMovimientos?: number;
  duracionTurnos?: number;
  esGanador?: boolean;

  constructor(init?: Partial<Participacion>) {
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad
  esActiva(): boolean {
    return ParticipacionEstado.isActive(this.estado);
  }

  estaDestruida(): boolean {
    return ParticipacionEstado.isDestroyed(this.estado);
  }

  llegoAMeta(): boolean {
    return ParticipacionEstado.isAtFinish(this.estado);
  }

  getEstadoDisplayName(): string {
    return ParticipacionEstado.getDisplayName(this.estado);
  }

  getPosicion(): string {
    return `(${this.posX},${this.posY})`;
  }

  getVelocidad(): string {
    return `(${this.velX || 0},${this.velY || 0})`;
  }

  getPartidaNombre(): string {
    return this.partidaNombre || 'Sin partida';
  }

  getBarcoAlias(): string {
    return this.barcoAlias || 'Sin barco';
  }

  getJugadorNombre(): string {
    return this.jugadorNombre || 'Sin jugador';
  }

  getTotalMovimientos(): number {
    return this.totalMovimientos || 0;
  }

  getDuracionTurnos(): number {
    return this.duracionTurnos || 0;
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.posX !== undefined && this.posY !== undefined && this.estado && this.partidaId && this.barcoId && this.jugadorId);
  }

  // Método para clonar
  clone(): Participacion {
    return new Participacion({
      idParticipacion: this.idParticipacion,
      turnoActual: this.turnoActual,
      posX: this.posX,
      posY: this.posY,
      velX: this.velX,
      velY: this.velY,
      estado: this.estado,
      partidaId: this.partidaId,
      partidaNombre: this.partidaNombre,
      barcoId: this.barcoId,
      barcoAlias: this.barcoAlias,
      jugadorId: this.jugadorId,
      jugadorNombre: this.jugadorNombre,
      movimientos: this.movimientos,
      totalMovimientos: this.totalMovimientos,
      duracionTurnos: this.duracionTurnos,
      esGanador: this.esGanador
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Participacion {
    return new Participacion({
      idParticipacion: dto.idParticipacion,
      turnoActual: dto.turnoActual,
      posX: dto.posX,
      posY: dto.posY,
      velX: dto.velX,
      velY: dto.velY,
      estado: dto.estado,
      partidaId: dto.partidaId,
      partidaNombre: dto.partidaNombre,
      barcoId: dto.barcoId,
      barcoAlias: dto.barcoAlias,
      jugadorId: dto.jugadorId,
      jugadorNombre: dto.jugadorNombre,
      movimientos: dto.movimientos ? dto.movimientos.map((m: any) => Movimiento.fromDTO(m)) : undefined,
      totalMovimientos: dto.totalMovimientos,
      duracionTurnos: dto.duracionTurnos,
      esGanador: dto.esGanador
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idParticipacion: this.idParticipacion,
      turnoActual: this.turnoActual,
      posX: this.posX,
      posY: this.posY,
      velX: this.velX,
      velY: this.velY,
      estado: this.estado,
      partidaId: this.partidaId,
      barcoId: this.barcoId,
      jugadorId: this.jugadorId
    };
  }

  toString(): string {
    return `Participacion{idParticipacion=${this.idParticipacion}, turnoActual=${this.turnoActual}, posX=${this.posX}, posY=${this.posY}, velX=${this.velX}, velY=${this.velY}, estado=${this.estado}, jugadorNombre='${this.jugadorNombre}', barcoAlias='${this.barcoAlias}'}`;
  }
}