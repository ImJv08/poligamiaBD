import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import {NgIf} from '@angular/common';
import { RouterLink } from '@angular/router';
import {SupervisorService} from '../service/supersivor.service';
import {Pareja} from '../model/pareja.model';
import {Supervisor} from '../model/supervisor.model';
import {Usuario} from '../model/usuario.model';
import {ParejaService} from '../service/pareja.service';
import {ClienteService} from '../service/cliente.service';
import {AdministradorService} from '../service/administrador.service';
import {Role} from '../model/role.enum';
import {Cliente} from '../model/cliente.model';

@Component({
  selector: 'app-principal',
  standalone: true,
  imports: [RouterOutlet, NgIf, RouterLink],
  templateUrl: './principal.html',
  styleUrl: './principal.css'
})
export class Principal {
  constructor(private router: Router, private servicePareja: ParejaService,
              private clienteService: ClienteService, private serviceSupervisor: SupervisorService,
              private adminService: AdministradorService) {
  }
  rolUsuario: string = localStorage.getItem('rol') || '';
  idUsuario: number = 0;

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

  ngOnInit(): void {
    this.rolUsuario = localStorage.getItem('rol') || '';
    this.idUsuario = Number(localStorage.getItem('idUsuario'));
    this.clienteMostrar();


  }

  tieneAcceso(rolesPermitidos: string[]): boolean {
    return this.rolUsuario !== null && rolesPermitidos.includes(this.rolUsuario);
  }

  clienteMostrar(){

    if(this.rolUsuario === 'CLIENTE'){
      this.clienteService.obtenerClientePorId(this.idUsuario).subscribe(cliente => {
        this.cliente.primerNombre = cliente.primerNombre;
        this.cliente.primerApellido = cliente.segundoApellido;

      });
    } else if (this.rolUsuario === 'SUPERVISOR'){
      this.serviceSupervisor.obtenerSupervisorPorId(this.idUsuario).subscribe(cliente => {
        this.cliente.primerNombre = cliente.primerNombre;
        this.cliente.primerApellido = cliente.segundoApellido;

      });
    } else if (this.rolUsuario === 'PAREJA'){
      this.servicePareja.obtenerParejaPorId(this.idUsuario).subscribe(cliente => {
        this.cliente.primerNombre = cliente.primerNombre;
        this.cliente.primerApellido = cliente.segundoApellido;

      });

    }

  }









}
