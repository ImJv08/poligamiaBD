/*
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  constructor(private http: HttpClient) {}
  private baseUrl = 'http://localhost:8080/usuario';

  // Opción por query params (no recomendada para producción: expone la contraseña en la URL)
  crearUsuario(usuario: UsuarioDTO): Observable<any> {
    const params = new HttpParams()
      .set('nombre', usuario.nombre)
      .set('correo', usuario.correo)
      .set('telefono', usuario.telefono)
      .set('contrasena', usuario.contrasena)
      .set('rol', usuario.rol);

    return this.http.post(`${this.baseUrl}/crear`, null, {
      params: params,
      responseType: 'text'
    });
  }

  // Opción recomendada: cuerpo JSON, la contraseña viaja en el body, no en la URL
  crearUsuarioJSON(usuario: UsuarioDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/createjson`, usuario, {
      responseType: 'text'
    });
  }

  login(credenciales: UsuarioLogin): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/checklogin`, credenciales);
  }

  verificarCuenta(token: number): Observable<any> {
    const params = new HttpParams().set('token', token.toString());
    return this.http.get(`${this.baseUrl}/verificar`, {
      params: params,
      responseType: 'text'
    });
  }

  obtenerTodosUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/getall`);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/deletebyid/${id}`, {
      responseType: 'text'
    });
  }
}
*/
