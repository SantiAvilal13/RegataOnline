export interface ModeloInterface {
  idModelo?: number;
  nombre: string;
  colorHex: string;
  cantidadBarcos?: number;
  totalParticipaciones?: number;
}

export interface CreateModeloRequest {
  nombre: string;
  colorHex: string;
}

export interface UpdateModeloRequest {
  idModelo: number;
  nombre?: string;
  colorHex?: string;
}
