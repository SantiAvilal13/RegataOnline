import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Partida } from '../../../models';
import { PartidaService } from '../../../shared/services/partidas/partida.service';

export interface PartidaConInfo {
  partida: any;
  infoJugadores?: {
    totalJugadores: number;
    jugadores: Array<{
      nombreUsuario: string;
      barcoAlias: string;
      numeroTurno: number;
    }>;
    estado: string;
  };
}

@Component({
  selector: 'app-partidas-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partidas-list.component.html',
  styleUrl: './partidas-list.component.css'
})
export class PartidasListComponent implements OnInit, OnDestroy {
  partidas = signal<PartidaConInfo[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  autoRefreshActivo = signal(true);
  
  partidaService = inject(PartidaService);
  router = inject(Router);
  
  private autoRefreshInterval: any;
  readonly REFRESH_INTERVAL = 3000; // 3 segundos

  ngOnInit() {
    this.loadPartidas();
    this.iniciarAutoRefresh();
  }

  ngOnDestroy() {
    this.detenerAutoRefresh();
  }

  iniciarAutoRefresh() {
    if (this.autoRefreshInterval) {
      clearInterval(this.autoRefreshInterval);
    }
    
    this.autoRefreshInterval = setInterval(() => {
      if (this.autoRefreshActivo()) {
        this.loadPartidas(true); // true = silencioso (sin mostrar loading)
      }
    }, this.REFRESH_INTERVAL);
  }

  detenerAutoRefresh() {
    if (this.autoRefreshInterval) {
      clearInterval(this.autoRefreshInterval);
      this.autoRefreshInterval = null;
    }
  }

  toggleAutoRefresh() {
    this.autoRefreshActivo.set(!this.autoRefreshActivo());
    
    if (this.autoRefreshActivo()) {
      this.iniciarAutoRefresh();
      this.loadPartidas();
    } else {
      this.detenerAutoRefresh();
    }
  }

  loadPartidas(silencioso: boolean = false) {
    if (!silencioso) {
      this.loading.set(true);
    }
    this.error.set(null);
    
    this.partidaService.getPartidas().subscribe({
      next: (partidas) => {
        if (partidas.length === 0) {
          this.partidas.set([]);
          this.loading.set(false);
          return;
        }

        const partidasConInfo: PartidaConInfo[] = [];
        let completados = 0;

        partidas.forEach(partida => {
          this.partidaService.obtenerInfoJugadores(partida.idPartida!).subscribe({
            next: (info) => {
              partidasConInfo.push({
                partida,
                infoJugadores: info
              });
              completados++;

              if (completados === partidas.length) {
                this.partidas.set(partidasConInfo);
                this.loading.set(false);
              }
            },
            error: () => {
              // Si falla, agregar sin info
              partidasConInfo.push({ partida });
              completados++;

              if (completados === partidas.length) {
                this.partidas.set(partidasConInfo);
                this.loading.set(false);
              }
            }
          });
        });
      },
      error: (error) => {
        this.error.set('Error al cargar partidas: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  crearPartidaSolitario() {
    this.router.navigate(['/partidas/new']);
  }

  crearPartidaMultijugador() {
    this.router.navigate(['/partidas/crear-multijugador']);
  }

  unirseAPartida(partidaId: number) {
    // Redirigir a selección de barco para unirse
    this.router.navigate(['/partidas', partidaId, 'unirse']);
  }

  iniciarPartida(partidaId: number) {
    if (confirm('¿Iniciar la partida? Los jugadores actuales comenzarán a jugar.')) {
      this.partidaService.iniciarPartida(partidaId).subscribe({
        next: (response) => {
          alert('¡Partida iniciada exitosamente!');
          this.loadPartidas();
        },
        error: (err) => {
          const errorMsg = err.error?.error || 'No se pudo iniciar la partida';
          alert('Error: ' + errorMsg);
        }
      });
    }
  }

  verPartida(partidaId: number) {
    this.router.navigate(['/partidas', partidaId]);
  }

  eliminarPartida(partidaId: number) {
    if (confirm('¿Estás seguro de eliminar esta partida?')) {
      this.partidaService.deletePartida(partidaId).subscribe({
        next: () => {
          alert('Partida eliminada');
          this.loadPartidas();
        },
        error: (err) => {
          alert('Error al eliminar: ' + err.message);
        }
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado) {
      case 'ESPERANDO':
        return 'badge-warning';
      case 'EN_JUEGO':
        return 'badge-success';
      case 'TERMINADA':
        return 'badge-secondary';
      default:
        return 'badge-info';
    }
  }
}
