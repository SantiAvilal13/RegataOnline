import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, Observable } from 'rxjs';
import { Partida } from '../../../models';
import { PartidaService } from '../../../shared/services/partidas/partida.service';

@Component({
  selector: 'app-partida-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-form.component.html',
  styleUrl: './partida-form.component.css'
})
export class PartidaFormComponent {
  partida = signal<Partida>(new Partida());
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  isEditMode = signal(false);
  
  partidaService = inject(PartidaService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit() {
    this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          this.isEditMode.set(true);
          this.loading.set(true);
          return this.partidaService.getPartida(id);
        } else {
          // Modo creación - retornamos un observable vacío
          return new Observable<Partida>(subscriber => {
            subscriber.complete();
          });
        }
      })
    ).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar la partida: ' + err.message);
        this.loading.set(false);
      },
      complete: () => {
        // En modo creación, no hay datos que cargar
        this.loading.set(false);
      }
    });
  }

  savePartida() {
    if (!this.partida().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const partida = this.partida();
    const operation = this.isEditMode()
      ? this.partidaService.updatePartida(partida.idPartida!, partida)
      : this.partidaService.createPartida(partida);
    
    operation.subscribe({
      next: () => {
        this.saving.set(false);
        alert(this.isEditMode() ? 'Partida actualizada exitosamente' : 'Partida creada exitosamente');
        this.router.navigate(['/partidas']);
      },
      error: (error) => {
        this.error.set('Error al guardar partida: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
