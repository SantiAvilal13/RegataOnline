import { DeltaVelocidad } from '../enums/delta-velocidad';

export interface MovimientoInterface {
  idMov?: number;
  turno: number;
  posX: number;
  posY: number;
  velX: number;
  velY: number;
  deltaVx: DeltaVelocidad;
  deltaVy: DeltaVelocidad;
  colision?: boolean;
  participacionId: number;
  participacionInfo?: string;
  celdaId?: number;
  celdaInfo?: string;
}

export interface CreateMovimientoRequest {
  turno: number;
  posX: number;
  posY: number;
  velX: number;
  velY: number;
  deltaVx: DeltaVelocidad;
  deltaVy: DeltaVelocidad;
  participacionId: number;
}

export interface UpdateMovimientoRequest {
  idMov: number;
  turno?: number;
  posX?: number;
  posY?: number;
  velX?: number;
  velY?: number;
  deltaVx?: DeltaVelocidad;
  deltaVy?: DeltaVelocidad;
  colision?: boolean;
}
