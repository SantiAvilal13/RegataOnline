import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Partida } from '../../../models';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {
  private apiUrl = 'http://localhost:8080/api/partidas';

  http = inject(HttpClient);

  getPartidas(): Observable<Partida[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(partidas => partidas.map(partida => new Partida(partida)))
    );
  }

  getPartida(id: number): Observable<Partida> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(partida => new Partida(partida))
    );
  }

  createPartida(partida: Partida): Observable<Partida> {
    return this.http.post<Partida>(this.apiUrl, partida);
  }

  updatePartida(id: number, partida: Partida): Observable<Partida> {
    return this.http.put<Partida>(`${this.apiUrl}/${id}`, partida);
  }

  deletePartida(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}