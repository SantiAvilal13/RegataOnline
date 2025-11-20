export class JwtAuthenticationResponse {
  constructor(
    public token: string,
    public email: string,
    public rol: string,  // Cambiado de 'role' a 'rol' para coincidir con el backend
    public nombre: string,
    public idUsuario: number
  ) {}
}
