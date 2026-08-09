import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-historial-compra',
  standalone: true,
  templateUrl: './historial-compra.html',
  styleUrl: './historial-compra.css'
})

export class HistorialCompras {
  constructor(private router: Router) {}
}
