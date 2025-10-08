export interface BarcoInterface {
  idBarco?: number;
  alias: string;
  usuarioId: number;
  usuarioNombre?: string;
  modeloId: number;
  modeloNombre?: string;
  modeloColorHex?: string;
  totalParticipaciones?: number;
  partidasGanadas?: number;
}

export interface CreateBarcoRequest {
  alias: string;
  usuarioId: number;
  modeloId: number;
}

export interface UpdateBarcoRequest {
  idBarco: number;
  alias?: string;
  usuarioId?: number;
  modeloId?: number;
}
