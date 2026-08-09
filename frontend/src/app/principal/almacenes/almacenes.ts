import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import {AlmacenService} from '../../service/almacen.service';
import {Almacen} from '../../model/almacen.model';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Cliente} from '../../model/cliente.model';
import {Role} from '../../model/role.enum';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-almacenes',
  standalone: true,
  templateUrl: './almacenes.html',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    NgClass
  ],
  styleUrl: './almacenes.css'
})

export class Almacenes {
  constructor(private router: Router, private almacenService: AlmacenService,
              private cdr: ChangeDetectorRef) { }

  almacenes: Almacen[] = [];
  almacenesRegistrados = 0;
  ciudadesRegistrados = 0;
  cargando = true;
  message: string = "";
  messageType: 'success' | 'error' | null = null;
  modoEdicion = false;
  almacenEditandoId: number | null = null;
  mostrarModalEliminar = false;
  mostrarModal = false;
  almacenAEliminar: Almacen | null = null;

  almacen: Almacen = {
    nombre: '',
    direccion: '',
    barrio: '',
    ciudad: ''
  }

  filtro: string = '';

  ngOnInit(): void {
      this.almacenService.obtenerAlmacenes().subscribe({
        next: (data) => {
          this.almacenes = data;
          this.almacenesRegistrados = this.almacenes.length;
          this.ciudadesRegistrados = new Set(
            this.almacenes.map(a => a.ciudad)
          ).size;
          this.cargando = false;
          this.cdr.detectChanges();
        }
      });
  }

  onCrear(){
    console.log('Entraaa');
    if(this.almacen.nombre === '' || this.almacen.barrio === '' || this.almacen.barrio === '' ||
    this.almacen.ciudad === '' || this.almacen.direccion === ''){
      console.log("Cayó en: campos vacíos", this.almacen);
      this.mostrarMensaje("Todos los espacios son obligatorios", "error");
      return;
    }

    if(this.modoEdicion){
      this.almacenService.actualizarAlmacen(this.almacenEditandoId!, this.almacen).subscribe({
        next: (response: any) => {
          console.log("Respuesta actualización OK:", response);
          this.mostrarMensaje("Cliente actualizado correctamente", 'success');
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err: any) => {
          this.mostrarMensaje("Error al registrar el usuario", "error");
        }
      });
    } else {
      this.almacenService.crearAlmacen(this.almacen).subscribe({
        next: (response: any) => {
          console.log("Respuesta OK:", response);
          this.mostrarMensaje(response, 'success');
          this.cerrarModal();
          this.ngOnInit();
        },
        error: (err: any) => {
        this.mostrarMensaje("Error al registrar el usuario", "error");
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

  abrirModal() {
    this.modoEdicion = false;
    this.almacenEditandoId = null;
    this.almacen = {
      nombre: '',
      direccion: '',
      barrio: '',
      ciudad: ''
    };
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }

  editar(almacen: Almacen): void {
    this.modoEdicion = true;
    this.almacenEditandoId = almacen.id!;
    this.almacen = { ...almacen};
    this.mostrarModal = true;
  }

  eliminar(almacen: Almacen): void {
    this.almacenAEliminar = almacen;
    this.mostrarModalEliminar = true;
  }

  cerrarModalEliminar(): void {
    this.mostrarModalEliminar = false;
    this.almacenAEliminar = null;
  }

  confirmarEliminar(): void {
    if (!this.almacenAEliminar) return;

    this.almacenService.eliminarAlmacen(this.almacenAEliminar.id!).subscribe({
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

  filtrarAlmacenes(): void {


    this.almacenService.filtrar(this.filtro).subscribe({

      next: (data) => {
        this.almacenes = data;
      },

      error: (err) => {
        console.error(err);
      }

    });

  }
}




