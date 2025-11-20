import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SignupRequest } from '../../../models/dto/signup-request';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  signupRequest = signal(new SignupRequest('', '', '', 'JUGADOR'));
  loading = signal(false);
  error = signal<string | null>(null);
  passwordConfirm = signal('');

  private auth = inject(AuthService);
  private router = inject(Router);

  register() {
    // Validación simple
    if (!this.signupRequest().nombre || !this.signupRequest().email || !this.signupRequest().password) {
      this.error.set('Por favor completa todos los campos');
      return;
    }

    // Validar que las contraseñas coincidan
    if (this.signupRequest().password !== this.passwordConfirm()) {
      this.error.set('Las contraseñas no coinciden');
      return;
    }

    // Validar longitud mínima de contraseña
    if (this.signupRequest().password.length < 6) {
      this.error.set('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.auth.signup(this.signupRequest()).subscribe({
      next: (jwt) => {
        console.log('Registro exitoso:', jwt);
        this.loading.set(false);
        // Redirigir según el rol
        if (jwt.rol === 'ADMIN') {
          this.router.navigate(['/usuarios']);
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        console.error('Registro failed:', err);
        this.loading.set(false);
        
        // Manejo de errores específicos
        if (err.status === 409) {
          this.error.set('El email ya está registrado. Por favor usa otro email.');
        } else if (err.error?.message) {
          this.error.set(err.error.message);
        } else {
          this.error.set('Error al registrar el usuario. Por favor intenta de nuevo.');
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
