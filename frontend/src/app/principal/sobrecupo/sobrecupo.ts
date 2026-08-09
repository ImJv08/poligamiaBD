import {ChangeDetectorRef, Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Sobrecupos} from '../../model/sobrecupo.model';
import {ParejaService} from '../../service/pareja.service';
import {ClienteService} from '../../service/cliente.service';
import {SobrecupoService} from '../../service/sobrecupo.service';
import {Pareja} from '../../model/pareja.model';
import {Cliente} from '../../model/cliente.model';
import {CurrencyPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AlmacenService} from '../../service/almacen.service';
import {Almacen} from '../../model/almacen.model';
import {Compra} from '../../model/compra.model';
import {CompraService} from '../../service/compra.service';
import {SupervisorService} from '../../service/supersivor.service';
import {of} from 'rxjs';

@Component({
  selector: 'app-sobrecupo',
  standalone: true,
  templateUrl: './sobrecupo.html',
  imports: [
    NgForOf,
    NgIf,
    NgClass,
    CurrencyPipe
  ],
  styleUrl: './sobrecupo.css'
})

export class Sobrecupo {
  constructor(private router: Router, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private parejaService: ParejaService, private clienteService: ClienteService,
              private sobrecupoService: SobrecupoService, private almacenService: AlmacenService,
              private compraService: CompraService, private supervisorService: SupervisorService) {
  }
  sobrecuposs:  Sobrecupos = {
    idCompra: 0,
    idSupervisor: 0,
    montoAutorizado: 0,
    autorizadoCliente: false,
    aprobadoSupervisor: false,
    idClienteTitular: 0
  }

  parejas: Pareja[] = []
  clientes: Cliente[] = []
  sobrecupos: Sobrecupos[] = []
  almacenes: Almacen[] = []
  compras: Compra[] = []
  cargando = true;
  idUsuario: number = 0;
  rolUsuario = localStorage.getItem('rol');

  pendientes = 0;
  aprobados = 0;
  montoAprobado = 0;

