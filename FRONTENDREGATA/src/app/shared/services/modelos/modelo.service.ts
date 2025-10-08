import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Modelo } from '../../../models';

@Injectable({
  providedIn: 'root'
})
export class ModeloService {
  private apiUrl = 'http://localhost:8080/api/modelos';

  http = inject(HttpClient);

  getModelos(): Observable<Modelo[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(modelos => modelos.map(modelo => new Modelo(modelo)))
    );
  }

  getModelo(id: number): Observable<Modelo> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(modelo => new Modelo(modelo))
    );
  }

  createModelo(modelo: Modelo): Observable<Modelo> {
    return this.http.post<Modelo>(this.apiUrl, modelo);
  }

  updateModelo(id: number, modelo: Modelo): Observable<Modelo> {
    return this.http.put<Modelo>(`${this.apiUrl}/${id}`, modelo);
  }

  deleteModelo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}