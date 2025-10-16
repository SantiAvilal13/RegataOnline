import { CeldaTipo } from '../enums/celda-tipo';
import { Movimiento } from '../movimiento/movimiento';

export class Celda {
  idCelda?: number;
  coordX: number = 0;
  coordY: number = 0;
  tipo: CeldaTipo = CeldaTipo.AGUA;
  mapaId: number = 0;
  mapaNombre?: string;
  
  // Campos adicionales para la vista
  movimientos?: Movimiento[];
  totalMovimientos?: number;
  colorDisplay?: string;

  constructor(init?: Partial<Celda>) {
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad
  esAgua(): boolean {
    return CeldaTipo.isWater(this.tipo);
  }

  esPared(): boolean {
    return CeldaTipo.isWall(this.tipo);
  }

  esPartida(): boolean {
    return CeldaTipo.isStart(this.tipo);
  }

  esMeta(): boolean {
    return CeldaTipo.isFinish(this.tipo);
  }

  getTipoDisplayName(): string {
    return CeldaTipo.getDisplayName(this.tipo);
  }

  getColor(): string {
    return CeldaTipo.getColor(this.tipo);
  }

  getCoordenadas(): string {
    return `(${this.coordX},${this.coordY})`;
  }

  getMapaNombre(): string {
    return this.mapaNombre || 'Sin mapa';
  }

  getTotalMovimientos(): number {
    return this.totalMovimientos || 0;
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.coordX !== undefined && this.coordY !== undefined && this.tipo && this.mapaId);
  }

  hasValidCoordinates(): boolean {
    return !!(this.coordX >= 0 && this.coordY >= 0);
  }

  // Método para clonar
  clone(): Celda {
    return new Celda({
      idCelda: this.idCelda,
      coordX: this.coordX,
      coordY: this.coordY,
      tipo: this.tipo,
      mapaId: this.mapaId,
      mapaNombre: this.mapaNombre,
      movimientos: this.movimientos,
      totalMovimientos: this.totalMovimientos,
      colorDisplay: this.colorDisplay
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Celda {
    return new Celda({
      idCelda: dto.idCelda,
      coordX: dto.coordX,
      coordY: dto.coordY,
      tipo: dto.tipo,
      mapaId: dto.mapaId,
      mapaNombre: dto.mapaNombre,
      movimientos: dto.movimientos ? dto.movimientos.map((m: any) => Movimiento.fromDTO(m)) : undefined,
      totalMovimientos: dto.totalMovimientos,
      colorDisplay: dto.colorDisplay
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idCelda: this.idCelda,
      coordX: this.coordX,
      coordY: this.coordY,
      tipo: this.tipo,
      mapaId: this.mapaId
    };
  }

  toString(): string {
    return `Celda{idCelda=${this.idCelda}, coordX=${this.coordX}, coordY=${this.coordY}, tipo=${this.tipo}, mapaNombre='${this.mapaNombre}'}`;
  }
}