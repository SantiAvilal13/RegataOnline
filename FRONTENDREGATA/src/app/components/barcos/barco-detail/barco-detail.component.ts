import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Barco } from '../../../models';
import { BarcoService } from '../../../shared/services/barcos/barco.service';

@Component({
  selector: 'app-barco-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './barco-detail.component.html',
  styleUrl: './barco-detail.component.css'
})
export class BarcoDetailComponent implements OnInit {
  @Input() barcoId!: number;
  
  barco = signal<Barco | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  
  barcoService = inject(BarcoService);

  ngOnInit() {
    if (this.barcoId) {
      this.loadBarco();
    }
  }

  loadBarco() {
    this.loading.set(true);
    this.error.set(null);
    
    this.barcoService.getBarco(this.barcoId).subscribe({
      next: (barco) => {
        this.barco.set(barco);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar barco: ' + error.message);
        this.loading.set(false);
      }
    });
  }
}
