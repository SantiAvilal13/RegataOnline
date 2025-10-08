import { PartidaEstado } from '../enums/partida-estado';
import { Participacion } from '../participacion/participacion';

export class Partida {
  idPartida?: number;
  fechaInicio?: Date;
  fechaFin?: Date;
  estado: PartidaEstado = PartidaEstado.ESPERANDO;
  mapaId: number = 0;
  mapaNombre?: string;
  ganadorUsuarioId?: number;
  ganadorUsuarioNombre?: string;
  ganadorBarcoId?: number;
  ganadorBarcoAlias?: string;
  
  // Campos adicionales para la vista
  participaciones?: Participacion[];
  totalParticipantes?: number;
  duracionMinutos?: number;

  constructor(init?: Partial<Partida>) {
    if (init) {
      Object.assign(this, init);
      // Convertir fechas si vienen como strings
      if (init.fechaInicio && typeof init.fechaInicio === 'string') {
        this.fechaInicio = new Date(init.fechaInicio);
      }
      if (init.fechaFin && typeof init.fechaFin === 'string') {
        this.fechaFin = new Date(init.fechaFin);
      }
    }
  }

  // Métodos de utilidad
  esActiva(): boolean {
    return PartidaEstado.isActive(this.estado);
  }

  estaTerminada(): boolean {
    return PartidaEstado.isFinished(this.estado);
  }

  estaEsperando(): boolean {
    return PartidaEstado.isWaiting(this.estado);
  }

  getEstadoDisplayName(): string {
    return PartidaEstado.getDisplayName(this.estado);
  }

  getMapaNombre(): string {
    return this.mapaNombre || 'Sin mapa';
  }

  getTotalParticipantes(): number {
    return this.totalParticipantes || 0;
  }

  getDuracionMinutos(): number {
    return this.duracionMinutos || 0;
  }

  getGanadorInfo(): string {
    if (this.ganadorUsuarioNombre && this.ganadorBarcoAlias) {
      return `${this.ganadorUsuarioNombre} - ${this.ganadorBarcoAlias}`;
    }
    return 'Sin ganador';
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.mapaId && this.estado);
  }

  // Método para clonar
  clone(): Partida {
    return new Partida({
      idPartida: this.idPartida,
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin,
      estado: this.estado,
      mapaId: this.mapaId,
      mapaNombre: this.mapaNombre,
      ganadorUsuarioId: this.ganadorUsuarioId,
      ganadorUsuarioNombre: this.ganadorUsuarioNombre,
      ganadorBarcoId: this.ganadorBarcoId,
      ganadorBarcoAlias: this.ganadorBarcoAlias,
      participaciones: this.participaciones,
      totalParticipantes: this.totalParticipantes,
      duracionMinutos: this.duracionMinutos
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Partida {
    return new Partida({
      idPartida: dto.idPartida,
      fechaInicio: dto.fechaInicio,
      fechaFin: dto.fechaFin,
      estado: dto.estado,
      mapaId: dto.mapaId,
      mapaNombre: dto.mapaNombre,
      ganadorUsuarioId: dto.ganadorUsuarioId,
      ganadorUsuarioNombre: dto.ganadorUsuarioNombre,
      ganadorBarcoId: dto.ganadorBarcoId,
      ganadorBarcoAlias: dto.ganadorBarcoAlias,
      participaciones: dto.participaciones ? dto.participaciones.map((p: any) => Participacion.fromDTO(p)) : undefined,
      totalParticipantes: dto.totalParticipantes,
      duracionMinutos: dto.duracionMinutos
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idPartida: this.idPartida,
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin,
      estado: this.estado,
      mapaId: this.mapaId
    };
  }

  toString(): string {
    return `Partida{idPartida=${this.idPartida}, fechaInicio=${this.fechaInicio}, fechaFin=${this.fechaFin}, estado=${this.estado}, mapaNombre='${this.mapaNombre}'}`;
  }
}