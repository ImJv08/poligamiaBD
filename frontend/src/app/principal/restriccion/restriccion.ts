import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import {Restriccion} from '../../model/restriccion.model';
import {RestriccionService} from '../../service/restriccion.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {Parejas} from '../parejas/parejas';
import {Pareja} from '../../model/pareja.model';
import {ParejaService} from '../../service/pareja.service';
import {Cliente} from '../../model/cliente.model';
import {ClienteService} from '../../service/cliente.service';
import {Role} from '../../model/role.enum';
import {FormsModule} from '@angular/forms';
import {map} from 'rxjs';

@Component({
  selector: 'app-restriccion',
  standalone: true,
  templateUrl: './restriccion.html',
  imports: [
    NgClass,
    NgForOf,
    DatePipe,
    NgIf,
    FormsModule
  ],
  styleUrl: './restriccion.css'
})

export class Restricciones {
  constructor(private router: Router, private cdr: ChangeDetectorRef,
              private restriccionService: RestriccionService,
              private parejaService: ParejaService,
              private clienteService: ClienteService) { }

  restricciones: Restriccion[] = [];
  parejas: Pareja[] = [];
  clientes: Cliente[] = [];
  cargando = true;
  restriccionesActivas: number = 0;
  parejasRestringidas: number = 0;
  mostrarModal = false;
  mostrarModalEliminar = false;
  restriccionEditandoId: number | null = null;
  restriccionAEliminar: Restriccion | null = null;
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  modoEdicion = false;

  restriccion: {
    id?: number;
    parejaId: number;
    fecha: string;
    horaInicio: string;
    horaFin: string;
    activa: boolean;
    motivo: string
  } = this.restriccionVacia();

  restriccionVacia(): Restriccion {
    return {
      parejaId: 0,
      fecha: '',
      horaInicio: '',
      horaFin: '',
      activa: true,
      motivo: ''
    };
  }

  idUsuario: number = 0;
  rolUsuario = localStorage.getItem('rol');

  ngOnInit(): void {

    this.idUsuario = Number(localStorage.getItem('idUsuario'));

    console.log("idUsuario:", this.idUsuario);
    console.log("rol:", this.rolUsuario);

    let peticionParejas;

    if (this.rolUsuario === 'ADMINISTRADOR') {
      peticionParejas = this.parejaService.obtenerParejas();

    } else if (this.rolUsuario === 'CLIENTE') {
      peticionParejas = this.parejaService.obtenerParejasCliente(this.idUsuario);

    } else {
      peticionParejas = this.parejaService.obtenerParejaPorId(this.idUsuario).pipe(
        map(p => [p])
      );
    }

    peticionParejas.subscribe({

      next: (parejas) => {

        this.parejas = parejas;

        this.clienteService.obtenerClientes().subscribe({

          next: (clientes) => {

            this.clientes = clientes;

            let peticionRestricciones;

            if (this.rolUsuario === 'PAREJA') {
              peticionRestricciones =
                this.restriccionService.obtenerRestriccionesPareja(this.idUsuario);
            } else {
              peticionRestricciones =
                this.restriccionService.obtenerTodas();
            }

            peticionRestricciones.subscribe({

              next: (restricciones) => {

                if (this.rolUsuario === 'ADMINISTRADOR' ||
                  this.rolUsuario === 'PAREJA') {

                  this.restricciones = restricciones;

                } else {

                  const idsParejas = this.parejas.map(p => p.id);

                  this.restricciones = restricciones.filter(r =>
                    idsParejas.includes(r.parejaId)
                  );
                }

                const idsParejasUnicas = new Set(
                  this.restricciones.map(r => r.parejaId)
                );

                this.parejasRestringidas = idsParejasUnicas.size;
                this.cargando = false;
                this.cdr.detectChanges();
              },

              error: (err) => {
                console.error(err);
                this.cargando = false;
              }

            });

          },

          error: (err) => console.error(err)

        });

      },

      error: (err) => console.error(err)

    });

    this.restriccionService.contarActivas().subscribe({
      next: (data) => {
        this.restriccionesActivas = data;
        this.cdr.detectChanges();
      }
    });

  }


