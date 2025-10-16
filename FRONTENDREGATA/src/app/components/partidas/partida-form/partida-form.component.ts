import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, Observable } from 'rxjs';
import { Partida, Mapa } from '../../../models';
import { PartidaEstado } from '../../../models/enums/partida-estado';
import { PartidaService } from '../../../shared/services/partidas/partida.service';
import { MapaJuegoService } from '../../../shared/services/juego/mapa-juego.service';

@Component({
  selector: 'app-partida-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-form.component.html',
  styleUrl: './partida-form.component.css'
})
export class PartidaFormComponent {
  partida = signal<Partida>(new Partida());
  mapasDisponibles = signal<Mapa[]>([]);
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  isEditMode = signal(false);
  
  partidaService = inject(PartidaService);
  mapaService = inject(MapaJuegoService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit() {
    // Cargar mapas disponibles
    this.cargarMapas();
    
    this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          this.isEditMode.set(true);
          this.loading.set(true);
          return this.partidaService.getPartida(id);
        } else {
          // Modo creación - configurar partida por defecto
          this.configurarPartidaNueva();
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

  cargarMapas() {
    this.mapaService.getMapas().subscribe({
      next: (mapas) => {
        this.mapasDisponibles.set(mapas);
      },
      error: (err) => {
        this.error.set('Error al cargar los mapas: ' + err.message);
      }
    });
  }

  configurarPartidaNueva() {
    // Configurar partida nueva con valores por defecto
    const nuevaPartida = new Partida();
    nuevaPartida.estado = PartidaEstado.ESPERANDO; // Estado por defecto
    this.partida.set(nuevaPartida);
  }

  savePartida() {
    if (!this.partida().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const partida = this.partida();
    
    if (this.isEditMode()) {
      // Modo edición - comportamiento normal
      this.partidaService.updatePartida(partida.idPartida!, partida).subscribe({
        next: () => {
          this.saving.set(false);
          alert('Partida actualizada exitosamente');
          this.router.navigate(['/partidas']);
        },
        error: (error) => {
          this.error.set('Error al guardar partida: ' + error.message);
          this.saving.set(false);
        }
      });
    } else {
      // Modo creación - usar el nuevo endpoint que redirige al juego
      this.partidaService.createPartidaAndPlay(partida).subscribe({
        next: (response: any) => {
          this.saving.set(false);
          alert('¡Partida creada! ¡A jugar!');
          // Redirigir al mapa del juego
          this.router.navigate([response.redirectTo]);
        },
        error: (error) => {
          this.error.set('Error al crear partida: ' + error.message);
          this.saving.set(false);
        }
      });
    }
  }
}
