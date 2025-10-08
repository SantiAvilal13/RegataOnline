import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../../models';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuario-form.component.html',
  styleUrl: './usuario-form.component.css'
})
export class UsuarioFormComponent implements OnInit {
  usuario = signal<Usuario>(new Usuario());
  saving = signal(false);
  error = signal<string | null>(null);
  
  usuarioService = inject(UsuarioService);

  ngOnInit() {
    // Component initialization
  }

  saveUsuario() {
    if (!this.usuario().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const usuario = this.usuario();
    
    this.usuarioService.createUsuario(usuario).subscribe({
      next: () => {
        this.saving.set(false);
        this.usuario.set(new Usuario()); // Reset form
        alert('Usuario creado exitosamente');
      },
      error: (error) => {
        this.error.set('Error al crear usuario: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
