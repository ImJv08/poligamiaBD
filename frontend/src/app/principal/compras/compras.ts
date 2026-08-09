import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import {Pareja} from '../../model/pareja.model';
import {ParejaService} from '../../service/pareja.service';
import {CompraService} from '../../service/compra.service';
import {Compra, EstadoCompra} from '../../model/compra.model';
import {Cliente} from '../../model/cliente.model';
import {Almacen} from '../../model/almacen.model';
import {CurrencyPipe, DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AlmacenService} from '../../service/almacen.service';
import {ClienteService} from '../../service/cliente.service';
import {map} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {Role} from '../../model/role.enum';

@Component({
  selector: 'app-compras',
  standalone: true,
  templateUrl: './compras.html',
  imports: [
    CurrencyPipe,
    NgForOf,
    NgIf,
    FormsModule,
    NgClass
  ],
  styleUrl: './compras.css'
})

export class Compras {

  constructor(private router: Router, private cdr: ChangeDetectorRef,
              private parejaService: ParejaService,
              private compraService: CompraService,
              private almacenService: AlmacenService,
              private clienteService: ClienteService) { }



  compras: Compra[] = [];
  parejas: Pareja[] = [];
  clientes: Cliente[] = [];
  almacenes: Almacen[] = [];
  idUsuario = 0;
  rolUsuario: string = '';
  cargando = true;
  modoEdicion = false;
  compraEditandoId: number | null = null;
  mostrarModal = false;
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  compraAEliminar: Compra | null = null;
  mostrarModalEliminar = false;

  mostrarModalContrasena = false;
  nuevaContrasena = "";
  confirmPassword = "";

  parejaLista : Pareja = {
    primerNombre: "",
    segundoNombre: "",
    primerApellido: "",
    segundoApellido: "",
    numeroDocumento: "",
    correoElectronico: "",
    contrasenia: "",
    role: Role.PAREJA,
    cupoAsignado: 0,
    idCliente: 0,
    primeraVez: true
  }

  compra: Compra = {
    idPareja: 0,
    idAlmacen: 0,
    montoTransaccion: 0,
    fecha: "",
    hora: "",
    estado: "APROBAD0"
  }


