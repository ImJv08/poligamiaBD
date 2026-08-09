// Refleja exactamente la tabla SOBRECUPO del modelo relacional.
import { Compra } from './compra.model';
import { Supervisor } from './supervisor.model';
import { Cliente } from './cliente.model';

export interface Sobrecupos {
  id?: number;
  idCompra: number;
  idSupervisor?: number;
  montoAutorizado: number;
  autorizadoCliente: boolean;
  aprobadoSupervisor: boolean;
  idClienteTitular: number;

}

