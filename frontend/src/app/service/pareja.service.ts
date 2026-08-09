// Ubicación sugerida: src/app/service/pareja.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Pareja} from '../model/pareja.model';

@Injectable({
  providedIn: 'root'
})
export class ParejaService {


  private readonly baseUrl = 'http://localhost:8080/parejaController';

  constructor(private http: HttpClient) { }

  mostrarParejas(idCliente: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/mostrarParejas/${idCliente}`
    );
  }

  obtenerParejas(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/obtenerParejas`
    );
  }

  obtenerParejaPorId(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/obtenerPorId/${id}`
    );
  }

  crearPareja(pareja: any): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/crearPareja`,
      pareja,
      {
        responseType: 'text'
      }
    );
  }

  actualizarPareja(id: number, pareja: any): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/actualizar/${id}`,
      pareja,
      {
        responseType: 'text'
      }
    );
  }

  eliminarPareja(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/eliminarPareja/${id}`
    );
  }

  cambiarContrasenia(id: number, contrasenia: string): Observable<any> {
    const body = {
      id: id.toString(),
      contrasenia: contrasenia
    };
    return this.http.put(`${this.baseUrl}/cambiarContrasenia`, body, { responseType: 'text' });
  }

  obtenerPorCorreo(correo: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/obtenerPorCorreo`, {
      params: { correo }
    });
  }

  contarPareja(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/contarPareja`, {})
  }

  obtenerParejasCliente(idCliente: number): Observable<Pareja[]> {
    console.log("Consultando parejas del cliente:", idCliente);
    return this.http.get<Pareja[]>(`${this.baseUrl}/cliente/${idCliente}`);
  }

  asignarCupoIndividual(idPareja: number, cupoIndividual: number): Observable<string> {

    return this.http.put(
      `${this.baseUrl}/${idPareja}/cupo?cupoIndividual=${cupoIndividual}`,
      {},
      {
        responseType: 'text'
      }
    );

  }

  filtrar(filtro: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/filtrar?filtro=${encodeURIComponent(filtro)}`
    );
  }
}
