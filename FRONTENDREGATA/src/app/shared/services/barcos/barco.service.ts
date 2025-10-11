import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Barco } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class BarcoService {

  http = inject(HttpClient);

  getBarcos(): Observable<Barco[]> {
    return this.http.get<any[]>(environment.apiUrl + '/barcos').pipe(
      map(barcos => barcos.map(barco => new Barco(barco)))
    );
  }

  getBarco(id: number): Observable<Barco> {
    return this.http.get<any>(`${environment.apiUrl}/barcos/${id}`).pipe(
      map(barco => new Barco(barco))
    );
  }

  createBarco(barco: Barco): Observable<Barco> {
    return this.http.post<Barco>(environment.apiUrl + '/barcos', barco);
  }

  updateBarco(id: number, barco: Barco): Observable<Barco> {
    return this.http.put<Barco>(`${environment.apiUrl}/barcos/${id}`, barco);
  }

  deleteBarco(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/barcos/${id}`);
  }

  searchBarcos(alias: string): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${environment.apiUrl}/barcos/buscar?alias=${alias}`);
  }

  getBarcosByUsuario(usuarioId: number): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${environment.apiUrl}/barcos/usuario/${usuarioId}`);
  }

  getBarcosByModelo(modeloId: number): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${environment.apiUrl}/barcos/modelo/${modeloId}`);
  }
}