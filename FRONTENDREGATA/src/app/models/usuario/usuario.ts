import { UsuarioRol } from '../enums/usuario-rol';

export class Usuario {
  idUsuario?: number;
  nombre: string = '';
  email: string = '';
  password?: string;
  rol: UsuarioRol = UsuarioRol.JUGADOR;
  
  // Campos adicionales para la vista (sin relaciones para evitar referencias circulares)
  totalPartidasGanadas?: number;
  totalBarcos?: number;

  constructor(init?: Partial<Usuario>) {
    // Inicializar valores por defecto para formularios
    this.nombre = '';
    this.email = '';
    this.rol = UsuarioRol.JUGADOR;
    
    // Asignar valores si se proporcionan (principalmente desde el backend)
    if (init) {
      Object.assign(this, init);
    }
  }

  // Métodos de utilidad (equivalentes a los del DTO del backend)
  esJugador(): boolean {
    return this.rol === UsuarioRol.JUGADOR;
  }

  esAdmin(): boolean {
    return this.rol === UsuarioRol.ADMIN;
  }

  getRolDisplayName(): string {
    return this.rol != null ? this.rol : "";
  }

  getTotalPartidasGanadas(): number {
    return this.totalPartidasGanadas || 0;
  }

  getTotalBarcos(): number {
    return this.totalBarcos || 0;
  }

  // Métodos para validación del frontend
  isValid(): boolean {
    return !!(this.nombre && this.email && this.rol);
  }

  hasValidEmail(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this.email);
  }

  hasValidPassword(): boolean {
    return !!(this.password && this.password.length >= 6);
  }

  // Métodos específicos para la UI del frontend
  getDisplayName(): string {
    return this.nombre || 'Usuario sin nombre';
  }

  getInitials(): string {
    const names = this.nombre.split(' ');
    if (names.length >= 2) {
      return (names[0][0] + names[1][0]).toUpperCase();
    }
    return this.nombre.substring(0, 2).toUpperCase();
  }

  getAvatarColor(): string {
    // Generar color basado en el nombre para avatares
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD', '#98D8C8', '#F7DC6F'];
    const hash = this.nombre.split('').reduce((a, b) => a + b.charCodeAt(0), 0);
    return colors[hash % colors.length];
  }

  getStatsSummary(): string {
    const partidas = this.getTotalPartidasGanadas();
    const barcos = this.getTotalBarcos();
    return `${partidas} partidas ganadas • ${barcos} barcos`;
  }

  canManageUsers(): boolean {
    return this.esAdmin();
  }

  canCreateBoats(): boolean {
    return true; // Todos los usuarios pueden crear barcos
  }

  getRoleBadgeClass(): string {
    return this.esAdmin() ? 'badge-danger' : 'badge-primary';
  }

  // Método para clonar
  clone(): Usuario {
    return new Usuario({
      idUsuario: this.idUsuario,
      nombre: this.nombre,
      email: this.email,
      password: this.password,
      rol: this.rol,
      totalPartidasGanadas: this.totalPartidasGanadas,
      totalBarcos: this.totalBarcos
    });
  }

  // Método para crear desde DTO del backend
  static fromDTO(dto: any): Usuario {
    return new Usuario({
      idUsuario: dto.idUsuario,
      nombre: dto.nombre,
      email: dto.email,
      password: dto.password,
      rol: dto.rol,
      totalPartidasGanadas: dto.totalPartidasGanadas,
      totalBarcos: dto.totalBarcos
    });
  }

  // Método para convertir a DTO del backend
  toDTO(): any {
    return {
      idUsuario: this.idUsuario,
      nombre: this.nombre,
      email: this.email,
      password: this.password,
      rol: this.rol
    };
  }

  toString(): string {
    return `Usuario{idUsuario=${this.idUsuario}, nombre='${this.nombre}', email='${this.email}', rol=${this.rol}}`;
  }
}