  ngOnInit(): void {

    this.cargarSobrecupos();

    this.rolUsuario = localStorage.getItem('rol');
    this.idUsuario = Number(localStorage.getItem('idUsuario'));

    console.log("idUsuario:", this.idUsuario);
    console.log("rol:", this.rolUsuario);

    const peticionParejas =
      (this.rolUsuario === 'ADMINISTRADOR' || this.rolUsuario === 'SUPERVISOR')
        ? this.parejaService.obtenerParejas()
        : this.parejaService.obtenerParejasCliente(this.idUsuario);

    peticionParejas.subscribe({
      next: (parejas) => {


        this.parejas = parejas;

        this.clienteService.obtenerClientes().subscribe({
          next: (clientes) => {

            this.clientes = clientes;

            this.compraService.obtenerTodas().subscribe({
              next: (compras) => {

                this.compras = compras;

                this.almacenService.obtenerAlmacenes().subscribe({
                  next: (almacenes) => {

                    this.almacenes = almacenes;

                    const peticionSupervisor =
                      this.rolUsuario === 'SUPERVISOR'
                        ? this.supervisorService.obtenerSupervisorPorId(this.idUsuario)
                        : of(null);

                    peticionSupervisor.subscribe({
                      next: (supervisor) => {

                        const idAlmacenSupervisor = supervisor?.almacen?.id;

                        this.sobrecupoService.mostrarSobrecupos().subscribe({
                          next: (sobrecupos) => {

                            if (this.rolUsuario === 'ADMINISTRADOR') {
                              this.sobrecupos = sobrecupos;

                            } else if (this.rolUsuario === 'SUPERVISOR') {
                              this.sobrecupos = sobrecupos.filter(s => {
                                const compra = this.compras.find(c => c.id === s.idCompra);
                                return compra?.idAlmacen === idAlmacenSupervisor;
                              });

                            } else {
                              const idsParejas = this.parejas.map(p => p.id);
                              this.sobrecupos = sobrecupos.filter(s => {
                                const compra = this.compras.find(c => c.id === s.idCompra);
                                return compra ? idsParejas.includes(compra.idPareja) : false;
                              });
                            }

                            this.cargando = false;
                            this.cdr.detectChanges();

                            this.calcularTarjetas();


                            console.log("Sobrecupos:", this.sobrecupos);
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


  nombrePareja(idCompra: number): string {

    const compra = this.obtenerCompra(idCompra);

    if (!compra) {
      return '';
    }

    const pareja = this.obtenerPareja(compra.idPareja);

    return pareja
      ? `${pareja.primerNombre} ${pareja.primerApellido}`
      : '';
  }

  nombreTitular(idPareja: number): string {
    const pareja = this.obtenerPareja(idPareja);

    if (!pareja) {
      return '';
    }

    const cliente = this.obtenerCliente(pareja.idCliente);

    return cliente
      ? `${cliente.primerNombre} ${cliente.primerApellido}`
      : '';
  }


  nombreAlmacen(idCompra: number): string {

    const compra = this.obtenerCompra(idCompra);

    if (!compra) {
      return '';
    }

    const almacen = this.obtenerAlmacen(compra.idAlmacen);

    return almacen
      ? `${almacen.nombre} - ${almacen.barrio}, ${almacen.ciudad}`
      : '';
  }

  montoCompra(idCompra: number): number {

    const compra = this.obtenerCompra(idCompra);

    return compra ? compra.montoTransaccion : 0;
  }

  obtenerCompra(idCompra: number): Compra | undefined {
    return this.compras.find(c => c.id === idCompra);
  }

  obtenerPareja(id: number): Pareja | undefined {
    return this.parejas.find(p => p.id === id);
  }

  obtenerCliente(id: number): Cliente | undefined {
    return this.clientes.find(c => c.id === id);
  }

  obtenerAlmacen(id: number): Almacen | undefined {
    return this.almacenes.find(a => a.id === id);
  }

  aprobarSobrecupo(sobrecupo:Sobrecupos){

  }

  rechazarSobrecupo(sobrecupo: Sobrecupos){

  }



  autorizar(sobrecupo: any) {

    console.log("AUTORIZAR CLICK", sobrecupo);

    this.sobrecupoService.autorizarSobrecupo(
      sobrecupo.id,
      this.idUsuario
    ).subscribe({

      next: () => {
        console.log(
          "Actualizar estado:",
          sobrecupo.idCompra,
          sobrecupo.id,
          sobrecupo.idPareja
        );

        const compra = this.compras.find(c => c.id === sobrecupo.idCompra);

        if (!compra) {
          console.error("No se encontró la compra");
          return;
        }

        this.compraService.actualizarEstadoCompra(
          sobrecupo.idCompra,
          sobrecupo.id,
          compra.idPareja
        ).subscribe({

          next: () => {
            this.cargarSobrecupos();
            this.cdr.detectChanges();
            this.ngOnInit()
          },

          error: (err) => console.error(err)

        });

        this.cdr.detectChanges();
        this.ngOnInit();
      },

      error: (err) => console.error(err)

    });
  }

  denegar(sobrecupo: any) {
    console.log("denegar CLICK", sobrecupo);

    this.sobrecupoService.denegarSobrecupo(
      sobrecupo.id,
      this.idUsuario
    ).subscribe({
      next: () => {

        console.log(
          "Actualizar estado:",
          sobrecupo.idCompra,
          sobrecupo.id,
          sobrecupo.idPareja
        );

        const compra = this.compras.find(c => c.id === sobrecupo.idCompra);

        if (!compra) {
          console.error("No se encontró la compra");
          return;
        }
        this.compraService.actualizarEstadoCompra(
          sobrecupo.idCompra,
          sobrecupo.id,
          compra.idPareja
        ).subscribe({


          next: () => {
            this.cargarSobrecupos();
            this.cdr.detectChanges();
            this.ngOnInit();
          },

          error: (err) => console.error(err)

        });

      },

      error: (err) => console.error(err)
    });

    this.cdr.detectChanges();
    this.ngOnInit();

  }

  cancelar(sobrecupo: any) {
    console.log("CANCELAR CLICK", sobrecupo);

    this.sobrecupoService.cancelarSobrecupo(
      sobrecupo.id,
      this.idUsuario
    ).subscribe({
      next: () => {
        console.log(
          "Actualizar estado:",
          sobrecupo.idCompra,
          sobrecupo.id,
          sobrecupo.idPareja
        );

        const compra = this.compras.find(c => c.id === sobrecupo.idCompra);

        if (!compra) {
          console.error("No se encontró la compra");
          return;
        }

        this.compraService.actualizarEstadoCompra(
          sobrecupo.idCompra,
          sobrecupo.id,
          compra.idPareja
        ).subscribe({

          next: () => {
            this.cargarSobrecupos();
            this.cdr.detectChanges();
            this.ngOnInit();
          },

          error: (err) => console.error(err)

        });

      },

      error: (err) => console.error(err)
    });

    this.cdr.detectChanges();
    this.ngOnInit();
  }

  aprobar(sobrecupo: any) {
    console.log("APROBAR CLICK", sobrecupo);
    this.sobrecupoService.aprobarSobrecupo(
      sobrecupo.id,
      this.idUsuario
    ).subscribe({
      next: () => {
        console.log(
          "Actualizar estado:",
          sobrecupo.idCompra,
          sobrecupo.id,
          sobrecupo.idPareja
        );

        const compra = this.compras.find(c => c.id === sobrecupo.idCompra);

        if (!compra) {
          console.error("No se encontró la compra");
          return;
        }

        this.compraService.actualizarEstadoCompra(
          sobrecupo.idCompra,
          sobrecupo.id,
          compra.idPareja
        ).subscribe({

          next: () => {
            this.cargarSobrecupos();
            this.cdr.detectChanges();
            this.ngOnInit();
          },

          error: (err) => console.error(err)

        });

      },

      error: (err) => console.error(err)
    });

    this.cdr.detectChanges();
    this.ngOnInit();
  }



  sobrecuposObtener: any[] = [];

  cargarSobrecupos() {
    this.sobrecupoService.obtenerTodas().subscribe({
      next: (sobrecupos) => {
        this.sobrecuposObtener = sobrecupos;
        console.log("SOBRECUPOS:", this.sobrecupos);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }



  calcularTarjetas(): void {


    console.log("Sobrecupos:", this.sobrecupos);

    this.pendientes = this.sobrecupos.filter(s =>
      s.aprobadoSupervisor == null
    ).length;

    this.aprobados = this.sobrecupos.filter(s =>
      s.aprobadoSupervisor === true
    ).length;

    this.montoAprobado = this.sobrecupos
      .filter(s => s.aprobadoSupervisor === true)
      .reduce((total, s) => {

        const compra = this.compras.find(c => c.id === s.idCompra);

        return total + (compra ? compra.montoTransaccion : 0);

      }, 0);

    this.cdr.detectChanges();
  }





}
