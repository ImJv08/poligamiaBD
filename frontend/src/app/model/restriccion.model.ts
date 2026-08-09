import { Pareja } from './pareja.model';

export interface Restriccion {
  id?: number;
  parejaId: number;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  activa: boolean;
  motivo: string;
}

