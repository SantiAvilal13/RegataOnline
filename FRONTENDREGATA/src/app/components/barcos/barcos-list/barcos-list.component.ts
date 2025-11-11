import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Barco } from '../../../models';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-barcos-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './barcos-list.component.html',
  styleUrl: './barcos-list.component.css'
})
export class BarcosListComponent {
  barcos = signal<Barco[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  barcoService = inject(BarcoService);
  authService = inject(AuthService);

  ngOnInit() {
    this.loadBarcos();
  }

  loadBarcos() {
    this.loading.set(true);
    this.error.set(null);
    
    this.barcoService.getBarcos().subscribe({
      next: (barcos) => {
        this.barcos.set(barcos);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar barcos: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  deleteBarco(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este barco?')) {
      this.barcoService.deleteBarco(id).subscribe({
        next: () => {
          this.loadBarcos();
        },
        error: (error) => {
          this.error.set('Error al eliminar barco: ' + error.message);
        }
      });
    }
  }

  /**
   * Verifica si el usuario puede editar/eliminar un barco
   * Un JUGADOR solo puede editar/eliminar sus propios barcos
   * Un ADMIN puede editar/eliminar cualquier barco
   */
  canEditOrDelete(barco: Barco): boolean {
    if (this.authService.isAdmin()) {
      return true;
    }
    return barco.usuarioId === this.authService.idUsuario();
  }

  /**
   * Verifica si el usuario solo puede ver el barco (no editar/eliminar)
   */
  canOnlyView(barco: Barco): boolean {
    return !this.canEditOrDelete(barco);
  }
}
