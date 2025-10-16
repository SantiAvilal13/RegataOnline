export class Barco {
  idBarco?: number;
  alias: string = '';
  usuarioId: number = 0;
  usuarioNombre?: string;
  modeloId: number = 0;
  modeloNombre?: string;
  modeloColorHex?: string;
  
  // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
  totalParticipaciones?: number;
  partidasGanadas?: number;

  constructor(init?: Partial<Barco>) {
    // Inicializar valores por defecto para formularios
    this.alias = '';
    this.usuarioId = 0;
    this.modeloId = 0;
    
    // Asignar valores si se proporcionan (principalmente desde el backend)
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad
  getUsuarioNombre(): string {
    return this.usuarioNombre || 'Sin usuario';
  }

  getModeloNombre(): string {
    return this.modeloNombre || 'Sin modelo';
  }

  getModeloColorHex(): string {
    return this.modeloColorHex || '#CCCCCC';
  }

  getTotalParticipaciones(): number {
    return this.totalParticipaciones || 0;
  }

  getPartidasGanadas(): number {
    return this.partidasGanadas || 0;
  }

  // Métodos para validación del frontend
  isValid(): boolean {
    return !!(this.alias && this.usuarioId && this.modeloId);
  }

  hasValidAlias(): boolean {
    return !!(this.alias && this.alias.length >= 2 && this.alias.length <= 50);
  }

  // Métodos específicos para la UI del frontend
  getDisplayName(): string {
    return this.alias || 'Barco sin nombre';
  }

  getFullInfo(): string {
    return `${this.getDisplayName()} (${this.getUsuarioNombre()})`;
  }

  getModeloColor(): string {
    return this.modeloColorHex || '#CCCCCC';
  }

  getStatsSummary(): string {
    const participaciones = this.getTotalParticipaciones();
    const ganadas = this.getPartidasGanadas();
    return `${participaciones} participaciones • ${ganadas} ganadas`;
  }

  getWinRate(): number {
    const participaciones = this.getTotalParticipaciones();
    const ganadas = this.getPartidasGanadas();
    if (participaciones === 0) return 0;
    return Math.round((ganadas / participaciones) * 100);
  }

  getWinRateDisplay(): string {
    return `${this.getWinRate()}% victorias`;
  }

  getStatusBadgeClass(): string {
    const winRate = this.getWinRate();
    if (winRate >= 70) return 'badge-success';
    if (winRate >= 40) return 'badge-warning';
    return 'badge-danger';
  }

  canBeEdited(): boolean {
    return !!(this.idBarco); // Solo barcos existentes pueden ser editados
  }

  canBeDeleted(): boolean {
    const participaciones = this.getTotalParticipaciones();
    return participaciones === 0; // Solo barcos sin participaciones pueden ser eliminados
  }

  // Método para clonar
  clone(): Barco {
    return new Barco({
      idBarco: this.idBarco,
      alias: this.alias,
      usuarioId: this.usuarioId,
      usuarioNombre: this.usuarioNombre,
      modeloId: this.modeloId,
      modeloNombre: this.modeloNombre,
      modeloColorHex: this.modeloColorHex,
      totalParticipaciones: this.totalParticipaciones,
      partidasGanadas: this.partidasGanadas
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Barco {
    return new Barco({
      idBarco: dto.idBarco,
      alias: dto.alias,
      usuarioId: dto.usuarioId,
      usuarioNombre: dto.usuarioNombre,
      modeloId: dto.modeloId,
      modeloNombre: dto.modeloNombre,
      modeloColorHex: dto.modeloColorHex,
      totalParticipaciones: dto.totalParticipaciones,
      partidasGanadas: dto.partidasGanadas
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idBarco: this.idBarco,
      alias: this.alias,
      usuarioId: this.usuarioId,
      modeloId: this.modeloId
    };
  }

  toString(): string {
    return `Barco{idBarco=${this.idBarco}, alias='${this.alias}', usuarioId=${this.usuarioId}, usuarioNombre='${this.usuarioNombre}', modeloId=${this.modeloId}, modeloNombre='${this.modeloNombre}', modeloColorHex='${this.modeloColorHex}'}`;
  }
}