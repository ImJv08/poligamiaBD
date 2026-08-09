import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Restriccion} from '../model/restriccion.model';

@Injectable({
  providedIn: 'root'
})
export class SobrecupoService {

  private baseUrl = 'http://localhost:8080/sobrecupoController';

  constructor(private http: HttpClient) {}

  mostrarSobrecupos(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/mostrarSobrecupo`
    );
  }

  estadoSobrecupo(idSobrecupo: number): Observable<string> {
    return this.http.get(
      `${this.baseUrl}/sobrecupo/${idSobrecupo}/estado`,
      {
        responseType: 'text'
      }
    );
  }

  autorizarSobrecupo(
    idSobrecupo: number,
    idCliente: number,
    montoAdicional?: number
  ): Observable<string> {

    let url = `${this.baseUrl}/sobrecupo/${idSobrecupo}/autorizar/${idCliente}`;

    if (montoAdicional != null) {
      url += `?montoAdicional=${montoAdicional}`;
    }

    return this.http.put(
      url,
      {},
      {
        responseType: 'text'
      }
    );
  }

  denegarSobrecupo(idSobrecupo: number, idCliente: number): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/sobrecupo/${idSobrecupo}/denegar/${idCliente}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  aprobarSobrecupo(idSobrecupo: number, idSupervisor: number): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/sobrecupo/${idSobrecupo}/aprobar/${idSupervisor}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  cancelarSobrecupo(idSobrecupo: number, idSupervisor: number): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/sobrecupo/${idSobrecupo}/cancelar/${idSupervisor}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  contarSobrecupo(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/contarSobrecupo`, {})
  }

  obtenerTodas(): Observable<Restriccion[]> {
    return this.http.get<Restriccion[]>(`${this.baseUrl}/obtenerSobrecupo`);
  }
}
