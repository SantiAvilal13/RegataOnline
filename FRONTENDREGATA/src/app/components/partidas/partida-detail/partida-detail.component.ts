import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PartidaService } from '../../../shared/services/partidas/partida.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { Partida } from '../../../models';

@Component({
  selector: 'app-partida-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partida-detail.component.html',
  styleUrl: './partida-detail.component.css'
})
export class PartidaDetailComponent {
  partidaService = inject(PartidaService);
  route = inject(ActivatedRoute);
  partida = signal<Partida | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loading.set(true);
    this.route.params.pipe(
      switchMap(params => this.partidaService.getPartida(params['id']))
    ).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar la partida: ' + err.message);
        this.loading.set(false);
      }
    });
  }
}
