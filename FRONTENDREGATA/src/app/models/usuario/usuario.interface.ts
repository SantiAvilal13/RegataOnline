import { UsuarioRol } from '../enums/usuario-rol';

export interface UsuarioInterface {
  idUsuario?: number;
  nombre: string;
  email: string;
  password?: string;
  rol: UsuarioRol;
  totalPartidasGanadas?: number;
  totalBarcos?: number;
}

// Interface para crear usuarios (sin ID)
export interface CreateUsuarioRequest {
  nombre: string;
  email: string;
  password: string;
  rol: UsuarioRol;
}

// Interface para actualizar usuarios
export interface UpdateUsuarioRequest {
  idUsuario: number;
  nombre?: string;
  email?: string;
  rol?: UsuarioRol;
}
