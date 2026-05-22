import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-configuration',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './configuration.html',
  styleUrl: './configuration.css'
})
export class Configuration implements OnInit {

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  rolUsuario = '';
  nombre = '';
  email = '';
  cargo = '';
  temaOscuro = false;
  notifEmail = true;
  notifSistema = true;
  pestanaActiva = 'perfil';
  cargos: any[] = [];

  ngOnInit() {
    this.nombre = this.authService.obtenerNombre();
    this.cargo = this.authService.obtenerCargo();
    this.rolUsuario = this.authService.obtenerRol();

    if (isPlatformBrowser(this.platformId)) {
      this.email = localStorage.getItem('correo') || '';
      const tema = localStorage.getItem('tema');
      if (tema === 'oscuro') {
        this.temaOscuro = true;
        document.body.classList.add('tema-oscuro');
      }
    }

    this.cargarCargos();
  }

  cargarCargos(): void {
    this.http.get<any[]>(`${environment.apiUrl}/cargos`).subscribe({
      next: (data) => this.cargos = data,
      error: () => this.cargos = []
    });
  }

  cambiarPestana(pestana: string) {
    this.pestanaActiva = pestana;
  }

  cambiarTema() {
    if (isPlatformBrowser(this.platformId)) {
      if (this.temaOscuro) {
        document.body.classList.add('tema-oscuro');
        localStorage.setItem('tema', 'oscuro');
      } else {
        document.body.classList.remove('tema-oscuro');
        localStorage.setItem('tema', 'claro');
      }
    }
  }

  guardarCambios() {
    alert('Cambios guardados correctamente');
  }
}