import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Movimiento } from '../../../models';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class MovimientoJuegoService {
  http = inject(HttpClient);

  realizarMovimiento(participacionId: number, deltaVx: number, deltaVy: number): Observable<Movimiento> {
    const url = `${environment.apiUrl}/movimientos/participacion/${participacionId}/mover?deltaVx=${deltaVx}&deltaVy=${deltaVy}`;
    console.log('URL del movimiento:', url);
    
    return this.http.post<any>(url, null).pipe(
      map(movimiento => new Movimiento(movimiento))
    );
  }

  obtenerEstadoActual(participacionId: number): Observable<Movimiento> {
    return this.http.get<any>(
      `${environment.apiUrl}/movimientos/participacion/${participacionId}/estado-actual`
    ).pipe(
      map(movimiento => new Movimiento(movimiento))
    );
  }

  obtenerHistorialCompleto(participacionId: number): Observable<Movimiento[]> {
    return this.http.get<any[]>(
      `${environment.apiUrl}/movimientos/participacion/${participacionId}/historial`
    ).pipe(
      map(movimientos => movimientos.map(movimiento => new Movimiento(movimiento)))
    );
  }

  obtenerDestinosPosibles(participacionId: number): Observable<any[]> {
    return this.http.get<any>(
      `${environment.apiUrl}/movimientos/participacion/${participacionId}/destinos-posibles`
    ).pipe(
      map(response => response.destinos || [])
    );
  }
}
