import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Usuario } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {


  http = inject(HttpClient);

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<any[]>(environment.apiUrl + '/usuarios').pipe(
      map(usuarios => usuarios.map(usuario => new Usuario(usuario)))
    );
  }

  getUsuario(id: number): Observable<Usuario> {
    return this.http.get<any>(`${environment.apiUrl}/usuarios/${id}`).pipe(
      map(usuario => new Usuario(usuario))
    );
  }

  createUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(environment.apiUrl + '/usuarios', usuario);
  }

  updateUsuario(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(
      `${environment.apiUrl}/usuarios/${id}`,
       usuario,
       { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) });
  }

  deleteUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/usuarios/${id}`);
  }
}
