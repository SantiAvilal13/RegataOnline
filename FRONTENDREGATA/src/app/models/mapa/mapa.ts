import { Celda } from '../celda/celda';

export class Mapa {
  idMapa?: number;
  nombre: string = '';
  tamFilas?: number;
  tamColumnas?: number;
  celdas?: Celda[];

  constructor(init?: Partial<Mapa>) {
    if (init) {
      Object.assign(this, init);
      // Convertir celdas si vienen como DTOs
      if (init.celdas) {
        this.celdas = init.celdas.map((celda: any) => Celda.fromDTO(celda));
      }
    }
  }

  // Métodos de utilidad
  getTamFilas(): number {
    return this.tamFilas || 0;
  }

  getTamColumnas(): number {
    return this.tamColumnas || 0;
  }

  getCeldas(): Celda[] {
    return this.celdas || [];
  }

  getCeldaPorCoordenadas(x: number, y: number): Celda | undefined {
    return this.celdas?.find(celda => celda.coordX === x && celda.coordY === y);
  }

  getCeldasPorTipo(tipo: any): Celda[] {
    return this.celdas?.filter(celda => celda.tipo === tipo) || [];
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.nombre && this.tamFilas && this.tamColumnas);
  }

  hasValidDimensions(): boolean {
    return !!(this.tamFilas && this.tamColumnas && this.tamFilas > 0 && this.tamColumnas > 0);
  }

  // Método para clonar
  clone(): Mapa {
    return new Mapa({
      idMapa: this.idMapa,
      nombre: this.nombre,
      tamFilas: this.tamFilas,
      tamColumnas: this.tamColumnas,
      celdas: this.celdas?.map(celda => celda.clone())
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Mapa {
    return new Mapa({
      idMapa: dto.idMapa,
      nombre: dto.nombre,
      tamFilas: dto.tamFilas,
      tamColumnas: dto.tamColumnas,
      celdas: dto.celdas
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idMapa: this.idMapa,
      nombre: this.nombre,
      tamFilas: this.tamFilas,
      tamColumnas: this.tamColumnas,
      celdas: this.celdas?.map(celda => celda.toDTO())
    };
  }

  toString(): string {
    return `Mapa{idMapa=${this.idMapa}, nombre='${this.nombre}', tamFilas=${this.tamFilas}, tamColumnas=${this.tamColumnas}}`;
  }
}