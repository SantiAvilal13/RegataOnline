export enum DeltaVelocidad {
  DECREMENTAR = 'DECREMENTAR',
  MANTENER = 'MANTENER',
  INCREMENTAR = 'INCREMENTAR'
}

export namespace DeltaVelocidad {
  export function getDisplayName(delta: DeltaVelocidad): string {
    switch (delta) {
      case DeltaVelocidad.DECREMENTAR:
        return 'Decrementar';
      case DeltaVelocidad.MANTENER:
        return 'Mantener';
      case DeltaVelocidad.INCREMENTAR:
        return 'Incrementar';
      default:
        return 'Desconocido';
    }
  }

  export function getAllDeltas(): DeltaVelocidad[] {
    return [DeltaVelocidad.DECREMENTAR, DeltaVelocidad.MANTENER, DeltaVelocidad.INCREMENTAR];
  }

  export function getValue(delta: DeltaVelocidad): number {
    switch (delta) {
      case DeltaVelocidad.DECREMENTAR:
        return -1;
      case DeltaVelocidad.MANTENER:
        return 0;
      case DeltaVelocidad.INCREMENTAR:
        return 1;
      default:
        return 0;
    }
  }

  export function getSymbol(delta: DeltaVelocidad): string {
    switch (delta) {
      case DeltaVelocidad.DECREMENTAR:
        return '-';
      case DeltaVelocidad.MANTENER:
        return '=';
      case DeltaVelocidad.INCREMENTAR:
        return '+';
      default:
        return '?';
    }
  }
}