import { Cliente } from './cliente.model';
import { Usuario } from './usuario.model';

export interface Pareja extends Usuario {
  id?: number;
  cupoAsignado: number;
  idCliente: number;
  primeraVez: boolean;
}
