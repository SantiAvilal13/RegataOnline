import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Barco, Usuario, Modelo } from '../../../models';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';

@Component({
  selector: 'app-barco-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './barco-form.component.html',
  styleUrl: './barco-form.component.css'
})
export class BarcoFormComponent implements OnInit {
  @Input() barcoId?: number;
  
  barco = signal<Barco>(new Barco());
  usuarios = signal<Usuario[]>([]);
  modelos = signal<Modelo[]>([]);
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  
  barcoService = inject(BarcoService);
  usuarioService = inject(UsuarioService);
  modeloService = inject(ModeloService);

  ngOnInit() {
    this.loadData();
    if (this.barcoId) {
      this.loadBarco();
    }
  }

  loadData() {
    this.loading.set(true);
    
    // Cargar usuarios y modelos en paralelo
    this.usuarioService.getUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
      },
      error: (error) => {
        this.error.set('Error al cargar usuarios: ' + error.message);
      }
    });

    this.modeloService.getModelos().subscribe({
      next: (modelos) => {
        this.modelos.set(modelos);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar modelos: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  loadBarco() {
    if (this.barcoId) {
      this.barcoService.getBarco(this.barcoId).subscribe({
        next: (barco) => {
          this.barco.set(barco);
        },
        error: (error) => {
          this.error.set('Error al cargar barco: ' + error.message);
        }
      });
    }
  }

  saveBarco() {
    if (!this.barco().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const barco = this.barco();
    
    if (this.barcoId) {
      // Actualizar
      this.barcoService.updateBarco(this.barcoId, barco).subscribe({
        next: () => {
          this.saving.set(false);
          alert('Barco actualizado exitosamente');
        },
        error: (error) => {
          this.error.set('Error al actualizar barco: ' + error.message);
          this.saving.set(false);
        }
      });
    } else {
      // Crear
      this.barcoService.createBarco(barco).subscribe({
        next: () => {
          this.saving.set(false);
          this.barco.set(new Barco()); // Reset form
          alert('Barco creado exitosamente');
        },
        error: (error) => {
          this.error.set('Error al crear barco: ' + error.message);
          this.saving.set(false);
        }
      });
    }
  }
}
