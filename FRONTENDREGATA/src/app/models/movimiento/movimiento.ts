import { DeltaVelocidad } from '../enums/delta-velocidad';

export class Movimiento {
  idMov?: number;
  turno: number = 0;
  posX: number = 0;
  posY: number = 0;
  velX: number = 0;
  velY: number = 0;
  deltaVx: DeltaVelocidad = DeltaVelocidad.MANTENER;
  deltaVy: DeltaVelocidad = DeltaVelocidad.MANTENER;
  colision?: boolean;
  llegoAMeta?: boolean;
  salioDelMapa?: boolean;
  participacionId: number = 0;
  participacionInfo?: string;
  celdaId?: number;
  celdaInfo?: string;

  constructor(init?: Partial<Movimiento>) {
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad
  tieneColision(): boolean {
    return Boolean(this.colision);
  }

  getPosicion(): string {
    return `(${this.posX},${this.posY})`;
  }

  getVelocidad(): string {
    return `(${this.velX},${this.velY})`;
  }

  getDeltaVelocidad(): string {
    return `(${DeltaVelocidad.getValue(this.deltaVx)},${DeltaVelocidad.getValue(this.deltaVy)})`;
  }

  getDeltaVxDisplayName(): string {
    return DeltaVelocidad.getDisplayName(this.deltaVx);
  }

  getDeltaVyDisplayName(): string {
    return DeltaVelocidad.getDisplayName(this.deltaVy);
  }

  getDeltaVxSymbol(): string {
    return DeltaVelocidad.getSymbol(this.deltaVx);
  }

  getDeltaVySymbol(): string {
    return DeltaVelocidad.getSymbol(this.deltaVy);
  }

  getParticipacionInfo(): string {
    return this.participacionInfo || 'Sin participación';
  }

  getCeldaInfo(): string {
    return this.celdaInfo || 'Sin celda';
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.turno !== undefined && this.posX !== undefined && this.posY !== undefined && 
              this.velX !== undefined && this.velY !== undefined && this.deltaVx && this.deltaVy && this.participacionId);
  }

  hasValidTurn(): boolean {
    return this.turno >= 0;
  }

  hasValidPosition(): boolean {
    return this.posX >= 0 && this.posY >= 0;
  }

  // Método para clonar
  clone(): Movimiento {
    return new Movimiento({
      idMov: this.idMov,
      turno: this.turno,
      posX: this.posX,
      posY: this.posY,
      velX: this.velX,
      velY: this.velY,
      deltaVx: this.deltaVx,
      deltaVy: this.deltaVy,
      colision: this.colision,
      llegoAMeta: this.llegoAMeta,
      salioDelMapa: this.salioDelMapa,
      participacionId: this.participacionId,
      participacionInfo: this.participacionInfo,
      celdaId: this.celdaId,
      celdaInfo: this.celdaInfo
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Movimiento {
    return new Movimiento({
      idMov: dto.idMov,
      turno: dto.turno,
      posX: dto.posX,
      posY: dto.posY,
      velX: dto.velX,
      velY: dto.velY,
      deltaVx: dto.deltaVx,
      deltaVy: dto.deltaVy,
      colision: dto.colision,
      llegoAMeta: dto.llegoAMeta,
      salioDelMapa: dto.salioDelMapa,
      participacionId: dto.participacionId,
      participacionInfo: dto.participacionInfo,
      celdaId: dto.celdaId,
      celdaInfo: dto.celdaInfo
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idMov: this.idMov,
      turno: this.turno,
      posX: this.posX,
      posY: this.posY,
      velX: this.velX,
      velY: this.velY,
      deltaVx: this.deltaVx,
      deltaVy: this.deltaVy,
      colision: this.colision,
      llegoAMeta: this.llegoAMeta,
      salioDelMapa: this.salioDelMapa,
      participacionId: this.participacionId,
      celdaId: this.celdaId
    };
  }

  toString(): string {
    return `Movimiento{idMov=${this.idMov}, turno=${this.turno}, posX=${this.posX}, posY=${this.posY}, velX=${this.velX}, velY=${this.velY}, deltaVx=${this.deltaVx}, deltaVy=${this.deltaVy}, colision=${this.colision}}`;
  }
}