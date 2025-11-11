import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LoginDto } from '../../../models/dto/login-dto';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginDto = signal(new LoginDto('', ''));
  loading = signal(false);
  error = signal<string | null>(null);

  private auth = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {
    // Hacer logout al entrar al componente de login
    this.auth.logout();
  }

  login() {
    // Validación simple
    if (!this.loginDto().email || !this.loginDto().password) {
      this.error.set('Por favor ingresa email y contraseña');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.auth.login(this.loginDto()).subscribe({
      next: (jwt) => {
        console.log('Login exitoso:', jwt);
        this.loading.set(false);
        // Redirigir según el rol
        if (jwt.rol === 'ADMIN') {
          this.router.navigate(['/usuarios']);
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.loading.set(false);
        this.error.set('Credenciales inválidas. Por favor verifica tu email y contraseña.');
      }
    });
  }
}
