import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TitularCupo } from '../model/cupo.model';

@Injectable({
  providedIn: 'root'
})
export class CupoService {

  private baseUrl = 'http://localhost:8081/cupos'; // ajusta a tu backend real

  constructor(private http: HttpClient) {}

  // Devuelve cada titular con su cupo total y el detalle de cupos de sus parejas-admin
  obtenerTitularesConCupos(): Observable<TitularCupo[]> {
    return this.http.get<TitularCupo[]>(`${this.baseUrl}/titulares`);
  }

  // Solo lectura: obtener el desglose de un titular puntual
  obtenerDesgloseTitular(idTitular: number): Observable<TitularCupo> {
    return this.http.get<TitularCupo>(`${this.baseUrl}/titulares/${idTitular}`);
  }

  // Editar el cupo TOTAL del titular (esto sí lo puede hacer el Supervisor)
  actualizarCupoTotal(idTitular: number, nuevoCupoTotal: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/titulares/${idTitular}/cupo-total`, {
      cupoTotal: nuevoCupoTotal
    });
  }

}
