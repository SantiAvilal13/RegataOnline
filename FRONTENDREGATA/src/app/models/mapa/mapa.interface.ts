import { CeldaInterface } from '../celda/celda.interface';

export interface MapaInterface {
  idMapa?: number;
  nombre: string;
  tamFilas?: number;
  tamColumnas?: number;
  celdas?: CeldaInterface[];
}

export interface CreateMapaRequest {
  nombre: string;
  tamFilas: number;
  tamColumnas: number;
}

export interface UpdateMapaRequest {
  idMapa: number;
  nombre?: string;
  tamFilas?: number;
  tamColumnas?: number;
}
