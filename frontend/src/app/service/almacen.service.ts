import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AlmacenService {
  private readonly baseUrl = 'http://localhost:8080/almacenesController';

  constructor(private http: HttpClient) {}

  obtenerAlmacenes(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/obtenerAlmacen`
    );
  }

  obtenerAlmacenPorId(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/obtenerPorId/${id}`
    );
  }

  crearAlmacen(almacen: any): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/crearAlmacen`,
      almacen,
      {
        responseType: 'text'
      }
    );
  }

  actualizarAlmacen(id: number, almacen: any): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/actualizarAlmacen/${id}`,
      almacen,
      {
        responseType: 'text'
      }
    );
  }

  eliminarAlmacen(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/eliminarAlmacen/${id}`
    );
  }

  filtrar(filtro: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/filtrar?filtro=${encodeURIComponent(filtro)}`
    );
  }

}