  ngOnInit(): void {
    this.cargarClientes();
    this.cargarAlmacenes();

    this.idUsuario = Number(localStorage.getItem('idUsuario'));
    this.rolUsuario = localStorage.getItem('rol') || '';

    console.log("Rol:", this.rolUsuario);
    console.log("Id:", this.idUsuario);

    const peticionParejas =
      this.rolUsuario === 'ADMINISTRADOR'
        ? this.parejaService.obtenerParejas()
        : this.rolUsuario === 'CLIENTE'
          ? this.parejaService.obtenerParejasCliente(this.idUsuario)
          : this.parejaService.obtenerParejaPorId(this.idUsuario);

    peticionParejas.subscribe({

      next: (parejas) => {

        this.parejas = Array.isArray(parejas) ? parejas : [parejas];

        const peticionClientes =
          this.rolUsuario === 'ADMINISTRADOR'
            ? this.clienteService.obtenerClientes()
            : this.rolUsuario === 'CLIENTE'
              ? this.clienteService.obtenerClientePorId(this.idUsuario).pipe(
                map(cliente => [cliente])
              )
              : this.clienteService.obtenerClientePorId(this.parejas[0].idCliente).pipe(
                map(cliente => [cliente])
              );

        peticionClientes.subscribe({

          next: (clientes) => {

            this.clientes = clientes;

            this.almacenService.obtenerAlmacenes().subscribe({

              next: (almacenes) => {

                this.almacenes = almacenes;

                const peticionCompras =
                  this.rolUsuario === 'ADMINISTRADOR'
                    ? this.compraService.obtenerTodas()
                    : this.rolUsuario === 'CLIENTE'
                      ? this.compraService.obtenerComprasCliente(this.idUsuario)
                      : this.compraService.obtenerComprasPareja(this.idUsuario);

                peticionCompras.subscribe({

                  next: (compras) => {

                    this.compras = compras;
                    this.calcularTarjetas();

                    console.log("Compras:", this.compras);
                    console.log("Parejas:", this.parejas);
                    console.log("Clientes:", this.clientes);
                    console.log("Almacenes:", this.almacenes);

                    this.cargando = false;
                    this.cdr.detectChanges();
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
              }

            });

          },

          error: (err) => {
            console.error(err);
            this.cargando = false;
          }

        });

      },

      error: (err) => {
        console.error(err);
        this.cargando = false;
      }

    });

    if(this.rolUsuario == 'PAREJA'){
      this.parejaService.obtenerParejaPorId(this.idUsuario).subscribe({
        next: (pareja) => {
          this.parejaLista = pareja;

          if (pareja.primeraVez) {
            console.log("Es la primera vez");
            this.mostrarModalContrasena = true;
          } else {
            console.log("No es la primera vez");
            this.mostrarModalContrasena = false;
          }

        },
        error: (err) => {
          console.error(err);
        }
      });
    }

  }
  obtenerCliente(id: number): Cliente | undefined {
    return this.clientes.find(c => c.id === id);
  }

  obtenerPareja(id: number): Pareja | undefined {
    return this.parejas.find(p => p.id === id);
  }

  obtenerAlmacen(id: number): Almacen | undefined {
    return this.almacenes.find(a => a.id === id);
  }

  onRegistrarCompra(): void {

    console.log("Entró a registrar compra");

    if (this.compra.montoTransaccion <= 0) {
      this.mostrarMensaje("Debe ingresar un monto válido", "error");
      return;
    }

    if (this.rolUsuario === 'ADMINISTRADOR') {

      if (!this.compra.idPareja || this.compra.idPareja === 0) {
        this.mostrarMensaje("Debe seleccionar una pareja", "error");
        return;
      }

      if (!this.compra.idAlmacen || this.compra.idAlmacen === 0) {
        this.mostrarMensaje("Debe seleccionar un almacén", "error");
        return;
      }

    } else {

      this.compra.idPareja = this.idUsuario;

      if (!this.compra.idAlmacen || this.compra.idAlmacen === 0) {
        this.mostrarMensaje("Debe seleccionar un almacén", "error");
        return;
      }
    }

    this.compra.idAlmacen = Number(this.compra.idAlmacen);
    console.log("Objeto enviado:", this.compra);

    if (this.modoEdicion) {

      this.compraService.actualizarCompra(this.compraEditandoId!, this.compra).subscribe({

        next: (response: any) => {
          this.mostrarMensaje("Compra actualizada correctamente", "success");
          this.cerrarModal();
          this.ngOnInit();
        },

        error: (err) => {
          this.mostrarMensaje("Error al actualizar la compra", "error");

          console.log("STATUS:", err.status);
          console.log("BODY:", err.error);
          console.log("ERROR COMPLETO:", err);
        }

      });

    } else {

      this.compraService.registrarCompra(this.compra.idPareja, this.compra.idAlmacen, this.compra.montoTransaccion).subscribe({

        next: (response: any) => {
          console.log("Respuesta OK:", response);
          this.mostrarMensaje(response, "success");
          this.cerrarModal();
          this.ngOnInit();
        },

        error: (err) => {

          this.mostrarMensaje("Error al registrar la compra", "error");

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

  abrirModal() {
    this.modoEdicion = false;
    this.compraEditandoId = null;
    this.compra = {
      idPareja: 0,
      idAlmacen: 0,
      montoTransaccion: 0,
      fecha: "",
      hora: "",
      estado: "APROBAD0"
    };
    this.mostrarModal = true;
  }

  editar(compra: Compra){
    console.log("Compra recibida para editar:", compra);
    this.modoEdicion = true;
    this.compraEditandoId = compra.id!;
    this.compra = {...compra};
    this.mostrarModal = true;
  }

  eliminar(compra: Compra){
    this.compraAEliminar = compra;
    this.mostrarModalEliminar = true;
  }

  cerrarModalEliminar(): void {
    this.mostrarModalEliminar = false;
    this.compraAEliminar = null;
  }
  confirmarEliminar(): void {
    if (!this.compraAEliminar) return;

    this.compraService.eliminarCompra(this.compraAEliminar.id!).subscribe({
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

  nombrePareja(pareja: Pareja): string{
    const cliente = this.obtenerCliente(pareja.idCliente);
    const nombreCliente = cliente ? `${cliente.primerNombre} ${cliente.primerApellido}` : 'Cliente desconocido';
    return `${pareja.primerNombre} ${pareja.primerApellido} (Titular: ${nombreCliente})`;
  }
  cliente = '';
  almacen = '';
  pareja = "";
  estado = '';
  fechaDesde = '';
  fechaHasta = '';

  filtrar() {
    console.log("CLIENTE:", this.cliente);
    console.log("ALMACEN:", this.almacen);
    console.log("ESTADO:", this.estado);
    this.compraService.filtrarCompras(
      this.cliente,
      this.pareja,
      this.almacen,
      this.fechaDesde,
      this.fechaHasta,
      this.estado
    ).subscribe({
      next: (compras) => {
        this.compras = compras;
        this.cdr.detectChanges();
      }
    });


  }

  clientesFiltrar: any[] = [];
  almacenesFiltrar: any[] = [];

  cargarClientes() {
    this.clienteService.obtenerClientes().subscribe({
      next: (clientes) => {
        this.clientesFiltrar = clientes;
      }
    });
  }

  cargarAlmacenes() {
    this.almacenService.obtenerAlmacenes().subscribe({
      next: (almacenes) => {
        this.almacenesFiltrar = almacenes;
      }
    });
  }

  cerrarModalContrasena(){
    console.log("Entrando a cerrar")
    this.mostrarModalContrasena = false;
    this.nuevaContrasena = "";
    this.confirmPassword = "";
  }

  cambiarContra() {

    if (this.nuevaContrasena !== this.confirmPassword) {
      this.mostrarMensaje("Las contraseñas no coinciden", "error");
      return;
    }

    if (this.nuevaContrasena === "" || this.confirmPassword === "") {
      this.mostrarMensaje("Debes llenar todos los espacios", "error");
      return;
    }

    console.log("id pareja: " + this.idUsuario)
    console.log("nueva contraseña: " + this.nuevaContrasena)
    console.log("confirm contraseña: " + this.confirmPassword)

    this.parejaService.cambiarContrasenia(this.idUsuario, this.nuevaContrasena).subscribe({

      next: (response: any) => {
        this.mostrarMensaje(response, "success");
        this.cerrarModalContrasena();
        this.cdr.detectChanges();
        this.ngOnInit();
      },

      error: (err) => {

        console.error(err);
        if (err.error) {
          this.mostrarMensaje(err.error, "error");
        } else {
          this.mostrarMensaje("Ocurrió un error al cambiar la contraseña.", "error");
        }

      }

    });

  }

  pendientes = 0;
  aprobadas = 0;
  rechazadas = 0;


  calcularTarjetas(){

    this.pendientes = this.compras.filter(c =>
      c.estado === 'PENDIENTE'
    ).length;


    this.aprobadas = this.compras.filter(c =>
      c.estado === 'APROBAD0'
    ).length;


    this.rechazadas = this.compras.filter(c =>
      c.estado === 'RECHAZADO'
    ).length;

  }
}
