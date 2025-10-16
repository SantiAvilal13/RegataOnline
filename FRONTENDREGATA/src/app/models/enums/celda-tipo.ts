export enum CeldaTipo {
  AGUA = 'AGUA',
  PARED = 'PARED',
  PARTIDA = 'PARTIDA',
  META = 'META'
}

export namespace CeldaTipo {
  export function getDisplayName(tipo: CeldaTipo): string {
    switch (tipo) {
      case CeldaTipo.AGUA:
        return 'Agua';
      case CeldaTipo.PARED:
        return 'Pared';
      case CeldaTipo.PARTIDA:
        return 'Línea de partida';
      case CeldaTipo.META:
        return 'Meta';
      default:
        return 'Desconocido';
    }
  }

  export function getAllTipos(): CeldaTipo[] {
    return [CeldaTipo.AGUA, CeldaTipo.PARED, CeldaTipo.PARTIDA, CeldaTipo.META];
  }

  export function getColor(tipo: CeldaTipo): string {
    switch (tipo) {
      case CeldaTipo.AGUA:
        return '#0077BE'; // Azul agua
      case CeldaTipo.PARED:
        return '#8B4513'; // Marrón pared
      case CeldaTipo.PARTIDA:
        return '#00FF00'; // Verde partida
      case CeldaTipo.META:
        return '#FFD700'; // Dorado meta
      default:
        return '#CCCCCC'; // Gris por defecto
    }
  }

  export function isWater(tipo: CeldaTipo): boolean {
    return tipo === CeldaTipo.AGUA;
  }

  export function isWall(tipo: CeldaTipo): boolean {
    return tipo === CeldaTipo.PARED;
  }

  export function isStart(tipo: CeldaTipo): boolean {
    return tipo === CeldaTipo.PARTIDA;
  }

  export function isFinish(tipo: CeldaTipo): boolean {
    return tipo === CeldaTipo.META;
  }
}