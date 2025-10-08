import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Barco } from '../../../models';

@Injectable({
  providedIn: 'root'
})
export class BarcoService {
  private apiUrl = 'http://localhost:8080/api/barcos';

  http = inject(HttpClient);

  getBarcos(): Observable<Barco[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(barcos => barcos.map(barco => new Barco(barco)))
    );
  }

  getBarco(id: number): Observable<Barco> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(barco => new Barco(barco))
    );
  }

  createBarco(barco: Barco): Observable<Barco> {
    return this.http.post<Barco>(this.apiUrl, barco);
  }

  updateBarco(id: number, barco: Barco): Observable<Barco> {
    return this.http.put<Barco>(`${this.apiUrl}/${id}`, barco);
  }

  deleteBarco(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchBarcos(alias: string): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${this.apiUrl}/buscar?alias=${alias}`);
  }

  getBarcosByUsuario(usuarioId: number): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  getBarcosByModelo(modeloId: number): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${this.apiUrl}/modelo/${modeloId}`);
  }
}