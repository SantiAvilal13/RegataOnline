import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Partida } from '../../../models';
import { PartidaService } from '../../../shared/services/partidas/partida.service';

@Component({
  selector: 'app-partidas-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partidas-list.component.html',
  styleUrl: './partidas-list.component.css'
})
export class PartidasListComponent {
  partidas = signal<Partida[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  partidaService = inject(PartidaService);

  ngOnInit() {
    this.loadPartidas();
  }

  loadPartidas() {
    this.loading.set(true);
    this.error.set(null);
    
    this.partidaService.getPartidas().subscribe({
      next: (partidas) => {
        this.partidas.set(partidas);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar partidas: ' + error.message);
        this.loading.set(false);
      }
    });
  }
}
