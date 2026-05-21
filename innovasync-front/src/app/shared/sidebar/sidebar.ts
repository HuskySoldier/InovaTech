import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {
  @Input() colapsado = false;

  nombreCompleto = '';
  cargo = '';
  iniciales = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.nombreCompleto = this.authService.obtenerNombre();
    this.cargo = this.authService.obtenerCargo();
    this.iniciales = this.nombreCompleto
      .split(' ')
      .map(p => p[0])
      .slice(0, 2)
      .join('')
      .toUpperCase();
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
  }
}