import { PartidaEstado } from '../enums/partida-estado';

export interface PartidaInterface {
  idPartida?: number;
  fechaInicio?: Date;
  fechaFin?: Date;
  estado: PartidaEstado;
  mapaId: number;
  mapaNombre?: string;
  ganadorUsuarioId?: number;
  ganadorUsuarioNombre?: string;
  ganadorBarcoId?: number;
  ganadorBarcoAlias?: string;
  totalParticipantes?: number;
  duracionMinutos?: number;
}

export interface CreatePartidaRequest {
  mapaId: number;
  estado: PartidaEstado;
}

export interface UpdatePartidaRequest {
  idPartida: number;
  estado?: PartidaEstado;
  fechaFin?: Date;
  ganadorUsuarioId?: number;
  ganadorBarcoId?: number;
}
