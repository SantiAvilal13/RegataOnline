import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, Observable } from 'rxjs';
import { Barco, Usuario, Modelo } from '../../../models';
import { BarcoService } from '../../../shared/services/barcos/barco.service';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';
import { ModeloService } from '../../../shared/services/modelos/modelo.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-barco-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './barco-form.component.html',
  styleUrl: './barco-form.component.css'
})
export class BarcoFormComponent {
  barco = signal<Barco>(new Barco());
  usuarios = signal<Usuario[]>([]);
  modelos = signal<Modelo[]>([]);
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  isEditMode = signal(false);
  
  barcoService = inject(BarcoService);
  usuarioService = inject(UsuarioService);
  modeloService = inject(ModeloService);
  authService = inject(AuthService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit() {
    this.loadData();
    
    this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          this.isEditMode.set(true);
          return this.barcoService.getBarco(id);
        } else {
          // Modo creación - retornamos un observable vacío
          return new Observable<Barco>(subscriber => {
            subscriber.complete();
          });
        }
      })
    ).subscribe({
      next: (barco) => {
        // Validar que un JUGADOR solo pueda editar sus propios barcos
        if (this.isEditMode() && !this.authService.isAdmin()) {
          if (barco.usuarioId !== this.authService.idUsuario()) {
            this.error.set('No tienes permiso para editar este barco');
            setTimeout(() => this.router.navigate(['/barcos']), 2000);
            return;
          }
        }
        this.barco.set(barco);
      },
      error: (err) => {
        this.error.set('Error al cargar el barco: ' + err.message);
      }
    });
  }

  loadData() {
    this.loading.set(true);
    
    // Solo ADMIN puede ver la lista de usuarios para asignar barcos
    if (this.authService.isAdmin()) {
      this.usuarioService.getUsuarios().subscribe({
        next: (usuarios) => {
          this.usuarios.set(usuarios);
        },
        error: (error) => {
          console.error('Error al cargar usuarios:', error);
          // No mostrar error fatal, continuar con la carga
        }
      });
    }

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

  saveBarco() {
    if (!this.barco().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const barco = this.barco();
    
    // Si es JUGADOR y está creando un barco, asignar automáticamente su propio ID
    if (!this.authService.isAdmin() && !this.isEditMode()) {
      barco.usuarioId = this.authService.idUsuario()!;
    }
    
    const operation = this.isEditMode()
      ? this.barcoService.updateBarco(barco.idBarco!, barco)
      : this.barcoService.createBarco(barco);
    
    operation.subscribe({
      next: () => {
        this.saving.set(false);
        alert(this.isEditMode() ? 'Barco actualizado exitosamente' : 'Barco creado exitosamente');
        this.router.navigate(['/barcos']);
      },
      error: (error) => {
        this.error.set('Error al guardar barco: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
