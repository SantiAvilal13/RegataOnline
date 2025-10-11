import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../shared/services/usuarios/usuario.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { Usuario } from '../../../models';

@Component({
  selector: 'app-usuario-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './usuario-detail.component.html',
  styleUrl: './usuario-detail.component.css'
})
export class UsuarioDetailComponent {
  usuarioService = inject(UsuarioService);
  route = inject(ActivatedRoute);
  usuario = signal<Usuario | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loading.set(true);
    this.route.params.pipe(
      switchMap(params => this.usuarioService.getUsuario(params['id']))
    ).subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el usuario: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  
}

