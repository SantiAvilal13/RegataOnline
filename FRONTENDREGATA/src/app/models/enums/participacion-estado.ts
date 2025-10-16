export enum ParticipacionEstado {
  ACTIVO = 'ACTIVO',
  DESTRUIDO = 'DESTRUIDO',
  EN_META = 'EN_META'
}

export namespace ParticipacionEstado {
  export function getDisplayName(estado: ParticipacionEstado): string {
    switch (estado) {
      case ParticipacionEstado.ACTIVO:
        return 'Activo';
      case ParticipacionEstado.DESTRUIDO:
        return 'Destruido';
      case ParticipacionEstado.EN_META:
        return 'En meta';
      default:
        return 'Desconocido';
    }
  }

  export function getAllEstados(): ParticipacionEstado[] {
    return [ParticipacionEstado.ACTIVO, ParticipacionEstado.DESTRUIDO, ParticipacionEstado.EN_META];
  }

  export function isActive(estado: ParticipacionEstado): boolean {
    return estado === ParticipacionEstado.ACTIVO;
  }

  export function isDestroyed(estado: ParticipacionEstado): boolean {
    return estado === ParticipacionEstado.DESTRUIDO;
  }

  export function isAtFinish(estado: ParticipacionEstado): boolean {
    return estado === ParticipacionEstado.EN_META;
  }
}