export class Modelo {
  idModelo?: number;
  nombre: string = '';
  colorHex: string = '#000000';
  
  // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
  cantidadBarcos?: number;
  totalParticipaciones?: number;

  constructor(init?: Partial<Modelo>) {
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad
  getCantidadBarcos(): number {
    return this.cantidadBarcos || 0;
  }

  getTotalParticipaciones(): number {
    return this.totalParticipaciones || 0;
  }

  // Métodos para validación
  isValid(): boolean {
    return !!(this.nombre && this.colorHex);
  }

  hasValidNombre(): boolean {
    return !!(this.nombre && this.nombre.length >= 2 && this.nombre.length <= 50);
  }

  hasValidColorHex(): boolean {
    const colorRegex = /^#[0-9A-Fa-f]{6}$/;
    return colorRegex.test(this.colorHex);
  }

  // Método para clonar
  clone(): Modelo {
    return new Modelo({
      idModelo: this.idModelo,
      nombre: this.nombre,
      colorHex: this.colorHex,
      cantidadBarcos: this.cantidadBarcos,
      totalParticipaciones: this.totalParticipaciones
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Modelo {
    return new Modelo({
      idModelo: dto.idModelo,
      nombre: dto.nombre,
      colorHex: dto.colorHex,
      cantidadBarcos: dto.cantidadBarcos,
      totalParticipaciones: dto.totalParticipaciones
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idModelo: this.idModelo,
      nombre: this.nombre,
      colorHex: this.colorHex
    };
  }

  toString(): string {
    return `Modelo{idModelo=${this.idModelo}, nombre='${this.nombre}', colorHex='${this.colorHex}', cantidadBarcos=${this.cantidadBarcos}}`;
  }
}