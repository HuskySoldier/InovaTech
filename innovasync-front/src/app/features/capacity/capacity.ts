import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserService } from '../../core/services/users';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-capacity',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './capacity.html',
  styleUrl: './capacity.css',
})
export class Capacity implements OnInit {

  semanas = ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4', 'Semana 5'];
  profesionales: any[] = [];
  proyectos: any[] = [];
  equipos: any[] = [];
  misEquipos: any[] = [];
  utilizacionGlobal = 0;
  cargando = false;
  error = '';
  exito = '';
  esAdmin = false;
  esColaborador = false;
  puedeCrear = false;
  idUserActual = 0;

  mostrarModal = false;
  guardando = false;
  errorModal = '';

  nuevoEquipo = {
    nombre: '',
    idProyecto: null as number | null
  };

  integrantesSeleccionados: number[] = [];

  private equiposUrl = `${environment.apiUrl}/equipos`;
  private proyectosUrl = `${environment.apiUrl}/proyectos`;

  constructor(
    private userService: UserService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      const rol = this.authService.obtenerRol();
      this.esAdmin = rol === '1';
      this.esColaborador = rol === '3';
      this.puedeCrear = rol === '1' || rol === '2';
      this.idUserActual = this.authService.obtenerIdUser();
      this.cargando = true;
      this.cdr.detectChanges();
      this.cargarUsuarios();
      this.cargarProyectos();
    }, 300);
  }

  cargarUsuarios(): void {
    this.userService.obtenerTodos().subscribe({
      next: (data: any[]) => {
        this.profesionales = data.map(u => ({
          nombre: u.nombreCompleto,
          cargo: u.nombreCargo,
          avatar: this.getIniciales(u.nombreCompleto),
          idUser: u.idUser,
          asignaciones: Array(5).fill({ proyecto: '', horas: 0, color: '' })
        }));
        this.utilizacionGlobal = this.calcularUtilizacion();
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar los usuarios.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarProyectos(): void {
    this.http.get<any[]>(this.proyectosUrl).subscribe({
      next: (data) => {
        this.proyectos = data;
        this.cdr.detectChanges();
        setTimeout(() => this.cargarEquipos(), 800);
      },
      error: () => {
        this.proyectos = [];
        this.cdr.detectChanges();
        setTimeout(() => this.cargarEquipos(), 800);
      }
    });
  }

  cargarEquipos(): void {
    this.http.get<any[]>(this.equiposUrl).subscribe({
      next: (data) => {
        const equiposMapeados = data.map(e => ({
          ...e,
          nombreProyecto: this.proyectos.find(p => p.idProyecto === e.idProyecto)?.nombre ?? `Proyecto ${e.idProyecto}`
        }));
        this.equipos = equiposMapeados;
        this.misEquipos = equiposMapeados.filter(e =>
          e.integrantes?.some((i: any) => i.idUser === this.idUserActual)
        );
        this.cdr.detectChanges();
      },
      error: () => {
        this.equipos = [];
        this.misEquipos = [];
        this.cdr.detectChanges();
      }
    });
  }

  toggleIntegrante(idUser: number): void {
    const idx = this.integrantesSeleccionados.indexOf(idUser);
    if (idx === -1) {
      this.integrantesSeleccionados.push(idUser);
    } else {
      this.integrantesSeleccionados.splice(idx, 1);
    }
  }

  estaSeleccionado(idUser: number): boolean {
    return this.integrantesSeleccionados.includes(idUser);
  }

  getIniciales(nombre: string): string {
    return nombre.split(' ').map(p => p[0]).slice(0, 2).join('').toUpperCase();
  }

  calcularUtilizacion(): number {
    const total = this.profesionales.length;
    if (total === 0) return 0;
    const asignados = this.profesionales.filter(p =>
      p.asignaciones.some((a: any) => a.proyecto !== '')
    ).length;
    return Math.round((asignados / total) * 100);
  }

  abrirModal(): void {
    if (!this.puedeCrear) return;
    setTimeout(() => {
      this.mostrarModal = true;
      this.errorModal = '';
      this.exito = '';
      this.integrantesSeleccionados = [];
      this.nuevoEquipo = { nombre: '', idProyecto: null };
      this.cdr.detectChanges();
    });
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.cdr.detectChanges();
  }

  crearEquipo(): void {
    if (!this.nuevoEquipo.nombre || !this.nuevoEquipo.idProyecto) {
      this.errorModal = 'Por favor completa todos los campos obligatorios.';
      return;
    }

    this.guardando = true;
    this.errorModal = '';

    this.http.post<any>(this.equiposUrl, this.nuevoEquipo).subscribe({
      next: (equipoCreado) => {
        const idEquipo = equipoCreado.idEquipo;

        if (this.integrantesSeleccionados.length === 0) {
          this.guardando = false;
          this.exito = '¡Equipo creado correctamente!';
          this.cerrarModal();
          this.cargarProyectos();
          this.cdr.detectChanges();
          return;
        }

        // Esperar 1 segundo antes de agregar integrantes
        setTimeout(() => {
          let completados = 0;
          this.integrantesSeleccionados.forEach(idUser => {
            this.http.post(`${this.equiposUrl}/${idEquipo}/integrantes/${idUser}`, {}).subscribe({
              next: () => {
                completados++;
                if (completados === this.integrantesSeleccionados.length) {
                  this.guardando = false;
                  this.exito = '¡Equipo creado con integrantes!';
                  this.cerrarModal();
                  this.cargarProyectos();
                  this.cdr.detectChanges();
                }
              },
              error: () => {
                completados++;
                if (completados === this.integrantesSeleccionados.length) {
                  this.guardando = false;
                  this.exito = '¡Equipo creado! (algunos integrantes no se pudieron agregar)';
                  this.cerrarModal();
                  this.cargarProyectos();
                  this.cdr.detectChanges();
                }
              }
            });
          });
        }, 1000);
      },
      error: () => {
        this.errorModal = 'Error al crear el equipo. Intenta de nuevo.';
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }
}