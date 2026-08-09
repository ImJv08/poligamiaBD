import { Role } from './role.enum';


export interface Administrador {
  primerNombre: string;
  segundoNombre: string;
  primerApellido: string;
  segundoApellido: string;
  numeroDocumento: string;
  correoElectronico: string;
  contrasenia: string;
  role: Role;
}
