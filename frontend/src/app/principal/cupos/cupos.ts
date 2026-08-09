import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ClienteService} from '../../service/cliente.service';
import {AlmacenService} from '../../service/almacen.service';
import {ParejaService} from '../../service/pareja.service';
import { CommonModule } from '@angular/common';
import {RestriccionService} from '../../service/restriccion.service';

@Component({
  selector: 'app-cupos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cupos.html',
  styleUrl: './cupos.css'
})

export class Cupos {

  totalCupos = 0;
  totalCuposParejas = 0;
  mostrarDesglose = false;

  constructor(private router: Router, private clienteService: ClienteService,
              private parejaService: ParejaService, private cd: ChangeDetectorRef,
              private restriccionService: RestriccionService, private route: ActivatedRoute,
              private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.cargarClientes();
    this.cargarParejas();
    this.mostrarCliente();
  }
  parejas: any[] = [];
  cliente: any;


  cargarClientes() {
    this.clienteService.obtenerClientes().subscribe({
      next: (clientes) => {
        console.log("CLIENTES:", clientes);
        this.totalCupos = clientes.reduce(
          (total, cliente) => total + cliente.cupoTotal,
          0
        );
        console.log("TOTAL CLIENTES:", this.totalCupos);
      }
    });

    this.cd.detectChanges();
  }



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

    this.cd.detectChanges();
  }

  clienteMostrar: any[] = [];

  mostrarCliente() {
    this.clienteService.obtenerClientes().subscribe({
      next: (clientes) => {
        console.log("CLIENTES PARA TABLA:", clientes);
        this.clienteMostrar = clientes.map(cliente => ({
          idCliente: cliente.id,
          nombre: cliente.primerNombre,
          apellido: cliente.primerApellido,
          cupoTotal: cliente.cupoTotal,
          cupoTotalPareja: 0,
          cantidadParejas: 0
        }));
        this.clienteMostrar.forEach(cliente => {
          this.cargarCuposParejas(cliente.idCliente);
        });
        console.log("TABLA:", this.clienteMostrar);
      },
      error: (err) => {
        console.error(err);
      }
    });

    this.cd.detectChanges();
  }



  cargarCuposParejas(idCliente: number) {
    this.clienteService.mostrarParejas(idCliente).subscribe({
      next: (parejas) => {

        const total = parejas.reduce(
          (sum, pareja) => sum + pareja.cupoAsignado,
          0
        );

        const cliente = this.clienteMostrar.find(
          c => c.idCliente === idCliente
        );

        if (cliente) {
          cliente.cupoAsignado = total;
          cliente.cantidadParejas = parejas.length;
        }
      }

    });

    this.cd.detectChanges();

  }

  totalPareja = 0;

  asignado = 0;
  disponible = 0;
  cupoInicial = 0;

  restricciones: any[] = [];

  verDesglose(cliente: any) {
    console.log("ID:", cliente.idCliente);
    this.mostrarDesglose = true;

    this.clienteService.obtenerClientePorId(cliente.idCliente)
      .subscribe({
        next: (clienteCompleto) => {
          this.cliente = clienteCompleto;
          this.disponible = clienteCompleto.cupoTotal
          this.cdr.detectChanges();

        }
      });



    this.clienteService.mostrarParejas(cliente.idCliente)
      .subscribe(parejas => {
        this.parejas = parejas;
        this.totalPareja = parejas.length
        this.parejas = parejas;
        this.asignado = parejas.reduce(
          (total, pareja) => total + pareja.cupoAsignado,
          0
        );
        this.restricciones = [];
        this.parejas.forEach(pareja => {
          this.restriccionService.obtenerRestriccionesPareja(pareja.id)
            .subscribe(res => {
              console.log("entraa");
              console.log(res);
              this.restricciones.push(...res);
              this.cd.detectChanges();
            });
        });

      });

    this.cupoInicial = this.disponible + this.asignado;
    console.log("Parejas:", this.parejas);
    console.log("Restricciones finales:", this.restricciones);
    this.cd.detectChanges();


  }

  volver() {
    this.mostrarDesglose = false;
  }

  porcentajeAsignado() {
    return (this.asignado / this.cupoInicial) * 100;
  }

  porcentajeDisponible() {
    return (this.disponible / this.cupoInicial) * 100;
  }

  obtenerPareja(id: number) {
    return this.parejas.find(p => p.id === id);
  }
}
