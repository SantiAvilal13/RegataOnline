import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { Barco } from '../../../models';

@Component({
  selector: 'app-barco-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './barco-detail.component.html',
  styleUrl: './barco-detail.component.css'
})
export class BarcoDetailComponent {
  barcoService = inject(BarcoService);
  route = inject(ActivatedRoute);
  barco = signal<Barco | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loading.set(true);
    this.route.params.pipe(
      switchMap(params => this.barcoService.getBarco(params['id']))
    ).subscribe({
      next: (barco) => {
        this.barco.set(barco);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el barco: ' + err.message);
        this.loading.set(false);
      }
    });
  }
}
