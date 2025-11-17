import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PartidaService } from '../../../shared/services/partidas/partida.service';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-partida-unirse',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partida-unirse.component.html',
  styleUrl: './partida-unirse.component.css'
})
export class PartidaUnirseComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private partidaService = inject(PartidaService);
  private barcoService = inject(BarcoService);
  private authService = inject(AuthService);

  partidaId = signal<number | null>(null);
  partida = signal<any>(null);
  barcos = signal<any[]>([]);
  barcoSeleccionado = signal<number | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  usuarioId = signal<number | null>(null);

  ngOnInit() {
    // Obtener ID de la partida de la ruta
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.partidaId.set(+id);
      this.loadPartida(+id);
    }

    // Obtener usuario actual
    const userId = this.authService.idUsuario();
    if (userId) {
      this.usuarioId.set(userId);
      this.loadBarcos();
    }
  }

  loadPartida(id: number) {
    this.partidaService.getPartida(id).subscribe({
      next: (partida) => {
        this.partida.set(partida);
      },
      error: (err) => {
        console.error('Error al cargar partida:', err);
        this.error.set('Error al cargar partida');
      }
    });
  }

  loadBarcos() {
    const userId = this.usuarioId();
    if (!userId) {
      this.error.set('No se pudo obtener el ID del usuario');
      return;
    }

    // Obtener SOLO los barcos del usuario actual
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

  unirse() {
    if (!this.barcoSeleccionado() || !this.partidaId() || !this.usuarioId()) {
      alert('Por favor selecciona un barco');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.partidaService.unirseAPartida(
      this.partidaId()!,
      this.usuarioId()!,
      this.barcoSeleccionado()!
    ).subscribe({
      next: (response) => {
        alert('¡Te has unido exitosamente a la partida!');
        this.loading.set(false);
        
        // Redirigir al game board si se proporcionó participacionId
        if (response.participacionId) {
          this.router.navigate(['/game/participacion', response.participacionId]);
        } else {
          this.router.navigate(['/partidas']);
        }
      },
      error: (err) => {
        console.error('Error al unirse:', err);
        const errorMsg = err.error?.error || 'No se pudo unir a la partida';
        this.error.set(errorMsg);
        alert('Error: ' + errorMsg);
        this.loading.set(false);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/partidas']);
  }
}
