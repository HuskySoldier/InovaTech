import { Component, OnInit, PLATFORM_ID, Inject, ChangeDetectorRef } from '@angular/core';
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
    private http: HttpClient,
    private cdr: ChangeDetectorRef
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

  // Gestión de roles
  usuarios: any[] = [];
  roles: any[] = [];
  cargandoRoles = false;

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
      this.notifEmail = localStorage.getItem('notifEmail') !== 'false';
      this.notifSistema = localStorage.getItem('notifSistema') !== 'false';
    }

    this.cargarCargos();
  }

  cargarCargos(): void {
    this.http.get<any[]>(`${environment.apiUrl}/cargos`).subscribe({
      next: (data) => { this.cargos = data; this.cdr.detectChanges(); },
      error: () => this.cargos = []
    });
  }

  cargarUsuariosYRoles(): void {
  this.cargandoRoles = true;
  this.cdr.detectChanges();
  this.http.get<any[]>(`${environment.apiUrl}/roles`).subscribe({
    next: (roles) => {
      this.roles = roles;
      this.http.get<any[]>(`${environment.apiUrl}/usuarios`).subscribe({
        next: (usuarios) => {
          console.log('Usuarios obtenidos:', usuarios);
          this.usuarios = usuarios;
          this.cargandoRoles = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.cargandoRoles = false;
          this.cdr.detectChanges();
        }
      });
    },
    error: () => {
      this.cargandoRoles = false;
      this.cdr.detectChanges();
    }
  });
}

  getNombreRol(idRol: number): string {
    const rol = this.roles.find(r => r.idRol === idRol);
    return rol?.nombre ?? 'Sin rol';
  }

  getColorRol(idRol: number): string {
    const colores: any = { 1: '#1A2B4C', 2: '#00A8E8', 3: '#28A745' };
    return colores[idRol] ?? '#6c757d';
  }

  cambiarRolUsuario(usuario: any): void {
    // Por ahora solo actualiza visualmente
    // Cuando el backend tenga el endpoint PUT /api/usuarios/{id}/rol se conecta aquí
    this.cdr.detectChanges();
  }

  cambiarPestana(pestana: string) {
    this.pestanaActiva = pestana;
    if (pestana === 'roles' && this.usuarios.length === 0) {
      this.cargarUsuariosYRoles();
    }
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
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('notifEmail', String(this.notifEmail));
      localStorage.setItem('notifSistema', String(this.notifSistema));
    }
    alert('Cambios guardados correctamente');
  }
}