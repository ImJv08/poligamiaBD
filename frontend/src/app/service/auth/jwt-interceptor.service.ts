import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from "rxjs";
import { authService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class JwtInterceptorService implements HttpInterceptor {

  constructor(private authService: authService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token: String = this.authService.userToken;
    console.log("Token interceptor:", this.authService.userToken);
    if (token != null) {
      // 👇 SI es FormData (archivo), NO agregues Content-Type
      if (req.body instanceof FormData) {
        req = req.clone({
          setHeaders: {
            'Authorization': `Bearer ${token}`
          }
        });
      } else {
        // 👇 Si NO es FormData, agrega Content-Type JSON
        req = req.clone({
          setHeaders: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
      }
    }

    return next.handle(req);
  }
}
