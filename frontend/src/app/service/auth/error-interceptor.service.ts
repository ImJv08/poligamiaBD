import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';

@Injectable()
export class ErrorInterceptorService implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Retorna la petición original, encadenando el operador catchError
    return next.handle(request).pipe(
      catchError((error) => {
        // Manda el error a la consola
        console.log(error);

        // Retorna la excepción (el error)
        return throwError(() => error);
      })
    );
  }
}

