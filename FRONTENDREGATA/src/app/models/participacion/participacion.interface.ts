import { ParticipacionEstado } from '../enums/participacion-estado';

export interface ParticipacionInterface {
  idParticipacion?: number;
  turnoActual?: number;
  posX: number;
  posY: number;
  velX?: number;
  velY?: number;
  estado: ParticipacionEstado;
  partidaId: number;
  partidaNombre?: string;
  barcoId: number;
  barcoAlias?: string;
  jugadorId: number;
  jugadorNombre?: string;
  totalMovimientos?: number;
  duracionTurnos?: number;
  esGanador?: boolean;
}

export interface CreateParticipacionRequest {
  partidaId: number;
  barcoId: number;
  jugadorId: number;
  posX: number;
  posY: number;
}

export interface UpdateParticipacionRequest {
  idParticipacion: number;
  posX?: number;
  posY?: number;
  velX?: number;
  velY?: number;
  estado?: ParticipacionEstado;
}