  obtenerPareja(parejaId: number): Pareja | undefined {
    return this.parejas.find(p => p.id === parejaId);
  }

  obtenerCliente(idCliente: number): Cliente | undefined {
    return this.clientes.find(c => c.id === idCliente);
  }

  nombrePareja(pareja: Pareja): string{
    const cliente = this.obtenerCliente(pareja.idCliente);
    const nombreCliente = cliente ? `${cliente.primerNombre} ${cliente.primerApellido}` : 'Cliente desconocido';
    return `${pareja.primerNombre} ${pareja.primerApellido} (Titular: ${nombreCliente})`;
  }

  onCrear(){
    console.log("Entraaa");
    if (this.restriccion.parejaId === 0 || this.restriccion.fecha === '' ||
      this.restriccion.horaInicio === '' || this.restriccion.horaFin === '') {
      this.mostrarMensaje("Todos los campos son obligatorios", "error");
      return;
    }

    if(this.modoEdicion){
      this.restriccionService.actualizarRestriccion(this.restriccionEditandoId!, this.restriccion).subscribe({
        next: (response: any) => {
          console.log("Respuesta actualizacion OK:", response);
          this.mostrarMensaje("Restriccion Actualizada correctamente", "success");
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err: any) => {
          if(err.status === 409){
            this.mostrarMensaje("Ya existe una restriccion en ese horario para esa fecha", "error");
          } else {
            this.mostrarMensaje("Error al crear la restriccion", "error")
          }
        }
      });
    } else {
      this.restriccionService.crearRestriccion(this.restriccion).subscribe({
        next: (response: any) => {
          this.mostrarMensaje(response, 'success');
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err: any) => {
          if(err.status === 409){
            this.mostrarMensaje("Ya existe una restriccion en ese horario para esa fecha", "error");
          } else if(err.status === 400){
            this.mostrarMensaje("Horario inválido (inicio > fin)", "error");
          } else {
            this.mostrarMensaje("Error al crear la restricción", "error");
          }
        }
      });
    }
  }

  eliminar(restriccion: Restriccion): void {
    this.restriccionAEliminar = restriccion;
    this.mostrarModalEliminar = true;
  }

  cerrarModalEliminar(): void{
    this.mostrarModalEliminar = false;
    this.restriccionAEliminar = null;
  }

  confirmarEliminar(): void{
    if(!this.restriccionAEliminar) return;

    this.restriccionService.eliminarRestriccion(this.restriccionAEliminar.id!).subscribe({
      next: () => {
        this.mostrarMensaje("Restriccion eliminada correctamente", "success");
        this.cerrarModalEliminar();
        this.ngOnInit();
      },
      error: (err) => {
        console.log("Error al eliminar", err);
        this.mostrarMensaje("Error al eliminar la restriccion", "error");
        this.cerrarModalEliminar();
      }
    });

  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error', duracionMs: number = 3000): void {
    this.message = texto;
    this.messageType = tipo
    this.cdr.detectChanges();

    setTimeout(() => {
      this.clearMessage();
    }, duracionMs);
  }

  clearMessage() {
    this.message = '';
    this.messageType = null;
  }

  abrirModal() {
    this.modoEdicion = false;
    this.restriccionEditandoId = null;
    this.restriccion = {
      parejaId: 0,
      fecha: '',
      horaInicio: '',
      horaFin: '',
      activa: true,
      motivo: ''
    };
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }
  editar(restriccion: Restriccion): void {
    this.modoEdicion = true;
    this.restriccionEditandoId = restriccion.id!;
    this.restriccion = { ...restriccion, motivo: ''};
    this.mostrarModal = true;
  }

}
