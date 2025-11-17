import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { PartidaService } from '../../../shared/services/partidas/partida.service';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-partida-crear-multi',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partida-crear-multi.component.html',
  styleUrl: './partida-crear-multi.component.css'
})
export class PartidaCrearMultiComponent implements OnInit {
  private router = inject(Router);
  private partidaService = inject(PartidaService);
  private barcoService = inject(BarcoService);
  private authService = inject(AuthService);

  barcos = signal<any[]>([]);
  barcoSeleccionado = signal<number | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  usuarioId = signal<number | null>(null);

  ngOnInit() {
    const userId = this.authService.idUsuario();
    if (userId) {
      this.usuarioId.set(userId);
      this.loadBarcos(userId);
    } else {
      this.error.set('No se pudo obtener el ID del usuario');
    }
  }

  loadBarcos(userId: number) {
    this.barcoService.getBarcosByUsuario(userId).subscribe({
      next: (barcos) => {
        this.barcos.set(barcos);
        // Seleccionar primer barco por defecto
        if (barcos.length > 0 && barcos[0].idBarco) {
          this.barcoSeleccionado.set(barcos[0].idBarco);
        }
      },
      error: (err) => {
        console.error('Error al cargar barcos:', err);
        this.error.set('Error al cargar tus barcos');
      }
    });
  }

  seleccionarBarco(barcoId: number) {
    this.barcoSeleccionado.set(barcoId);
  }

  crearPartida() {
    if (!this.barcoSeleccionado() || !this.usuarioId()) {
      alert('Por favor selecciona un barco');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    // Crear la partida en estado ESPERANDO con un mapa por defecto (ID 1)
    const partidaData = {
      mapaId: 1, // Mapa por defecto
      estado: 'ESPERANDO'
    };

    this.partidaService.crearPartidaYEsperar(
      partidaData,
      this.usuarioId()!,
      this.barcoSeleccionado()!
    ).subscribe({
      next: (response) => {
        alert('¡Partida multijugador creada! Ya puedes jugar o esperar a otros jugadores.');
        this.loading.set(false);
        
        // Redirigir al game board con la participación creada
        if (response.participacionId) {
          this.router.navigate(['/game/participacion', response.participacionId]);
        } else {
          this.router.navigate(['/partidas']);
        }
      },
      error: (err) => {
        console.error('Error al crear partida:', err);
        this.error.set('Error al crear partida: ' + (err.error?.error || err.message));
        this.loading.set(false);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/partidas']);
  }
}
