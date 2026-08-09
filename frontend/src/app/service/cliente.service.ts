// Ubicación sugerida: src/app/service/cliente.service.ts
// Servicio mínimo: solo lo necesario para alimentar el selector de "cliente titular"
// en el componente de Parejas. Amplíalo cuando construyas el CRUD propio de Clientes.

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Cliente} from '../model/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private readonly baseUrl = 'http://localhost:8080/clienteController';

  constructor(private http: HttpClient) { }

  obtenerClientes(): Observable<any[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({'Authorization': `Bearer ${token}`});
    return this.http.get<any[]>(
      `${this.baseUrl}/obtenerCliente`, {headers});
  }

  crearCliente(cliente: any): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/crearCliente`,
      cliente,
      {
        responseType: 'text'
      }
    );
  }

  actualizarCliente(id: number, cliente: any): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/actualizarCliente/${id}`,
      cliente,
      {
        responseType: 'text'
      }
    );
  }

  eliminarCliente(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/eliminarCliente/${id}`
    );
  }

  asignarCupoTotal(idCliente: number, cupoTotal: number): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/cliente/${idCliente}/cupo?cupoTotal=${cupoTotal}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  mostrarParejas(idCliente: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/mostrarParejas/${idCliente}`
    );
  }

  obtenerPorCorreo(correo: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/obtenerPorCorreo`, {
      params: { correo }
    });
  }

  contarCliente(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/contarCliente`, {})
  }

  obtenerClientePorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${id}`);
  }
}
