import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdministradorService {

  private readonly baseUrl = 'http://localhost:8080/administradorController';

  constructor(private http: HttpClient) {}

  obtenerPorCorreo(correo: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/obtenerPorCorreo`, {
      params: { correo }
    });
  }
}
