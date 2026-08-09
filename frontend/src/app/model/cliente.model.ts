import {Usuario} from './usuario.model';

export interface Cliente extends Usuario{
  id?: number;
  cupoTotal: number;
  fechaRegistro: string;
}
