import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Usuario } from '../../../models';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './usuarios-list.component.html',
  styleUrl: './usuarios-list.component.css'
})
export class UsuariosListComponent {
  usuarios = signal<Usuario[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  usuarioService = inject(UsuarioService);

  ngOnInit() {
    this.loadUsuarios();
  }

  loadUsuarios() {
    this.loading.set(true);
    this.error.set(null);
    
    this.usuarioService.getUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
        this.loading.set(false);
      },
      error: (error) => {
        this.error.set('Error al cargar usuarios: ' + error.message);
        this.loading.set(false);
      }
    });
  }

  deleteUsuario(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
      this.usuarioService.deleteUsuario(id).subscribe({
        next: () => {
          this.loadUsuarios();
        },
        error: (error) => {
          this.error.set('Error al eliminar usuario: ' + error.message);
        }
      });
    }
  }
}
