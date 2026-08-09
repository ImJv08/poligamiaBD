import {ChangeDetectorRef, Component} from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import {CommonModule, NgIf} from '@angular/common';
import { RouterLink } from '@angular/router';
import {ClienteService} from '../../service/cliente.service';
import {ParejaService} from '../../service/pareja.service';
import {SobrecupoService} from '../../service/sobrecupo.service';
import {CompraService} from '../../service/compra.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-principal',
  standalone: true,
  templateUrl: './anadirCupo.html',
  imports: [
    CommonModule,
    FormsModule
  ],
  styleUrl: './anadirCupo.css'
})
export class AnadirCupo {

  constructor(private clienteService: ClienteService, private router: Router) {}

  clienteLogueado: any;
  idUsuario: any;
  message: string = '';
  messageType: 'success' | 'error' | null = null;
  mostrarModalCupo = true;

  ngOnInit(): void {

    this.idUsuario = Number(localStorage.getItem('idUsuario'));

    this.clienteService.obtenerClientePorId(this.idUsuario).subscribe({
      next: (cliente) => {
        this.clienteLogueado = cliente;
      },
      error: (err) => {
        console.error(err);
      }
    });

  }

  cupoTotal: number = 0;

  asignarCupoTotal(): void {

    console.log("ID CLIENTE:", this.clienteLogueado.id);
    console.log("CUPO:", this.cupoTotal);

    if (this.cupoTotal <= 0) {
      this.mostrarMensaje("Debe ingresar un cupo válido", "error");
      return;
    }

    this.clienteService.asignarCupoTotal(
      this.clienteLogueado.id,
      this.cupoTotal
    ).subscribe({

      next: (respuesta) => {
        this.mostrarMensaje(respuesta, "success");
        this.cerrarModalCupo();
        console.log("ID CLIENTE:", this.clienteLogueado.id);
        console.log("CUPO:", this.cupoTotal);
        this.ngOnInit();
        this.router.navigate(['/pagina-principal/parejas']);
      },

      error: (err) => {

        if (err.status === 404) {
          this.mostrarMensaje("El cliente no existe", "error");
        } else if (err.status === 400) {
          this.mostrarMensaje(err.error, "error");
        } else {
          this.mostrarMensaje("Error al asignar el cupo", "error");
        }

        console.error(err);
      }

    });

  }
  mostrarMensaje(
    texto: string,
    tipo: 'success' | 'error',
    duracionMs: number = 3000
  ): void {

    this.message = texto;
    this.messageType = tipo;



    setTimeout(() => {
      this.clearMessage();
    }, duracionMs);
  }

  clearMessage(): void {
    this.message = '';
    this.messageType = null;
  }
  abrirModalCupo(): void {
    this.mostrarModalCupo = true;
  }

  cerrarModalCupo(): void {
    this.mostrarModalCupo = false;
    this.cupoTotal = 0;
    this.clearMessage();
    this.router.navigate(['/pagina-principal/parejas']);
  }

}
