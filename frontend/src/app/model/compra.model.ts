import { Pareja } from './pareja.model';
import { Almacen } from './almacen.model';

export type EstadoCompra = 'APROBAD0' | 'RECHAZADO' | 'PENDIENTE';


export interface Compra {
  id?: number;
  idPareja: number;
  idAlmacen: number;
  montoTransaccion: number;
  fecha: string;
  hora: string;
  estado: EstadoCompra;
}
