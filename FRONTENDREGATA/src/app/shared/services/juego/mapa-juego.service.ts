import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Mapa } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class MapaJuegoService {
  http = inject(HttpClient);

  getMapas(): Observable<Mapa[]> {
    return this.http.get<any[]>(environment.apiUrl + '/mapas').pipe(
      map(mapas => mapas.map(mapa => new Mapa(mapa)))
    );
  }

  getMapa(id: number): Observable<Mapa> {
    return this.http.get<any>(`${environment.apiUrl}/mapas/${id}`).pipe(
      map(mapa => new Mapa(mapa))
    );
  }

  getMapaConCeldas(id: number): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/mapas/${id}/celdas`);
  }

  getMapaMatriz(id: number): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/mapas/${id}/matriz`);
  }
}
