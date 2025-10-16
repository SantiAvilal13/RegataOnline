export enum UsuarioRol {
  ADMIN = 'ADMIN',
  JUGADOR = 'JUGADOR'
}

export namespace UsuarioRol {
  export function getDisplayName(rol: UsuarioRol): string {
    switch (rol) {
      case UsuarioRol.ADMIN:
        return 'Administrador';
      case UsuarioRol.JUGADOR:
        return 'Jugador';
      default:
        return 'Desconocido';
    }
  }

  export function getAllRoles(): UsuarioRol[] {
    return [UsuarioRol.ADMIN, UsuarioRol.JUGADOR];
  }
}