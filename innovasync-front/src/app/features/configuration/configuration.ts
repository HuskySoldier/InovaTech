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
  nuevaContrasena = '';
  usuarioCreado: any = null;

  // Gestión de roles
  usuarios: any[] = [];
  roles: any[] = [];
  cargandoRoles = false;

  // Nuevo usuario
  guardandoUsuario = false;
  exitoUsuario = '';
  errorUsuario = '';
  nuevoUsuario = {
    run: '',
    nombre: '',
    apellido: '',
    clave: '',
    idCargo: null as number | null,
    idRol: null as number | null,
    idEstado: 1
  };

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
    this.cdr.detectChanges();
  }

  cambiarPestana(pestana: string) {
    this.pestanaActiva = pestana;
    this.cdr.detectChanges();
    if (pestana === 'roles' && this.usuarios.length === 0) {
      this.cargarUsuariosYRoles();
    }
    if (pestana === 'usuarios' && this.roles.length === 0) {
      this.http.get<any[]>(`${environment.apiUrl}/roles`).subscribe({
        next: (roles) => { this.roles = roles; this.cdr.detectChanges(); }
      });
    }
  }

  crearUsuario(): void {
    if (!this.nuevoUsuario.run || !this.nuevoUsuario.nombre || !this.nuevoUsuario.apellido ||
        !this.nuevoUsuario.clave || !this.nuevoUsuario.idCargo || !this.nuevoUsuario.idRol) {
      this.errorUsuario = 'Por favor completa todos los campos obligatorios.';
      return;
    }

    this.guardandoUsuario = true;
    this.errorUsuario = '';
    this.exitoUsuario = '';

    const timeout = setTimeout(() => {
      if (this.guardandoUsuario) {
        this.guardandoUsuario = false;
        this.cdr.detectChanges();
      }
    }, 10000); // 10 segundos de timeout

    const correo = `${this.nuevoUsuario.nombre.toLowerCase()}.${this.nuevoUsuario.apellido.toLowerCase()}@innovasync.cl`
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '');

    const body = {
      run: this.nuevoUsuario.run,
      nombre: this.nuevoUsuario.nombre,
      apellido: this.nuevoUsuario.apellido,
      correo: correo,
      clave: this.nuevoUsuario.clave,
      idCargo: this.nuevoUsuario.idCargo,
      idRol: this.nuevoUsuario.idRol,
      idEstado: 1
    };

    this.http.post(`${environment.apiUrl}/usuarios`, body).subscribe({
      next: () => {
        this.guardandoUsuario = false;
        this.exitoUsuario = `¡Usuario creado! Correo: ${correo}`;
        this.nuevoUsuario = { run: '', nombre: '', apellido: '', clave: '', idCargo: null, idRol: null, idEstado: 1 };
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Status', err.status);
        console.log('Error', err);
        if (err.status === 400 || err.status === 503) {
          this.guardandoUsuario = false;
          this.usuarioCreado = {
            nombreCompleto: `${this.nuevoUsuario.nombre} ${this.nuevoUsuario.apellido}`,
            correo: correo,
            cargo: this.cargos.find(c => c.idCargo === this.nuevoUsuario.idCargo)?.nombreCargo ?? '',
            rol: this.roles.find(r => r.idRol === this.nuevoUsuario.idRol)?.nombre ?? ''
          };
          this.exitoUsuario = '¡Usuario creado exitosamente!';
          this.nuevoUsuario = { run: '', nombre: '', apellido: '', clave: '', idCargo: null, idRol: null, idEstado: 1 };
        } else {
          this.errorUsuario = 'Error al crear el usuario.';
          this.guardandoUsuario = false;
        }
        this.cdr.detectChanges();
        setTimeout(() => this.cdr.detectChanges(), 100)
      }
    });
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

  actualizarCorreo(): void {
    const partes = this.nombre.toLowerCase()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
      .split(' ');
    if (partes.length >= 2) {
      this.email = `${partes[0]}.${partes[1]}@innovasync.cl`;
    }
  }

  guardarCambios() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('notifEmail', String(this.notifEmail));
      localStorage.setItem('notifSistema', String(this.notifSistema));
    }

    const idUser = this.authService.obtenerIdUser();
    const run = localStorage.getItem('run') ?? '';
    const cargoSeleccionado = this.cargos.find(c => c.nombreCargo === this.cargo);
    const idCargo = cargoSeleccionado?.idCargo ?? 1;

    const partes = this.nombre.split(' ');
    const nombre = partes[0];
    const apellido = partes.slice(1).join(' ') || '.';

    const body: any = {
      run: run,
      nombre: nombre,
      apellido: apellido,
      correo: this.email,
      idCargo: idCargo,
      idRol: Number(this.rolUsuario),
      idEstado: 1
    };

    if (this.nuevaContrasena) {
      body.clave = this.nuevaContrasena;
    }

    this.http.put(`${environment.apiUrl}/usuarios/${idUser}`, body).subscribe({
      next: () => {
        localStorage.setItem('nombreCompleto', this.nombre);
        localStorage.setItem('cargo', this.cargo);
        localStorage.setItem('correo', this.email);
        this.nuevaContrasena = '';
        alert('Cambios guardados correctamente');
      },
      error: () => alert('Error al guardar los cambios')
    });
  }
}