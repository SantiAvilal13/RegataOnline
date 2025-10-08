import { Injectable } from '@angular/core';
import { UsuarioInterface } from '../../models/usuario/usuario.interface';

@Injectable({
  providedIn: 'root'
})
export class ModelUtilsService {

  constructor() { }

  // Métodos de utilidad para Usuario
  getUsuarioDisplayName(usuario: UsuarioInterface): string {
    return usuario.nombre || 'Usuario sin nombre';
  }

  getUsuarioInitials(usuario: UsuarioInterface): string {
    const names = usuario.nombre.split(' ');
    if (names.length >= 2) {
      return (names[0][0] + names[1][0]).toUpperCase();
    }
    return usuario.nombre.substring(0, 2).toUpperCase();
  }

  getUsuarioAvatarColor(usuario: UsuarioInterface): string {
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD', '#98D8C8', '#F7DC6F'];
    const hash = usuario.nombre.split('').reduce((a, b) => a + b.charCodeAt(0), 0);
    return colors[hash % colors.length];
  }

  getUsuarioStatsSummary(usuario: UsuarioInterface): string {
    const partidas = usuario.totalPartidasGanadas || 0;
    const barcos = usuario.totalBarcos || 0;
    return `${partidas} partidas ganadas • ${barcos} barcos`;
  }

  // Métodos de validación
  validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  validatePassword(password: string): boolean {
    return !!(password && password.length >= 6);
  }

  validateNombre(nombre: string): boolean {
    return !!(nombre && nombre.length >= 2 && nombre.length <= 50);
  }

  // Métodos de formateo
  formatColorHex(color: string): string {
    const colorRegex = /^#[0-9A-Fa-f]{6}$/;
    return colorRegex.test(color) ? color : '#CCCCCC';
  }

  formatCoordenadas(x: number, y: number): string {
    return `(${x},${y})`;
  }

  formatVelocidad(x: number, y: number): string {
    return `(${x || 0},${y || 0})`;
  }

  // Métodos de cálculo
  calculateWinRate(ganadas: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((ganadas / total) * 100);
  }

  getWinRateBadgeClass(winRate: number): string {
    if (winRate >= 70) return 'badge-success';
    if (winRate >= 40) return 'badge-warning';
    return 'badge-danger';
  }

  // Métodos de clonación
  cloneObject<T>(obj: T): T {
    return JSON.parse(JSON.stringify(obj));
  }

  // Métodos de transformación de datos
  transformBackendData<T>(data: any, transformer: (item: any) => T): T[] {
    if (Array.isArray(data)) {
      return data.map(item => transformer(item));
    }
    return [transformer(data)];
  }
}