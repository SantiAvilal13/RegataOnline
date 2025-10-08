import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Modelo } from '../../../models';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';

@Component({
  selector: 'app-modelo-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modelo-form.component.html',
  styleUrl: './modelo-form.component.css'
})
export class ModeloFormComponent implements OnInit {
  @Input() modeloId?: number;
  
  modelo = signal<Modelo>(new Modelo());
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  
  modeloService = inject(ModeloService);

  ngOnInit() {
    if (this.modeloId) {
      this.loadModelo();
    }
  }

  loadModelo() {
    if (this.modeloId) {
      this.modeloService.getModelo(this.modeloId).subscribe({
        next: (modelo) => {
          this.modelo.set(modelo);
        },
        error: (error) => {
          this.error.set('Error al cargar modelo: ' + error.message);
        }
      });
    }
  }

  saveModelo() {
    if (!this.modelo().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const modelo = this.modelo();
    
    if (this.modeloId) {
      this.modeloService.updateModelo(this.modeloId, modelo).subscribe({
        next: () => {
          this.saving.set(false);
          alert('Modelo actualizado exitosamente');
        },
        error: (error) => {
          this.error.set('Error al actualizar modelo: ' + error.message);
          this.saving.set(false);
        }
      });
    } else {
      this.modeloService.createModelo(modelo).subscribe({
        next: () => {
          this.saving.set(false);
          this.modelo.set(new Modelo());
          alert('Modelo creado exitosamente');
        },
        error: (error) => {
          this.error.set('Error al crear modelo: ' + error.message);
          this.saving.set(false);
        }
      });
    }
  }
}
