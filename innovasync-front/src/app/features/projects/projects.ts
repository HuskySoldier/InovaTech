import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ProyectosService } from '../../core/services/projects';
import { AuthService } from '../../core/services/auth';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './projects.html',
  styleUrl: './projects.css'
})
export class Projects implements OnInit {

  cargando = true;
  guardando = false;
  guardandoDetalle = false;
  guardandoDetalleTarea = false;
  error = '';
  exito = '';
  esAdmin = false;
  esGestor = false;
  esColaborador = false;
  puedeCrear = false;
  editandoDetalle = false;
  editandoDetalleTarea = false;
  

  proyectos: any[] = [];
  misIdProyectos: number[] = [];
  proyectoSeleccionado: any = null;
  asignaciones: any[] = [];
  usuarios: any[] = [];

  mostrarModalProyecto = false;
  mostrarModalTarea = false;
  mostrarModalDetalle = false;
  mostrarModalDetalleTarea = false;
  proyectoDetalle: any = null;
  tareaDetalle: any = null;

  nuevoProyecto = {
    nombre: '',
    descripcion: '',
    fechaInicio: '',
    fechaTerminoEsti: '',
    presuEstimado: 0,
    idEstado: 4
  };

  nuevaTarea = {
    nombre: '',
    descripcion: '',
    fLimiteTerm: '',
    presupuestoAsignado: 0,
    idPrioridad: 1,
    idEstado: 8,
    proyectoId: null as number | null
  };

  prioridades: any[] = [];

  columnas = [
    { titulo: 'Pendiente',   color: '#6c757d', idEstado: 8,  tareas: [] as any[] },
    { titulo: 'En Progreso', color: '#FFC107', idEstado: 9,  tareas: [] as any[] },
    { titulo: 'Completada',  color: '#28A745', idEstado: 10, tareas: [] as any[] },
    { titulo: 'Bloqueada',   color: '#DC3545', idEstado: 11, tareas: [] as any[] }
  ];

  draggingTarea: any = null;
  draggingFromCol: any = null;

  private tareasUrl = `${environment.apiUrl}/tareas`;
  private prioridadesUrl = `${environment.apiUrl}/prioridades`;
  private proyectosUrl = `${environment.apiUrl}/proyectos`;

