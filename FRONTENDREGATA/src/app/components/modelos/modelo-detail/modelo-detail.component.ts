import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { Modelo } from '../../../models';

@Component({
  selector: 'app-modelo-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './modelo-detail.component.html',
  styleUrl: './modelo-detail.component.css'
})
export class ModeloDetailComponent {
  modeloService = inject(ModeloService);
  route = inject(ActivatedRoute);
  modelo = signal<Modelo | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loading.set(true);
    this.route.params.pipe(
      switchMap(params => this.modeloService.getModelo(params['id']))
    ).subscribe({
      next: (modelo) => {
        this.modelo.set(modelo);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el modelo: ' + err.message);
        this.loading.set(false);
      }
    });
  }
}
