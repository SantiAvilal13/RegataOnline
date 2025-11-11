import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Modelo } from '../../../models';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-modelos-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './modelos-list.component.html',
  styleUrl: './modelos-list.component.css'
})
export class ModelosListComponent {
  modelos = signal<Modelo[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  modeloService = inject(ModeloService);
  authService = inject(AuthService);

  ngOnInit() {
    this.loadModelos();
  }

  loadModelos() {
    this.loading.set(true);
    this.error.set(null);
    
    this.modeloService.getModelos().subscribe({
      next: (modelos) => {
        this.modelos.set(modelos);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar modelos: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  deleteModelo(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este modelo?')) {
      this.modeloService.deleteModelo(id).subscribe({
        next: () => {
          this.loadModelos();
        },
        error: (error) => {
          this.error.set('Error al eliminar modelo: ' + error.message);
        }
      });
    }
  }
}
