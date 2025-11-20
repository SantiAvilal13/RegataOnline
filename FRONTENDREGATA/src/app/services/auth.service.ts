import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginDto } from '../models/dto/login-dto';
import { SignupRequest } from '../models/dto/signup-request';
import { JwtAuthenticationResponse } from '../models/dto/jwt-authentication-response';

const JWT_TOKEN = 'jwt-token';
const EMAIL = 'user-email';
const ROLE = 'user-role';
const NOMBRE = 'user-nombre';
const ID_USUARIO = 'user-id';

/**
 * Servicio de autenticación con enfoque funcional (Angular 20)
 * Usa inject() y sessionStorage
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  http = inject(HttpClient);
  
  /**
   * Registro de nuevo usuario
   */
  signup(signupRequest: SignupRequest): Observable<JwtAuthenticationResponse> {
    return this.http
      .post<JwtAuthenticationResponse>(`${environment.apiUrl}/auth/signup`, signupRequest)
      .pipe(
        map((jwt) => {
          // Guardar datos en sessionStorage igual que en login
          sessionStorage.setItem(JWT_TOKEN, jwt.token);
          sessionStorage.setItem(EMAIL, jwt.email);
          sessionStorage.setItem(ROLE, jwt.rol); // Cambiado de jwt.role a jwt.rol
          sessionStorage.setItem(NOMBRE, jwt.nombre);
          sessionStorage.setItem(ID_USUARIO, jwt.idUsuario.toString());
          return jwt;
        })
      );
  }

  /**
   * Login de usuario
   */
  login(loginDto: LoginDto): Observable<JwtAuthenticationResponse> {
    return this.http
      .post<JwtAuthenticationResponse>(`${environment.apiUrl}/auth/login`, loginDto)
      .pipe(
        map((jwt) => {
          sessionStorage.setItem(JWT_TOKEN, jwt.token);
          sessionStorage.setItem(EMAIL, jwt.email);
          sessionStorage.setItem(ROLE, jwt.rol); // Cambiado de jwt.role a jwt.rol
          sessionStorage.setItem(NOMBRE, jwt.nombre);
          sessionStorage.setItem(ID_USUARIO, jwt.idUsuario.toString());
          return jwt;
        })
      );
  }
  
  /**
   * Logout de usuario
   */
  logout() {
    sessionStorage.removeItem(JWT_TOKEN);
    sessionStorage.removeItem(EMAIL);
    sessionStorage.removeItem(ROLE);
    sessionStorage.removeItem(NOMBRE);
    sessionStorage.removeItem(ID_USUARIO);
  }
  
  /**
   * Verifica si el usuario está autenticado
   */
  isAuthenticated() {
    return sessionStorage.getItem(JWT_TOKEN) != null;
  }
  
  /**
   * Obtiene el token JWT
   */
  token() {
    return sessionStorage.getItem(JWT_TOKEN);
  }
  
  /**
   * Obtiene el rol del usuario
   */
  role() {
    return sessionStorage.getItem(ROLE);
  }
  
  /**
   * Obtiene el email del usuario
   */
  email() {
    return sessionStorage.getItem(EMAIL);
  }
  
  /**
   * Obtiene el nombre del usuario
   */
  nombre() {
    return sessionStorage.getItem(NOMBRE);
  }
  
  /**
   * Obtiene el ID del usuario
   */
  idUsuario() {
    const id = sessionStorage.getItem(ID_USUARIO);
    return id ? parseInt(id) : null;
  }
  
  /**
   * Verifica si el usuario es ADMIN
   */
  isAdmin() {
    return this.role() === 'ADMIN';
  }
  
  /**
   * Verifica si el usuario es JUGADOR
   */
  isJugador() {
    return this.role() === 'JUGADOR';
  }
}
