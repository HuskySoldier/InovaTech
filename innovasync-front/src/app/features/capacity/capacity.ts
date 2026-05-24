import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserService } from '../../core/services/users';
import { AuthService } from '../../core/services/auth';
import { retry } from 'rxjs/operators';

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
  utilizacionGlobal = 0;
  cargando = false;
  error = '';
  exito = '';
  esAdmin = false;
  puedeCrear = false;

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
    const rol = this.authService.obtenerRol();
    this.esAdmin = rol === '1';
    this.puedeCrear = rol === '1' || rol === '2';

    setTimeout(() => {
      this.cargando = true;
      this.cdr.detectChanges();
      this.cargarUsuarios();
      this.cargarProyectos();
  
    }, 100);
  }

  cargarUsuarios(): void {
    this.userService.obtenerTodos().pipe(retry(3)).subscribe({
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
        if (this.esAdmin) 
          this.cargarEquipos();
      },
      error: () => {
        this.proyectos = [];
        this.cdr.detectChanges();
      }
    });
  }

  cargarEquipos(): void {
    this.http.get<any[]>(this.equiposUrl).subscribe({
      next: (data) => {
        this.equipos = data.map(e => ({
          ...e,
          nombreProyecto: this.proyectos.find(p => p.idProyecto === e.idProyecto)?.nombre ?? `Proyecto ${e.idProyecto}`
        }));
        this.cdr.detectChanges();
      },
      error: () => {
        this.equipos = [];
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
          this.cargarUsuarios();
          if (this.esAdmin) this.cargarEquipos();
          this.cdr.detectChanges();
          return;
        }

        // Agregar integrantes uno por uno
        let completados = 0;
        this.integrantesSeleccionados.forEach(idUser => {
          this.http.post(`${this.equiposUrl}/${idEquipo}/integrantes/${idUser}`, {}).subscribe({
            next: () => {
              completados++;
              if (completados === this.integrantesSeleccionados.length) {
                this.guardando = false;
                this.exito = '¡Equipo creado con integrantes!';
                this.cerrarModal();
                this.cargarUsuarios();
                if (this.esAdmin) this.cargarEquipos();
                this.cdr.detectChanges();
              }
            },
            error: () => {
              completados++;
              if (completados === this.integrantesSeleccionados.length) {
                this.guardando = false;
                this.exito = '¡Equipo creado! (algunos integrantes no se pudieron agregar)';
                this.cerrarModal();
                if (this.esAdmin) this.cargarEquipos();
                this.cdr.detectChanges();
              }
            }
          });
        });
      },
      error: () => {
        this.errorModal = 'Error al crear el equipo. Intenta de nuevo.';
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }
}