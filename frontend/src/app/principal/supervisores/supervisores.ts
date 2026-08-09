import {ChangeDetectorRef, Component} from '@angular/core';
import {Supervisor} from '../../model/supervisor.model';
import {Role} from '../../model/role.enum';
import {SupervisorService} from '../../service/supersivor.service';
import {Cliente} from '../../model/cliente.model';
import {AlmacenService} from '../../service/almacen.service';
import {Almacen} from '../../model/almacen.model';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-supervisores',
  standalone: true,
  templateUrl: './supervisores.html',
  styleUrl: './supervisores.css',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    NgClass
  ]
})

export class Supervisores {

  constructor(private supervisorService: SupervisorService,
              private cdr: ChangeDetectorRef, private almacenService: AlmacenService) {}

  supervisor: Supervisor = {
    primerNombre: ' ',
    segundoNombre: ' ',
    primerApellido: ' ',
    segundoApellido: ' ',
    numeroDocumento: ' ',
    correoElectronico: ' ',
    contrasenia: ' ',
    role: Role.SUPERVISOR,
    almacen: {
      id: 0,
      nombre: '',
      direccion: '',
      barrio: '',
      ciudad: ''
    }
  }

  supervisores: Supervisor[] = [];
  almacenes: Almacen[] = [];
  supervisoresRegistrados = 0;
  confirmPassword: string = "";
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  modoEdicion = false;
  supervisorEditandoId: number | null = null;
  mostrarModalEliminar = false;
  supervisorAEliminar: Supervisor | null = null;
  mostrarModal = false;
  cargando = true;

  ngOnInit() {
    this.supervisorService.obtenerSupervisores().subscribe({
      next: (data) => {
        console.log("Supervisores:", data);
        this.supervisores = data;
        this.supervisoresRegistrados = this.supervisores.length;
        this.cargando = false;
        this.cdr.detectChanges()
      }
    });

    this.almacenService.obtenerAlmacenes().subscribe(data => {
      console.log("Almacenes:", data);
      this.almacenes = data;
      this.cdr.detectChanges();
    });
  }

  obtenerAlmacen(idAlmacen: number): Almacen | undefined {
    return this.almacenes.find(a => a.id === idAlmacen);
  }

  onSignUp() {
    console.log("Entraaa")
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (this.supervisor.primerNombre === '' || this.supervisor.primerApellido === '' ||
      this.supervisor.numeroDocumento === '' || this.supervisor.correoElectronico === '' ||
      this.confirmPassword === '' || this.supervisor.contrasenia === '') {
      console.log("Cayó en: campos vacíos", this.supervisor);
      this.mostrarMensaje("Todos los espacios son obligatorios", "error");
    } else if (this.supervisor.contrasenia != "" && this.confirmPassword != "" && this.supervisor.contrasenia != this.confirmPassword) {
      console.log("Cayó en: contraseñas no coinciden");
      this.mostrarMensaje("Las contraseñas no coinciden", "error");
    } else if (!emailRegex.test(this.supervisor.correoElectronico)) {
      console.log("Cayó en: correo inválido", this.supervisor.correoElectronico);
      this.mostrarMensaje("El correo no tiene un formato valido", "error");
    } else {
      console.log("Pasó todas las validaciones, llamando al backend");

      if(this.modoEdicion){
        this.supervisorService.actualizarSupervisor(this.supervisorEditandoId!, this.supervisor).subscribe({
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
        this.supervisorService.crearSupervisor(this.supervisor).subscribe({
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

  eliminar(supervisor: Supervisor): void {
    this.supervisorAEliminar = supervisor;
    this.mostrarModalEliminar = true;
  }

  editar(supervisor: Supervisor): void {
    this.modoEdicion = true;
    this.supervisorEditandoId = supervisor.id!;
    this.supervisor = { ...supervisor, contrasenia: '' };
    this.confirmPassword = '';
    this.mostrarModal = true;
  }

  abrirModal(){
    this.modoEdicion = false;
    this.supervisorEditandoId = null;
    this.supervisor = {
      primerNombre: ' ',
      segundoNombre: ' ',
      primerApellido: ' ',
      segundoApellido: ' ',
      numeroDocumento: ' ',
      correoElectronico: ' ',
      contrasenia: ' ',
      role: Role.SUPERVISOR,
      almacen: {
        id: 0,
        nombre: '',
        direccion: '',
        barrio: '',
        ciudad: ''
      }
    };

    this.confirmPassword = "";
    this.mostrarModal = true;
  }

  filtro: string = '';
  filtrarSuperviores(): void {


    this.supervisorService.filtrar(this.filtro).subscribe({

      next: (data) => {
        this.supervisores = data;
      },

      error: (err) => {
        console.error(err);
      }

    });

  }
}
