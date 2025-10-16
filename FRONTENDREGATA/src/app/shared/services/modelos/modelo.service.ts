import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Modelo } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ModeloService {

  http = inject(HttpClient);

  getModelos(): Observable<Modelo[]> {
    return this.http.get<any[]>(environment.apiUrl + '/modelos').pipe(
      map(modelos => modelos.map(modelo => new Modelo(modelo)))
    );
  }

  getModelo(id: number): Observable<Modelo> {
    return this.http.get<any>(`${environment.apiUrl}/modelos/${id}`).pipe(
      map(modelo => new Modelo(modelo))
    );
  }

  createModelo(modelo: Modelo): Observable<Modelo> {
    return this.http.post<Modelo>(environment.apiUrl + '/modelos', modelo);
  }

  updateModelo(id: number, modelo: Modelo): Observable<Modelo> {
    return this.http.put<Modelo>(`${environment.apiUrl}/modelos/${id}`, modelo);
  }

  deleteModelo(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/modelos/${id}`);
  }
}