import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, catchError, map, Observable, tap, throwError} from 'rxjs';
import { Usuario } from '../../model/usuario.model';


@Injectable({
  providedIn: 'root',
})
  export class authService {
  private apiUrl = 'http://localhost:8080/auth'
  currentUserLoginOn = new BehaviorSubject<boolean>(false);
  currentUserData = new BehaviorSubject<string>('');
  constructor(private http: HttpClient) {
    // Inicializa con el token guardado o cadena vacía
    this.currentUserData = new BehaviorSubject<string>(sessionStorage.getItem('token') || '');
  }
// Cuando el usuario hace login exitoso
  login(credentials: Usuario) {
    console.log("Usuario: " + credentials.correoElectronico)
    console.log("Contraseña: " + credentials.contrasenia);

    const loginPayload = {
      correoElectronico: credentials.correoElectronico,
      contrasenia: credentials.contrasenia
    };
    return this.http.post<any>(`${this.apiUrl}/login`, loginPayload).pipe(
      tap(userData => {
        console.log('userData completo:', userData);
        sessionStorage.setItem('token', userData.token);
        localStorage.setItem('rol', userData.rol);
        localStorage.setItem('id', userData.id);

        this.currentUserData.next(userData.token);
        this.currentUserLoginOn.next(true);
      }),
    map((userData) => userData.token),
      catchError((error) => this.handleError(error))
    );

}
//Cerrar sesion
longout():void{
  sessionStorage.removeItem('token');
  localStorage.removeItem('idUsuario');
  this.currentUserLoginOn.next(false);
}


  registrar(usuario: Usuario): Observable<string> {
    return this.http.post(`${this.apiUrl}/registrarCliente`, usuario, { responseType: 'text' });
  }

  private handleError(error:any) {
    if(error.status === 0) {
      console.error('se ha producido un error', error.error)
    }else {
      console.error(`Backend retornó el código de estado ${error.status}`, error.error);
    }
    return throwError(() => new Error('Algo fallo, por favor intente nuevamente') )
  }

  get userData():Observable<String> {
return this.currentUserData.asObservable();
  }

get userToken():String {
    return this.currentUserData.value;
}

  isAuthenticated(): boolean {
    return this.currentUserData.value !== '';
  }
  }

