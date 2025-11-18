import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MapaJuegoService } from '../../../shared/services/juego/mapa-juego.service';
import { AuthService } from '../../../services/auth.service';
import { Mapa } from '../../../models';

@Component({
  selector: 'app-map-selector',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './map-selector.component.html',
  styleUrl: './map-selector.component.css'
})
export class MapSelectorComponent implements OnInit {
  mapaService = inject(MapaJuegoService);
  router = inject(Router);
  authService = inject(AuthService);
  
  // Hacer Math disponible en el template
  Math = Math;

  mapas = signal<Mapa[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    // Verificar que el usuario no es administrador
    if (this.authService.isAdmin()) {
      this.error.set('Los administradores no pueden jugar. Solo pueden realizar operaciones CRUD.');
      setTimeout(() => this.router.navigate(['/home']), 2000);
      return;
    }

    this.cargarMapas();
  }

  cargarMapas() {
    this.loading.set(true);
    this.mapaService.getMapas().subscribe({
      next: (mapas) => {
        this.mapas.set(mapas);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar los mapas: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  seleccionarMapa(mapa: Mapa) {
    this.router.navigate(['/game/mapa', mapa.idMapa]);
  }
}
