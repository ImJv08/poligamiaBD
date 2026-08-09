import { Pareja } from './pareja.model';
import { Almacen } from './almacen.model';
import { Usuario } from './usuario.model';


export interface Supervisor extends Usuario{
  id?: number;
  almacen: Almacen;
}
