import {ChangeDetectorRef, Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ClienteService} from '../../service/cliente.service';
import {ParejaService} from '../../service/pareja.service';
import {SobrecupoService} from '../../service/sobrecupo.service';
import {CompraService} from '../../service/compra.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-principal',
  standalone: true,
  templateUrl: './asignarCupoPareja.html',
  imports: [
    CommonModule,
    FormsModule
  ],
  styleUrl: './asignarCupoPareja.css'
})
export class AsignarCupoPareja {

  constructor(private clienteService: ClienteService, private parejaService: ParejaService,
              private cdr: ChangeDetectorRef, private router: Router) {}

  clienteLogueado: any;
  idUsuario: any;

  parejasCliente: any[] = [];

  idParejaSeleccionada: number = 0;
  cupoAsignado: number = 0;

  message: string = '';
  messageType: 'success' | 'error' | null = null;

  mostrarModalPareja = true;

  ngOnInit(): void {

    this.idUsuario = Number(localStorage.getItem('idUsuario'));


    this.clienteService.obtenerClientePorId(this.idUsuario)
      .subscribe({

        next: (cliente) => {

          this.clienteLogueado = cliente;

          this.clienteService.mostrarParejas(this.idUsuario)
            .subscribe({

              next: (parejas) => {
                this.parejasCliente = parejas;
                console.log("Parejas del cliente:", this.parejasCliente);
              },

              error: (err) => {
                console.error("Error cargando parejas", err);
              }

            });

        },

        error: (err) => {
          console.error("Error cargando cliente", err);
        }

      });

  }


  asignarCupoPareja(): void {

    if (this.idParejaSeleccionada === 0) {
      this.mostrarMensaje("Debe seleccionar una pareja", "error");
      return;
    }


    if (this.cupoAsignado <= 0) {
      this.mostrarMensaje("Debe ingresar un cupo válido", "error");
      return;
    }


    this.parejaService.asignarCupoIndividual(
      this.idParejaSeleccionada,
      this.cupoAsignado
    )
      .subscribe({

        next: (respuesta) => {

          this.mostrarMensaje(respuesta, "success");

          this.cupoAsignado = 0;
          this.idParejaSeleccionada = 0;
          this.router.navigate(['/pagina-principal/parejas']);

        },

        error: (err) => {

          console.log("Status:", err.status);
          console.log("Error:", err.error);
          console.log("Respuesta:", err);

          if (err.status === 400) {
            this.mostrarMensaje(String(err.error), "error");
          } else {
            this.mostrarMensaje("Error al asignar cupo", "error");
          }

        }

      });

  }


  mostrarMensaje(texto: string, tipo: 'success' | 'error'): void {

    console.log("Mensaje:", texto);

    this.message = texto;
    this.messageType = tipo;

    setTimeout(() => {
      this.message = '';
      this.messageType = null;
    }, 3000);

    this.cdr.detectChanges();
  }


  cerrarModalPareja(): void {

    this.mostrarModalPareja = false;
    this.cupoAsignado = 0;
    this.idParejaSeleccionada = 0;
    this.router.navigate(['/pagina-principal/parejas']);

  }


}
