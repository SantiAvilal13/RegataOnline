import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * Interceptor funcional que maneja errores HTTP
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP Error intercepted:', error);
      
      // Manejo específico de errores de autenticación/autorización
      if (error.status === 401) {
        // Token inválido o expirado - logout y redirigir a login
        console.error('Error 401: No autenticado. Redirigiendo a login...');
        authService.logout();
        router.navigateByUrl('/login');
      } else if (error.status === 403) {
        // Sin permisos para acceder al recurso
        console.error('Error 403: Acceso denegado. No tienes permisos para esta acción.');
        // NO redirigir automáticamente - dejar que el componente maneje el error
      }

      return throwError(() => error);
    })
  );
};
