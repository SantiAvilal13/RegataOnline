import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, Observable } from 'rxjs';
import { Usuario } from '../../../models';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuario-form.component.html',
  styleUrl: './usuario-form.component.css'
})
export class UsuarioFormComponent {
  usuario = signal<Usuario>(new Usuario());
  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  isEditMode = signal(false);
  
  usuarioService = inject(UsuarioService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit() {
    this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          this.isEditMode.set(true);
          this.loading.set(true);
          return this.usuarioService.getUsuario(id);
        } else {
          // Modo creación - retornamos un observable vacío
          return new Observable<Usuario>(subscriber => {
            subscriber.complete();
          });
        }
      })
    ).subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el usuario: ' + err.message);
        this.loading.set(false);
      },
      complete: () => {
        // En modo creación, no hay datos que cargar
        this.loading.set(false);
      }
    });
  }

  saveUsuario() {
    // Validación específica según el modo
    if (this.isEditMode()) {

      if (!this.usuario().nombre || !this.usuario().email || !this.usuario().rol) {
        this.error.set('Por favor completa todos los campos requeridos');
        return;
      }
    } else {
     
      if (!this.usuario().isValid()) {
        this.error.set('Por favor completa todos los campos requeridos');
        return;
      }
    }

    this.saving.set(true);
    this.error.set(null);

    const usuario = this.usuario();
    const operation = this.isEditMode() 
      ? this.usuarioService.updateUsuario(usuario.idUsuario!, usuario)
      : this.usuarioService.createUsuario(usuario);
    
    operation.subscribe({
      next: () => {
        console.log('Confirmación de datos guardados:', {
          nombre: usuario.nombre,
          email: usuario.email,
          rol: usuario.rol,
          ...(usuario.idUsuario && { idUsuario: usuario.idUsuario })
        });
        this.saving.set(false);
        alert(this.isEditMode() ? 'Usuario actualizado exitosamente' : 'Usuario creado exitosamente');
        this.router.navigate(['/usuarios']);
      },
      error: (error) => {
        this.error.set('Error al guardar usuario: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
