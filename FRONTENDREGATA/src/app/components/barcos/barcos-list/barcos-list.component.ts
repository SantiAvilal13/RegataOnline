import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Barco } from '../../../models';
import { BarcoService } from '../../../shared/services/barcos/barco.service';

@Component({
  selector: 'app-barcos-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './barcos-list.component.html',
  styleUrl: './barcos-list.component.css'
})
export class BarcosListComponent implements OnInit {
  barcos = signal<Barco[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  barcoService = inject(BarcoService);

  ngOnInit() {
    this.loadBarcos();
  }

  loadBarcos() {
    this.loading.set(true);
    this.error.set(null);
    
    this.barcoService.getBarcos().subscribe({
      next: (barcos) => {
        this.barcos.set(barcos);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar barcos: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  deleteBarco(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este barco?')) {
      this.barcoService.deleteBarco(id).subscribe({
        next: () => {
          this.loadBarcos();
        },
        error: (error) => {
          this.error.set('Error al eliminar barco: ' + error.message);
        }
      });
    }
  }
}
