import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import {ParejaService} from '../../service/pareja.service';
import {Pareja} from '../../model/pareja.model';
import {Role} from '../../model/role.enum';
import {CurrencyPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {Cliente} from '../../model/cliente.model';
import {ClienteService} from '../../service/cliente.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-parejas',
  standalone: true,
  templateUrl: './parejas.html',
  imports: [
    CurrencyPipe,
    NgForOf,
    NgIf,
    FormsModule,
    NgClass
  ],
  styleUrl: './parejas.css'
})

export class Parejas {
  constructor(private router: Router, private parejaService: ParejaService,
              private cdr: ChangeDetectorRef,
              private clienteService: ClienteService) { }

pareja: Pareja = {
  primerNombre: ' ',
  segundoNombre: ' ',
  primerApellido: ' ',
  segundoApellido: ' ',
  numeroDocumento: ' ',
  correoElectronico: ' ',
  contrasenia: ' ',
  role: Role.PAREJA,
  cupoAsignado: 0,
  idCliente: 0,
  primeraVez: true
}

parejaVacia(): Pareja{
  return {
    primerNombre: '',
    segundoNombre: '',
    primerApellido: '',
    segundoApellido: '',
    numeroDocumento: '',
    correoElectronico: '',
    contrasenia: ' ',
    role: Role.PAREJA,
    idCliente: this.rolUsuario === 'CLIENTE' ? this.idUsuario : 0,
    primeraVez: true,
    cupoAsignado: 0
  };
}

  filtro: string = '';

  parejas: Pareja[] = []

  clientes: Cliente[] = []
  parejasRegistradas = 0;
  cargando = true;
  idUsuario: number = 0;
  rolUsuario = localStorage.getItem('rol');
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  modoEdicion = false;
  parejaEditandoId: number | null = null;
  mostrarModalEliminar = false;
  parejaAEliminar: Pareja | null = null;
  mostrarModal = false;

