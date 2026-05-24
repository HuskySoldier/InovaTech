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
  todosProfesionales: any[] = [];
  proyectos: any[] = [];
  equipos: any[] = [];
  misEquipos: any[] = [];
  utilizacionGlobal = 0;
  cargando = false;
  error = '';
  exito = '';
  esAdmin = false;
  esGestor = false;
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
  private tareasUrl = `${environment.apiUrl}/tareas`;

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
      this.esGestor = rol === '2';
      this.esColaborador = rol === '3';
      this.puedeCrear = rol === '1' || rol === '2';
      this.idUserActual = this.authService.obtenerIdUser();
      this.cargando = true;
      this.cdr.detectChanges();
      this.cargarUsuarios();
      this.cargarProyectos();
    }, 100);
  }

  cargarUsuarios(): void {
    this.userService.obtenerTodos().subscribe({
      next: (data: any[]) => {
        this.todosProfesionales = data.map(u => ({
          nombre: u.nombreCompleto,
          cargo: u.nombreCargo,
          avatar: this.getIniciales(u.nombreCompleto),
          idUser: u.idUser,
          asignaciones: Array(5).fill({ proyecto: '', horas: 0, color: '' })
        }));

        if (this.esAdmin) {
          this.profesionales = this.todosProfesionales;
        }

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
        setTimeout(() => this.cargarEquipos(), 500);
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
        const equiposMapeados = data.map(e => ({
          ...e,
          nombreProyecto: this.proyectos.find(p => p.idProyecto === e.idProyecto)?.nombre ?? `Proyecto ${e.idProyecto}`,
          avance: 0
        }));

        this.equipos = equiposMapeados;

        this.misEquipos = equiposMapeados.filter(e =>
          e.integrantes?.some((i: any) => i.idUser === this.idUserActual)
        );

        this.misEquipos.forEach(equipo => {
          this.calcularAvanceProyecto(equipo);
        });

        // Filtrar profesionales según equipos para gestor y colaborador
        if (!this.esAdmin) {
          const idsIntegrantes = this.misEquipos
            .flatMap(e => e.integrantes?.map((i: any) => i.idUser) ?? []);
          this.profesionales = this.todosProfesionales.filter(p =>
            idsIntegrantes.includes(p.idUser)
          );
        }

        this.cdr.detectChanges();
      },
      error: () => {
        this.equipos = [];
        this.misEquipos = [];
        this.cdr.detectChanges();
      }
    });
  }

  calcularAvanceProyecto(equipo: any): void {
    this.http.get<any[]>(`${this.tareasUrl}/proyecto/${equipo.idProyecto}`).subscribe({
      next: (tareas) => {
        const total = tareas.length;
        const completadas = tareas.filter(t => t.idEstado === 10).length;
        equipo.avance = total > 0 ? Math.round((completadas / total) * 100) : 0;
        equipo.totalTareas = total;
        equipo.tareasCompletadas = completadas;
        this.cdr.detectChanges();
      },
      error: () => {
        equipo.avance = 0;
        this.cdr.detectChanges();
      }
    });
  }

  getColorAvance(avance: number): string {
    if (avance >= 75) return '#28A745';
    if (avance >= 40) return '#FFC107';
    return '#DC3545';
  }

  getNombreIntegrante(idUser: number): string {
    const p = this.todosProfesionales.find(u => u.idUser === idUser);
    return p?.nombre ?? `Usuario ${idUser}`;
  }

  getCargoIntegrante(idUser: number): string {
    const p = this.todosProfesionales.find(u => u.idUser === idUser);
    return p?.cargo ?? '';
  }

  getInicialesIntegrante(idUser: number): string {
    const nombre = this.getNombreIntegrante(idUser);
    return this.getIniciales(nombre);
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
      },
      error: () => {
        this.errorModal = 'Error al crear el equipo. Intenta de nuevo.';
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }
}