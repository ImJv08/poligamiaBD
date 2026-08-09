import {HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class SupervisorService {
  private readonly baseUrl = 'http://localhost:8080/supervisorController';

  constructor(private http: HttpClient) { }

  obtenerSupervisores(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/mostrarTodo`
    );
  }

  obtenerSupervisorPorId(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/obtenerPorId/${id}`
    );
  }

  crearSupervisor(supervisor: any): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/crear`,
      supervisor,
      {
        responseType: 'text'
      }
    );
  }

  actualizarSupervisor(id: number, supervisor: any): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/actualizar/${id}`,
      supervisor,
      {
        responseType: 'text'
      }
    );
  }

  eliminarSupervisor(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/eliminar/${id}`
    );
  }

  obtenerPorCorreo(correo: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/obtenerPorCorreo`, {
      params: { correo }
    });
  }

  filtrar(filtro: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/filtrar?filtro=${encodeURIComponent(filtro)}`
    );
  }

}
