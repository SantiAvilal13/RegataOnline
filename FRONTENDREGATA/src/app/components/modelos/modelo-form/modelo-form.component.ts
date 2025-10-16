import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, Observable } from 'rxjs';
import { Modelo } from '../../../models';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';

@Component({
  selector: 'app-modelo-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modelo-form.component.html',
  styleUrl: './modelo-form.component.css'
})
export class ModeloFormComponent {
  modelo = signal<Modelo>(new Modelo());
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  isEditMode = signal(false);
  
  modeloService = inject(ModeloService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit() {
    this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          this.isEditMode.set(true);
          this.loading.set(true);
          return this.modeloService.getModelo(id);
        } else {
          // Modo creación - retornamos un observable vacío
          return new Observable<Modelo>(subscriber => {
            subscriber.complete();
          });
        }
      })
    ).subscribe({
      next: (modelo) => {
        this.modelo.set(modelo);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el modelo: ' + err.message);
        this.loading.set(false);
      },
      complete: () => {
        // En modo creación, no hay datos que cargar
        this.loading.set(false);
      }
    });
  }

  saveModelo() {
    if (!this.modelo().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const modelo = this.modelo();
    const operation = this.isEditMode()
      ? this.modeloService.updateModelo(modelo.idModelo!, modelo)
      : this.modeloService.createModelo(modelo);
    
    operation.subscribe({
      next: () => {
        this.saving.set(false);
        alert(this.isEditMode() ? 'Modelo actualizado exitosamente' : 'Modelo creado exitosamente');
        this.router.navigate(['/modelos']);
      },
      error: (error) => {
        this.error.set('Error al guardar modelo: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