  ngOnInit(): void {

    this.cargarParejas();

    this.rolUsuario = localStorage.getItem('rol');
    this.idUsuario = Number(localStorage.getItem('idUsuario'));

    if (this.rolUsuario === 'ADMINISTRADOR') {
      this.clienteService.obtenerClientes().subscribe(data => {
        this.clientes = data;
        this.cdr.detectChanges();
      });
    }

    console.log("idUsuario:", this.idUsuario);
    console.log("rol:", this.rolUsuario);

    const peticionParejas = this.rolUsuario === 'ADMINISTRADOR'
      ? this.parejaService.obtenerParejas()
      : this.parejaService.obtenerParejasCliente(this.idUsuario);

    peticionParejas.subscribe({
      next: (parejas) => {

        this.parejas = parejas;
        this.parejasRegistradas = parejas.length;

        this.clienteService.obtenerClientes().subscribe({
          next: (clientes) => {
            this.clientes = clientes;
            this.cargando = false;
            this.cdr.detectChanges();

            console.log("Parejas:", this.parejas);
            console.log("Clientes:", this.clientes);
          },
          error: (err) => {
            console.error(err);
            this.cargando = false;
            this.cdr.detectChanges();
          }
        });

      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });

  }

  obtenerCliente(idCliente: number): Cliente | undefined {
    return this.clientes.find(c => c.id === idCliente);
  }

  onSignUp(): void {
    console.log("Entraaa")
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (this.pareja.primerNombre === '' || this.pareja.primerApellido === '' ||
      this.pareja.numeroDocumento === '' || this.pareja.correoElectronico === '' ) {
      console.log("Cayó en: campos vacíos", this.pareja);
      this.mostrarMensaje("Todos los espacios son obligatorios", "error");
      return;
    }

    if (!emailRegex.test(this.pareja.correoElectronico)) {
      console.log("Cayó en: correo inválido", this.pareja.correoElectronico);
      this.mostrarMensaje("El correo no tiene un formato valido", "error");
      return;
    }

    if (this.rolUsuario === 'ADMINISTRADOR' && this.pareja.idCliente === 0) {
      this.mostrarMensaje("Debe seleccionar un cliente titular", "error");
      return;
    }

    if (this.rolUsuario === 'CLIENTE') {
      this.pareja.idCliente = this.idUsuario;
      console.log("Id asignado: ", this.idUsuario);
    }

    this.pareja.contrasenia = this.pareja.numeroDocumento;

    console.log("Pasó todas las validaciones, llamando al backend");


    if (this.modoEdicion) {
      this.parejaService.actualizarPareja(this.parejaEditandoId!, this.pareja).subscribe({
        next: (response: any) => {
          console.log("Respuesta actualización OK:", response);
          this.mostrarMensaje("Pareja actualizada correctamente", 'success');
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err: any) => {
          if (err.status === 409) {
            this.mostrarMensaje("Ese correo ya está registrado", "error");
          } else {
            this.mostrarMensaje("Error al actualizar la pareja", "error");
          }

          console.log("STATUS:", err.status);
          console.log("BODY:", err.error);
          console.log("ERROR COMPLETO:", err);
        }
      });
    } else {
      console.log("Objeto enviado:", this.pareja);
      this.parejaService.crearPareja(this.pareja).subscribe({
        next: (response: any) => {
          console.log("Respuesta OK:", response);
          this.mostrarMensaje(response, 'success');
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err) => {
          if (err.status === 409) {
            this.mostrarMensaje("Ese correo ya está registrado", "error");

          } else if (err.error === "El cupo asignado supera el cupo disponible del cliente") {
            this.mostrarMensaje("El cupo asignado supera el cupo disponible del cliente", "error");

          } else if (err.error === "El cupo asignado debe ser mayor a cero") {
            this.mostrarMensaje("El cupo asignado debe ser mayor a cero", "error");

          } else if (err.error === "Datos incompletos o error al crear la pareja") {
            this.mostrarMensaje("Debe completar todos los campos", "error");

          } else {
            this.mostrarMensaje("Error al registrar la pareja", "error");
          }

          console.log("STATUS:", err.status);
          console.log("BODY:", err.error);
          console.log("ERROR COMPLETO:", err);
        }
      });
    }
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

  cerrarModal() {
    this.mostrarModal = false;
  }

  eliminar(pareja: Pareja): void {
    this.parejaAEliminar = pareja;
    this.mostrarModalEliminar = true;
  }

  editar(pareja: Pareja): void {
    this.modoEdicion = true;
    this.parejaEditandoId = pareja.id!;
    this.pareja = { ...pareja, contrasenia: '' };
    this.mostrarModal = true;
  }

  abrirModal() {
    this.modoEdicion = false;
    this.parejaEditandoId = null;
    this.pareja = {
      primerNombre: '',
      segundoNombre: '',
      primerApellido: '',
      segundoApellido: '',
      numeroDocumento: '',
      correoElectronico: '',
      contrasenia: '',
      role: Role.PAREJA,
      cupoAsignado: 0,
      idCliente: 0,
      primeraVez: true
    };
    this.mostrarModal = true;
  }

  cerrarModalEliminar(): void {
    this.mostrarModalEliminar = false;
    this.parejaAEliminar = null;
  }

  confirmarEliminar(): void {
    if (!this.parejaAEliminar) return;

    this.parejaService.eliminarPareja(this.parejaAEliminar.id!).subscribe({
      next: () => {
        this.mostrarMensaje("Cliente eliminado correctamente", "success");
        this.cerrarModalEliminar();
        this.ngOnInit();
      },
      error: (err) => {
        console.log("Error al eliminar:", err);
        this.mostrarMensaje("Error al eliminar el cliente", "error");
        this.cerrarModalEliminar();
      }
    });
  }

  filtrarParejas(): void {


    this.parejaService.filtrar(this.filtro).subscribe({

      next: (data) => {
        this.parejas = data;
        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error(err);
      }

    });

  }

  totalCuposParejas = 0;

  cargarParejas() {
    this.parejaService.obtenerParejas().subscribe({
      next: (parejas) => {
        console.log("PAREJAS:", parejas);
        this.totalCuposParejas = parejas.reduce(
          (total, pareja) => total + pareja.cupoAsignado,
          0
        );
        console.log("TOTAL PAREJAS:", this.totalCuposParejas);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

}
