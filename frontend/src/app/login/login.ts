import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import {Role} from '../model/role.enum';
import {Usuario} from '../model/usuario.model';
import {authService} from '../service/auth/auth.service';
import {SupervisorService} from '../service/supersivor.service';
import {ParejaService} from '../service/pareja.service';
import {ClienteService} from '../service/cliente.service';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {catchError, Observable, of} from 'rxjs';
import {Cliente} from '../model/cliente.model';
import {CommonModule, NgClass} from '@angular/common';
import {AdministradorService} from '../service/administrador.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule
  ],
  styleUrl: './login.css'
})

export class Login {

  constructor(
    private fb: FormBuilder,
    private authService: authService,
    private supervisorService: SupervisorService,
    private clienteService: ClienteService,
    private parejaService: ParejaService,
    private adminService: AdministradorService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ){};

  usuario: Usuario = {
    primerNombre: ' ',
    segundoNombre: ' ',
    primerApellido: ' ',
    segundoApellido: ' ',
    numeroDocumento: ' ',
    correoElectronico: ' ',
    contrasenia: ' ',
    role: Role.CLIENTE
  }

  loading = false;
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  showPassword = false;
  showVerificationDialog: boolean = false;

  clearMessage() {
    this.message = '';
    this.messageType = null;
  }

  closeDialog() {
    this.showVerificationDialog = false;
  }

  onSubmit(): void {

    if (this.usuario.correoElectronico == "" || this.usuario.contrasenia == "") {
      this.mostrarMensaje("Todos los espacios son obligatorios", "error");
      return;
    }

    this.loading = true;

    this.authService.login(this.usuario).subscribe({

      next: (response: any) => {

        const token = response;
        localStorage.setItem('token', token);

        console.log("Respuesta login:", response);

        this.supervisorService.obtenerPorCorreo(this.usuario.correoElectronico)
          .pipe(
            catchError(() => of(null))
          )
          .subscribe(usuario => {

            if (usuario) {
              localStorage.setItem('rol', 'SUPERVISOR');
              localStorage.setItem('idUsuario', usuario.id);
              this.mostrarMensaje("Bienvenido " + this.usuario.primerNombre + " " + this.usuario.primerApellido, "success");
              this.router.navigate(['/pagina-principal/sobrecupo']);
              return;
            }

            this.clienteService.obtenerPorCorreo(this.usuario.correoElectronico)
              .pipe(
                catchError(() => of(null))
              )
              .subscribe(usuario => {

                if (usuario) {
                  localStorage.setItem('rol', 'CLIENTE');
                  localStorage.setItem('idUsuario', usuario.id);
                  this.mostrarMensaje("Bienvenido " + this.usuario.primerNombre + " " + this.usuario.primerApellido, "success");
                  this.router.navigate(['/pagina-principal/parejas']);
                  return;
                }

                this.parejaService.obtenerPorCorreo(this.usuario.correoElectronico)
                  .pipe(
                    catchError(() => of(null))
                  )
                  .subscribe(usuario => {

                    if (usuario) {
                      localStorage.setItem('rol', 'PAREJA');
                      localStorage.setItem('idUsuario', usuario.id);
                      this.mostrarMensaje("Bienvenido " + this.usuario.primerNombre + " " + this.usuario.primerApellido, "success");
                      this.router.navigate(['/pagina-principal/compras']);
                      return;
                    }

                    this.adminService.obtenerPorCorreo(this.usuario.correoElectronico)
                      .pipe(
                        catchError(() => of(null))
                      )
                      .subscribe(usuario => {

                        if (usuario) {
                          localStorage.setItem('rol', 'ADMINISTRADOR');
                          localStorage.setItem('idUsuario', usuario.id);
                          this.mostrarMensaje("Bienvenido " + this.usuario.primerNombre + " " + this.usuario.primerApellido, "success");
                          this.router.navigate(['/pagina-principal']);
                          return;
                        }

                        this.mostrarMensaje("Usuario no encontrado", "error");

                      });

                  });

              });

          });

      },

      error: (err) => {
        console.log('ENTRÓ AL ERROR DEL LOGIN', err);
        this.loading = false;
        this.mostrarMensaje("Usuario o contraseña incorrectos", "error");
      }

    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  irRegistro(): void {
    this.router.navigate(['/registro-cliente']);
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error', duracionMs: number = 3000): void {
    this.message = texto;
    this.messageType = tipo;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.clearMessage();
    }, duracionMs);
  }
}
