import {ChangeDetectorRef, Component} from '@angular/core';
import {Cliente} from '../../model/cliente.model';
import {ClienteService} from '../../service/cliente.service';
import {CurrencyPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {ParejaService} from '../../service/pareja.service';
import {Observable} from 'rxjs';
import {SobrecupoService} from '../../service/sobrecupo.service';
import {CompraService} from '../../service/compra.service';
import {Role} from '../../model/role.enum';
import {FormsModule} from '@angular/forms';
import {authService} from '../../service/auth/auth.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  templateUrl: './clientes.html',
  imports: [
    NgIf,
    CurrencyPipe,
    NgForOf,
    FormsModule,
    NgClass
  ],
  styleUrl: './clientes.css'
})

export class Clientes {

  clientes: Cliente[] = [];
  cargando = true;
  mostrarModal = false;
  parejasRegistradas: number = 0;
  clientesRegistrados: number = 0;
  sobrecuposPendientes: number = 0;
  comprasHoy: number = 0;
  modoEdicion = false;
  clienteEditandoId: number | null = null;
  mostrarModalEliminar = false;
  clienteAEliminar: Cliente | null = null;
  rolUsuairo = localStorage.getItem('rol');

  constructor(private clienteService: ClienteService, private parejaService: ParejaService,
              private cdr: ChangeDetectorRef, private sobrecupoService: SobrecupoService,
              private compraService: CompraService, private authService: authService) {}

  cliente: Cliente = {
    primerNombre: ' ',
    segundoNombre: ' ',
    primerApellido: ' ',
    segundoApellido: ' ',
    numeroDocumento: ' ',
    correoElectronico: ' ',
    contrasenia: ' ',
    role: Role.CLIENTE,
    cupoTotal: 0,
    fechaRegistro: ' '
  }

  confirmPassword: string = "";
  message: string = "";
  messageType: 'success' | 'error' | null = null;

  onSignUp(){
    console.log("Entraaa");
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (this.cliente.primerNombre === '' || this.cliente.primerApellido === '' ||
      this.cliente.numeroDocumento === '' || this.cliente.correoElectronico === '' ||
      this.confirmPassword === '' || this.cliente.contrasenia === '') {
      console.log("Cayó en: campos vacíos", this.cliente);
      this.mostrarMensaje("Todos los espacios son obligatorios", "error");
    } else if (this.cliente.contrasenia != "" && this.confirmPassword != "" && this.cliente.contrasenia != this.confirmPassword) {
      console.log("Cayó en: contraseñas no coinciden");
      this.mostrarMensaje("Las contraseñas no coinciden", "error");
    } else if (!emailRegex.test(this.cliente.correoElectronico)) {
      console.log("Cayó en: correo inválido", this.cliente.correoElectronico);
      this.mostrarMensaje("El correo no tiene un formato valido", "error");
    } else {
      console.log("Pasó todas las validaciones, llamando al backend");

      if (this.modoEdicion) {
        this.clienteService.actualizarCliente(this.clienteEditandoId!, this.cliente).subscribe({
          next: (response: any) => {
            console.log("Respuesta actualización OK:", response);
            this.mostrarMensaje("Cliente actualizado correctamente", 'success');
            this.cerrarModal();
            this.ngOnInit();
          },
          error: (err: any) => {
            if (err.status === 409) {
              this.mostrarMensaje("Ese correo ya está registrado", "error");
            } else {
              this.mostrarMensaje("Error al registrar el usuario", "error");
            }
          }
        });
      } else {
        this.authService.registrar(this.cliente).subscribe({
          next: (response: any) => {
            console.log("Respuesta OK:", response);
            this.mostrarMensaje(response, 'success');
            this.cerrarModal();
            this.ngOnInit();
          },
          error: (err) => {
            if (err.status === 409) {
              this.mostrarMensaje("Ese correo ya está registrado", "error");
            } else {
              this.mostrarMensaje("Error al registrar el usuario", "error");
            }
          }
        });
      }
    }
  }

  eliminar(cliente: Cliente): void {
    this.clienteAEliminar = cliente;
    this.mostrarModalEliminar = true;
  }

  cerrarModalEliminar(): void {
    this.mostrarModalEliminar = false;
    this.clienteAEliminar = null;
  }

  confirmarEliminar(): void {
    if (!this.clienteAEliminar) return;

    this.clienteService.eliminarCliente(this.clienteAEliminar.id!).subscribe({
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

  ngOnInit(): void {
    this.rolUsuairo = localStorage.getItem('rol');
    console.log("Entró al ngOnInit");

    this.clienteService.obtenerClientes().subscribe({
      next: (data) => {
        console.log("Clientes:", data);
        this.clientes = data;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
      }
    });

    this.parejaService.contarPareja().subscribe({
      next: (cantidad) => {
        console.log("Parejas:", cantidad);
        this.parejasRegistradas = cantidad;
        this.cdr.detectChanges();
      }
    });

    this.clienteService.contarCliente().subscribe({
      next: (data) => {
        console.log("Clientes:", data);
        this.clientesRegistrados = data;
        this.cdr.detectChanges();
      }
    });

    this.sobrecupoService.contarSobrecupo().subscribe({
      next: (data) => {
        console.log("Sobrecupos:", data);
        this.sobrecuposPendientes = data;
        this.cdr.detectChanges();
      }
    });

    this.compraService.comprasHoy().subscribe({
      next: (data) => {
        console.log("ComprasHoy:", data);
        this.comprasHoy = data;
        this.cdr.detectChanges();
      }
    });
  }
  abrirModal() {
    this.modoEdicion = false;
    this.clienteEditandoId = null;
    this.cliente = {
      primerNombre: ' ',
      segundoNombre: ' ',
      primerApellido: ' ',
      segundoApellido: ' ',
      numeroDocumento: ' ',
      correoElectronico: ' ',
      contrasenia: ' ',
      role: Role.CLIENTE,
      cupoTotal: 0,
      fechaRegistro: ' '
    };
    this.confirmPassword = '';
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }
  editar(cliente: Cliente): void {
    this.modoEdicion = true;
    this.clienteEditandoId = cliente.id!;
    this.cliente = { ...cliente, contrasenia: '' };
    this.confirmPassword = '';
    this.mostrarModal = true;
  }

}
