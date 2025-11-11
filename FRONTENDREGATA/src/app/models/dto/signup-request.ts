export class SignupRequest {
  constructor(
    public nombre: string,
    public email: string,
    public password: string,
    public rol: string
  ) {}
}
