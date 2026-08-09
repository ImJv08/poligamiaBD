import {Component, NgZone, ChangeDetectorRef} from '@angular/core';
import { Router } from '@angular/router';
import {Role} from '../model/role.enum';
import {Cliente} from '../model/cliente.model';
import {authService} from '../service/auth/auth.service';
import {FormsModule} from '@angular/forms';
import {ClienteService} from '../service/cliente.service';
import {CommonModule, NgIf} from '@angular/common';

@Component({
  selector: 'app-registro',
  standalone: true,
  templateUrl: './registro.html',
  imports: [
    FormsModule,
    NgIf,
    CommonModule
  ],
  styleUrl: './registro.css'
})

export class Registro {
  constructor(private router: Router, private authService: authService, private clienteService: ClienteService,
  private ngZone: NgZone, private cdr: ChangeDetectorRef) {}

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
    fechaRegistro: ''
  }
  confirmPassword: string = "";
  message: string = "";
  messageType: 'success' | 'error' | null = null;

  irPrincipal(){
    this.router.navigate(['/']);
  }

  onSignUp() {
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
      this.authService.registrar(this.cliente).subscribe({
        next: (response: any) => {
          console.log("Respuesta OK:", response);
          this.mostrarMensaje(response, 'success');
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

}
