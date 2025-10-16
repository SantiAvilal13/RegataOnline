export enum PartidaEstado {
  ESPERANDO = 'ESPERANDO',
  EN_JUEGO = 'EN_JUEGO',
  TERMINADA = 'TERMINADA'
}

export namespace PartidaEstado {
  export function getDisplayName(estado: PartidaEstado): string {
    switch (estado) {
      case PartidaEstado.ESPERANDO:
        return 'Esperando jugadores';
      case PartidaEstado.EN_JUEGO:
        return 'En juego';
      case PartidaEstado.TERMINADA:
        return 'Terminada';
      default:
        return 'Desconocido';
    }
  }

  export function getAllEstados(): PartidaEstado[] {
    return [PartidaEstado.ESPERANDO, PartidaEstado.EN_JUEGO, PartidaEstado.TERMINADA];
  }

  export function isActive(estado: PartidaEstado): boolean {
    return estado === PartidaEstado.EN_JUEGO;
  }

  export function isFinished(estado: PartidaEstado): boolean {
    return estado === PartidaEstado.TERMINADA;
  }

  export function isWaiting(estado: PartidaEstado): boolean {
    return estado === PartidaEstado.ESPERANDO;
  }
}