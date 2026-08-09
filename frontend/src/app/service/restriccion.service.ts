import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Restriccion} from '../model/restriccion.model';

@Injectable({
  providedIn: 'root'
})
export class RestriccionService {

  private readonly baseUrl = 'http://localhost:8080/restriccionesController';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<Restriccion[]> {
    return this.http.get<Restriccion[]>(`${this.baseUrl}/mostrarTodo`);
  }

  contarActivas(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/contarActivas`);
  }

  eliminarRestriccion(id: number): Observable<any>{
    return this.http.delete(`${this.baseUrl}/eliminar/${id}`);
  }

  crearRestriccion(restriccion: any): Observable<String>{
    return this.http.post(
      `${this.baseUrl}/crearRestriccion`,
      restriccion,
      {
        responseType: "text"
      }
    );
  }

  actualizarRestriccion(id: number, restriccion: any): Observable<string>{
    return this.http.put(`${this.baseUrl}/actualizar/${id}`,
      restriccion,
      {
        responseType: "text"
      });
  }

  obtenerRestriccionesPareja(idPareja: number): Observable<Restriccion[]> {
    return this.http.get<Restriccion[]>(
      `${this.baseUrl}/pareja/${idPareja}`
    );
  }

}
