import { Routes } from '@angular/router';
import {Login} from './login/login';
import {Registro} from './registro/registro';
import {Principal} from './principal/principal';
import {Clientes} from './principal/clientes/clientes';
import {Compras} from './principal/compras/compras';
import {Cupos} from './principal/cupos/cupos';
import {Almacenes} from './principal/almacenes/almacenes';
import {HistorialCompras} from './principal/historial-compra/historial-compra';
import {Parejas} from './principal/parejas/parejas';
import {Restricciones} from './principal/restriccion/restriccion';
import {Sobrecupo} from './principal/sobrecupo/sobrecupo';
import {Supervisores} from './principal/supervisores/supervisores';
import {AnadirCupo} from './principal/AñadirCupo/anadirCupo';
import {AsignarCupoPareja} from './principal/asignarCupoPareja/asignarCupoPareja';

export const routes: Routes = [
  {path: '', component: Login},
  {path: 'registro-cliente', component: Registro},
  {
    path: 'pagina-principal',
    component: Principal,

    children: [

      {
        path: '',
        redirectTo: 'cupos',
        pathMatch: 'full'
      },

      {
        path: 'clientes',
        component: Clientes
      },

      {
        path: 'compras',
        component: Compras
      },

      {
        path: 'cupos',
        component: Cupos
      },

      {
        path: 'almacenes',
        component: Almacenes
      },

      {
        path: 'historial-compras',
        component: HistorialCompras
      },

      {
        path: 'parejas',
        component: Parejas
      },

      {
        path: 'restriccion',
        component: Restricciones
      },

      {
        path: 'sobrecupo',
        component: Sobrecupo
      },

      {
        path: 'supervisores',
        component: Supervisores
      },
      {
        path: 'anadirCupo',
        component: AnadirCupo,
      },
      {
        path: 'asignarCupoPareja',
        component: AsignarCupoPareja,
      },

    ]
  }
];


