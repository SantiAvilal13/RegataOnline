import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Partida } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {

  http = inject(HttpClient);

  getPartidas(): Observable<Partida[]> {
    return this.http.get<any[]>(environment.apiUrl + '/partidas').pipe(
      map(partidas => partidas.map(partida => new Partida(partida)))
    );
  }

  getPartida(id: number): Observable<Partida> {
    return this.http.get<any>(`${environment.apiUrl}/partidas/${id}`).pipe(
      map(partida => new Partida(partida))
    );
  }

  createPartida(partida: Partida): Observable<Partida> {
    return this.http.post<Partida>(environment.apiUrl + '/partidas', partida);
  }

  createPartidaAndPlay(partida: Partida): Observable<any> {
    return this.http.post<any>(environment.apiUrl + '/partidas/crear-y-jugar', partida);
  }

  updatePartida(id: number, partida: Partida): Observable<Partida> {
    return this.http.put<Partida>(`${environment.apiUrl}/partidas/${id}`, partida);
  }

  deletePartida(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/partidas/${id}`);
  }

  // ===== MÉTODOS MULTIJUGADOR =====

  obtenerInfoJugadores(partidaId: number): Observable<any> {
    return this.http.get(`${environment.apiUrl}/partidas/${partidaId}/info-jugadores`);
  }

  listarPartidasDisponibles(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/partidas/disponibles`);
  }

  unirseAPartida(partidaId: number, usuarioId: number, barcoId: number): Observable<any> {
    return this.http.post(`${environment.apiUrl}/partidas/${partidaId}/unirse`, null, {
      params: { 
        usuarioId: usuarioId.toString(), 
        barcoId: barcoId.toString() 
      }
    });
  }

  iniciarPartida(partidaId: number): Observable<any> {
    return this.http.post(`${environment.apiUrl}/partidas/${partidaId}/iniciar`, null);
  }

  crearPartidaYEsperar(partidaData: any, usuarioId: number, barcoId: number): Observable<any> {
    return this.http.post(`${environment.apiUrl}/partidas/crear-y-esperar`, partidaData, {
      params: {
        usuarioId: usuarioId.toString(),
        barcoId: barcoId.toString()
      }
    });
  }

  obtenerEstadoCompleto(partidaId: number): Observable<any> {
    return this.http.get(`${environment.apiUrl}/partidas/${partidaId}/estado-completo`);
  }
}