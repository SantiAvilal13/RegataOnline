import { CeldaTipo } from '../enums/celda-tipo';

export interface CeldaInterface {
  idCelda?: number;
  coordX: number;
  coordY: number;
  tipo: CeldaTipo;
  mapaId: number;
  mapaNombre?: string;
  totalMovimientos?: number;
  colorDisplay?: string;
}

export interface CreateCeldaRequest {
  coordX: number;
  coordY: number;
  tipo: CeldaTipo;
  mapaId: number;
}

export interface UpdateCeldaRequest {
  idCelda: number;
  coordX?: number;
  coordY?: number;
  tipo?: CeldaTipo;
}
