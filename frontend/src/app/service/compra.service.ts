import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Compra} from '../model/compra.model';

@Injectable({
  providedIn: 'root'
})
export class CompraService {

  private baseUrl = 'http://localhost:8080/compraController';

  constructor(private http: HttpClient) {}

  registrarCompra(idPareja: number, idAlmacen: number, montoTransaccion: number): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/registrar/${idPareja}/${idAlmacen}/${montoTransaccion}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  actualizarEstadoCompra(idCompra: number, idSobrecupo: number, idPareja: number): Observable<string> {
    return this.http.put(
      `${this.baseUrl}/actualizarEstado/${idCompra}/${idSobrecupo}/${idPareja}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  filtrarCompras(
    cliente?: string,
    pareja?: string,
    almacen?: string,
    fechaDesde?: string,
    fechaHasta?: string,
    estado?: string
  ): Observable<any[]> {

    let params: any = {};

    if (cliente) params.cliente = cliente;
    if (pareja) params.pareja = pareja;
    if (almacen) params.almacen = almacen;
    if (fechaDesde) params.fechaDesde = fechaDesde;
    if (fechaHasta) params.fechaHasta = fechaHasta;
    if (estado) params.estado = estado;

    return this.http.get<any[]>(
      `${this.baseUrl}/filtrarCompra`,
      { params }
    );
  }

  comprasHoy(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/comprasHoy`, {})
  }

  obtenerTodas(): Observable<Compra[]> {
    return this.http.get<Compra[]>(`${this.baseUrl}/obtenerCompras`);
  }

  obtenerComprasCliente(idCliente: number): Observable<Compra[]> {
    return this.http.get<Compra[]>(`${this.baseUrl}/cliente/${idCliente}`);
  }

  obtenerComprasPareja(idPareja: number): Observable<Compra[]> {
    return this.http.get<Compra[]>(`${this.baseUrl}/pareja/${idPareja}`);
  }

  actualizarCompra(id: number, compra: Compra): Observable<any> {
    return this.http.put(
      `${this.baseUrl}/actualizarEstado/${id}`,
      compra,
      { responseType: 'text' }
    );
  }

  eliminarCompra(idCompra: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/eliminar/${idCompra}`,
      { responseType: 'text' }
    );
  }
}