  constructor(
    private proyectosService: ProyectosService,
    private authService: AuthService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      if (isPlatformBrowser(this.platformId)) {
        const rol = this.authService.obtenerRol();
        this.esAdmin = rol === '1';
        this.esGestor = rol === '2';
        this.esColaborador = rol === '3';
        this.puedeCrear = rol === '1' || rol === '2';
        this.cdr.detectChanges();
        setTimeout(() => {
          this.cargarProyectos();
          this.cargarPrioridades();
          this.cargarAsignaciones();
          this.cargarUsuarios();
        }, 300);
      } else {
        this.cargando = false;
      }
    }, 100);
  }

  cargarProyectos(): void {
    this.cargando = true;
    this.proyectosService.obtenerTodos().subscribe({
      next: (data: any[]) => {
        if (this.esColaborador) {
          const idUser = this.authService.obtenerIdUser();
          this.http.get<any[]>(`${environment.apiUrl}/equipos`).subscribe({
            next: (equipos) => {
              const misEquipos = equipos.filter(e =>
                e.integrantes?.some((i: any) => i.idUser === idUser)
              );
              const misIdProyectos = misEquipos.map(e => e.idProyecto);
              this.proyectos = data.filter(p => misIdProyectos.includes(p.idProyecto));
              this.cargando = false;
              this.cdr.detectChanges();
            },
            error: () => {
              this.proyectos = data;
              this.cargando = false;
              this.cdr.detectChanges();
            }
          });
        } else if (this.esGestor) {
          const idUser = this.authService.obtenerIdUser();
          this.http.get<any[]>(`${environment.apiUrl}/equipos`).subscribe({
            next: (equipos) => {
              const misEquipos = equipos.filter(e =>
                e.integrantes?.some((i: any) => i.idUser === idUser)
              );
              this.misIdProyectos = misEquipos.map(e => e.idProyecto);
              this.proyectos = data;
              this.cargando = false;
              this.cdr.detectChanges();
            },
            error: () => {
              this.proyectos = data;
              this.cargando = false;
              this.cdr.detectChanges();
            }
          });
        } else {
          this.proyectos = data;
          this.cargando = false;
          this.cdr.detectChanges();
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar los proyectos.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  esMiProyecto(idProyecto: number): boolean {
    return this.esGestor && this.misIdProyectos.includes(idProyecto);
  }

  seleccionarProyecto(proyecto: any): void {
    this.proyectoSeleccionado = proyecto;
    this.cargarTareas(proyecto.idProyecto);
  }

  // ===== MODAL DETALLE PROYECTO =====
  abrirModalDetalle(event: Event, proyecto: any): void {
    event.stopPropagation();
    this.editandoDetalle = false;
    this.http.get<any>(`${this.proyectosUrl}/detalle/${proyecto.idProyecto}`).subscribe({
      next: (detalle) => {
        this.proyectoDetalle = {
          idProyecto: proyecto.idProyecto,
          idEstado: proyecto.idEstado,
          nombre: detalle.nombre,
          descripcion: detalle.descripcion,
          fechaInicio: detalle.fechaInicio,
          fechaTerminoEsti: detalle.fechaTermEst,
          presuEstimado: detalle.presupuestoEst
        };
        this.mostrarModalDetalle = true;
        this.cdr.detectChanges();
      },
      error: () => {
        this.proyectoDetalle = {
          idProyecto: proyecto.idProyecto,
          idEstado: proyecto.idEstado,
          nombre: proyecto.nombre,
          descripcion: proyecto.descripcion,
          fechaInicio: proyecto.fechaInicio,
          fechaTerminoEsti: proyecto.fechaTerminoEsti,
          presuEstimado: proyecto.presuEstimado ?? 0
        };
        this.mostrarModalDetalle = true;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarModalDetalle(): void {
    this.mostrarModalDetalle = false;
    this.proyectoDetalle = null;
    this.editandoDetalle = false;
  }

  guardarDetalleProyecto(): void {
    if (!this.proyectoDetalle.nombre || !this.proyectoDetalle.fechaInicio || !this.proyectoDetalle.fechaTerminoEsti) {
      this.error = 'Por favor completa los campos obligatorios.';
      return;
    }
    this.guardandoDetalle = true;
    const body = {
      nombre: this.proyectoDetalle.nombre,
      descripcion: this.proyectoDetalle.descripcion,
      fechaInicio: this.proyectoDetalle.fechaInicio,
      fechaTerminoEsti: this.proyectoDetalle.fechaTerminoEsti,
      presuEstimado: this.proyectoDetalle.presuEstimado,
      idEstado: this.proyectoDetalle.idEstado
    };
    this.http.put(`${this.proyectosUrl}/${this.proyectoDetalle.idProyecto}`, body).subscribe({
      next: () => {
        this.guardandoDetalle = false;
        this.exito = '¡Proyecto actualizado correctamente!';
        this.cerrarModalDetalle();
        this.cargarProyectos();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al actualizar el proyecto.';
        this.guardandoDetalle = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ===== MODAL DETALLE TAREA =====
  abrirModalDetalleTarea(event: Event, tarea: any): void {
    event.stopPropagation();
    this.editandoDetalleTarea = false;
    this.http.get<any>(`${this.tareasUrl}/detalle/${tarea.id}`).subscribe({
      next: (detalle) => {
        this.tareaDetalle = {
          id: tarea.id,
          nombre: detalle.nombre,
          descripcion: detalle.descripcion,
          fLimiteTerm: detalle.fLimiteTerm,
          presupuestoAsignado: detalle.presupuestoAsignado,
          proyectoNombre: detalle.proyectoNombre,
          idPrioridad: tarea.idPrioridad ?? 1,
          idEstado: tarea.idEstado,
          proyectoId: this.proyectoSeleccionado?.idProyecto ?? null,
          idIntegrante: this.asignaciones.find(a => a.idTarea === tarea.id)?.idIntegrante ?? null
        };
        this.mostrarModalDetalleTarea = true;
        this.cdr.detectChanges();
      },
      error: () => {
        this.tareaDetalle = {
          id: tarea.id,
          nombre: tarea.titulo,
          descripcion: tarea.descripcion,
          fLimiteTerm: tarea.fecha,
          presupuestoAsignado: 0,
          proyectoNombre: this.proyectoSeleccionado?.nombre ?? '',
          idPrioridad: tarea.idPrioridad ?? 1,
          idEstado: tarea.idEstado,
          proyectoId: this.proyectoSeleccionado?.idProyecto ?? null
        };
        this.mostrarModalDetalleTarea = true;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarModalDetalleTarea(): void {
    this.mostrarModalDetalleTarea = false;
    this.tareaDetalle = null;
    this.editandoDetalleTarea = false;
  }

  guardarDetalleTarea(): void {
    if (!this.tareaDetalle.nombre || !this.tareaDetalle.fLimiteTerm) {
      this.error = 'Por favor completa los campos obligatorios.';
      if (this.tareaDetalle.idIntegrante){
        this.http.post(`${environment.apiUrl}/asignaciones/tarea/${this.tareaDetalle.id}/integrante/${this.tareaDetalle.idIntegrante}`, {}).subscribe({
        });
      }
      return;
    }
    this.guardandoDetalleTarea = true;
    const body = {
      nombre: this.tareaDetalle.nombre,
      descripcion: this.tareaDetalle.descripcion,
      fLimiteTerm: this.tareaDetalle.fLimiteTerm,
      presupuestoAsignado: this.tareaDetalle.presupuestoAsignado,
      idPrioridad: this.tareaDetalle.idPrioridad,
      idEstado: this.tareaDetalle.idEstado,
      proyectoId: this.tareaDetalle.proyectoId
    };
    this.http.put(`${this.tareasUrl}/${this.tareaDetalle.id}`, body).subscribe({
      next: () => {
        this.guardandoDetalleTarea = false;
        this.exito = '¡Tarea actualizada correctamente!';
        this.cerrarModalDetalleTarea();
        if (this.proyectoSeleccionado) this.cargarTareas(this.proyectoSeleccionado.idProyecto);
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al actualizar la tarea.';
        this.guardandoDetalleTarea = false;
        this.cdr.detectChanges();
      }
    });
  }

  getEstadoProyecto(idEstado: number): string {
    const estados: any = { 4: 'En Planificación', 5: 'En Progreso', 6: 'Completado', 7: 'Cancelado' };
    return estados[idEstado] ?? 'Desconocido';
  }

  getColorEstadoProyecto(idEstado: number): string {
    const colores: any = { 4: '#6c757d', 5: '#FFC107', 6: '#28A745', 7: '#DC3545' };
    return colores[idEstado] ?? '#6c757d';
  }

  getNombreEstadoTarea(idEstado: number): string {
    const estados: any = { 8: 'Pendiente', 9: 'En Progreso', 10: 'Completada', 11: 'Bloqueada' };
    return estados[idEstado] ?? 'Desconocido';
  }

  cargarTareas(idProyecto: number): void {
    this.columnas.forEach(col => col.tareas = []);
    this.http.get<any[]>(`${this.tareasUrl}/proyecto/${idProyecto}`).subscribe({
      next: (data) => {
        data.forEach(t => {
          const col = this.columnas.find(c => c.idEstado === t.idEstado);
          if (col) {
            col.tareas.push({
              id: t.id,
              titulo: t.nombre,
              descripcion: t.descripcion,
              fecha: t.fLimiteTerm ?? '—',
              prioridad: this.getPrioridad(t.idPrioridad),
              colorPrioridad: this.getColorPrioridad(t.idPrioridad),
              idEstado: t.idEstado,
              idPrioridad: t.idPrioridad
            });
          }
        });
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  cargarPrioridades(): void {
    this.http.get<any[]>(this.prioridadesUrl).subscribe({
      next: (data) => { this.prioridades = data; this.cdr.detectChanges(); },
      error: () => this.prioridades = [{ id: 1, nombre: 'Alta' }, { id: 2, nombre: 'Media' }, { id: 3, nombre: 'Baja' }]
    });
  }

  cargarAsignaciones(): void {
    this.http.get<any[]>(`${environment.apiUrl}/asignaciones`).subscribe({
      next: (data) => { this.asignaciones = data; this.cdr.detectChanges(); },
      error: () => this.asignaciones = []
    });
  }

  cargarUsuarios(): void {
    this.http.get<any[]>(`${environment.apiUrl}/usuarios`).subscribe({
      next: (data) => { this.usuarios = data; this.cdr.detectChanges(); },
      error: () => this.usuarios = []
    });
  }

  getAsignadosTarea(idTarea: number): string {
    const asigs = this.asignaciones.filter(a => a.idTarea === idTarea);
    if (asigs.length === 0) return 'Sin asignar';
    return asigs.map(a => {
      const u = this.usuarios.find(u => u.idUser === a.idIntegrante);
      return u ? u.nombreCompleto : `Usuario ${a.idIntegrante}`;
    }).join(', ');
  }

  getPrioridad(idPrioridad: number): string {
    const p = this.prioridades.find(x => x.id === idPrioridad);
    return p?.nombre ?? 'Media';
  }

  getColorPrioridad(idPrioridad: number): string {
    const colores: any = { 1: '#DC3545', 2: '#FFC107', 3: '#28A745' };
    return colores[idPrioridad] ?? '#FFC107';
  }

  onDragStart(tarea: any, columna: any): void {
    this.draggingTarea = tarea;
    this.draggingFromCol = columna;
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(columnaDestino: any): void {
    if (!this.draggingTarea || this.draggingFromCol === columnaDestino) return;
    this.http.put(`${this.tareasUrl}/${this.draggingTarea.id}`, {
      nombre: this.draggingTarea.titulo,
      descripcion: this.draggingTarea.descripcion,
      fLimiteTerm: this.draggingTarea.fecha,
      presupuestoAsignado: 0,
      idPrioridad: this.draggingTarea.idPrioridad ?? 1,
      idEstado: columnaDestino.idEstado,
      proyectoId: this.proyectoSeleccionado.idProyecto
    }).subscribe({
      next: () => {
        this.draggingFromCol.tareas = this.draggingFromCol.tareas.filter((t: any) => t.id !== this.draggingTarea.id);
        this.draggingTarea.idEstado = columnaDestino.idEstado;
        columnaDestino.tareas.push(this.draggingTarea);
        this.draggingTarea = null;
        this.draggingFromCol = null;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo mover la tarea.';
        this.draggingTarea = null;
        this.draggingFromCol = null;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalProyecto(): void {
    this.mostrarModalProyecto = true;
    this.exito = '';
    this.error = '';
    this.nuevoProyecto = { nombre: '', descripcion: '', fechaInicio: '', fechaTerminoEsti: '', presuEstimado: 0, idEstado: 4 };
  }

  cerrarModalProyecto(): void {
    this.mostrarModalProyecto = false;
  }

  crearProyecto(): void {
    if (!this.nuevoProyecto.nombre || !this.nuevoProyecto.fechaInicio || !this.nuevoProyecto.fechaTerminoEsti) {
      this.error = 'Por favor completa los campos obligatorios.';
      return;
    }
    if (this.nuevoProyecto.presuEstimado < 0 || !Number.isInteger(this.nuevoProyecto.presuEstimado)) {
      this.error = 'El presupuesto debe ser un número entero positivo.';
      return;
    }
    this.guardando = true;
    this.error = '';
    this.proyectosService.crear(this.nuevoProyecto).subscribe({
      next: () => {
        this.guardando = false;
        this.exito = '¡Proyecto creado correctamente!';
        this.cerrarModalProyecto();
        this.cargarProyectos();
      },
      error: () => {
        this.error = 'Error al crear el proyecto.';
        this.guardando = false;
      }
    });
  }

  abrirModalTarea(): void {
    this.mostrarModalTarea = true;
    this.error = '';
    this.nuevaTarea = {
      nombre: '',
      descripcion: '',
      fLimiteTerm: '',
      presupuestoAsignado: 0,
      idPrioridad: 1,
      idEstado: 8,
      proyectoId: this.proyectoSeleccionado?.idProyecto ?? null
    };
  }

  cerrarModalTarea(): void {
    this.mostrarModalTarea = false;
  }

  crearTarea(): void {
    if (!this.nuevaTarea.nombre || !this.nuevaTarea.fLimiteTerm) {
      this.error = 'Por favor completa los campos obligatorios.';
      return;
    }
    if (this.nuevaTarea.presupuestoAsignado < 0 || !Number.isInteger(this.nuevaTarea.presupuestoAsignado)) {
      this.error = 'El presupuesto debe ser un número entero positivo.';
      return;
    }
    this.guardando = true;
    this.http.post(this.tareasUrl, this.nuevaTarea).subscribe({
      next: () => {
        this.guardando = false;
        this.exito = '¡Tarea creada correctamente!';
        this.cerrarModalTarea();
        if (this.proyectoSeleccionado) this.cargarTareas(this.proyectoSeleccionado.idProyecto);
      },
      error: () => {
        this.error = 'Error al crear la tarea.';
        this.guardando = false;
      }
    });
  }
}