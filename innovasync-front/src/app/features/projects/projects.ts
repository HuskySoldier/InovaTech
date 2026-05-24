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

  // Estado general
  cargando = true;
  guardando = false;
  error = '';
  exito = '';
  esAdmin = false;
  esGestor = false;
  esColaborador = false;
  puedeCrear = false;

  // Proyectos
  proyectos: any[] = [];
  proyectoSeleccionado: any = null;

  // Modales
  mostrarModalProyecto = false;
  mostrarModalTarea = false;

  // Nuevo proyecto
  nuevoProyecto = {
    nombre: '',
    descripcion: '',
    fechaInicio: '',
    fechaTerminoEsti: '',
    presuEstimado: 0,
    idEstado: 4
  };

  // Nueva tarea
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

  // Kanban de tareas
  columnas = [
    { titulo: 'Pendiente',   color: '#6c757d', idEstado: 8,  tareas: [] as any[] },
    { titulo: 'En Progreso', color: '#FFC107', idEstado: 9,  tareas: [] as any[] },
    { titulo: 'Completada',  color: '#28A745', idEstado: 10, tareas: [] as any[] },
    { titulo: 'Bloqueada',   color: '#DC3545', idEstado: 11, tareas: [] as any[] }
  ];

  // Drag & Drop
  draggingTarea: any = null;
  draggingFromCol: any = null;

  private tareasUrl = `${environment.apiUrl}/tareas`;
  private prioridadesUrl = `${environment.apiUrl}/prioridades`;

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
      }, 300);
    } else {
      this.cargando = false;
    }
  }, 100);
}

  // ===== PROYECTOS =====
  cargarProyectos(): void {
  this.cargando = true;
  this.proyectosService.obtenerTodos().subscribe({
    next: (data: any[]) => {
      if (this.esColaborador) {
        // Cargar equipos para filtrar proyectos del colaborador
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

  seleccionarProyecto(proyecto: any): void {
    this.proyectoSeleccionado = proyecto;
    this.cargarTareas(proyecto.idProyecto);
  }

  getEstadoProyecto(idEstado: number): string {
    const estados: any = { 4: 'En Planificación', 5: 'En Progreso', 6: 'Completado', 7: 'Cancelado' };
    return estados[idEstado] ?? 'Desconocido';
  }

  getColorEstadoProyecto(idEstado: number): string {
    const colores: any = { 4: '#6c757d', 5: '#FFC107', 6: '#28A745', 7: '#DC3545' };
    return colores[idEstado] ?? '#6c757d';
  }

  // ===== TAREAS =====
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
              idEstado: t.idEstado
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

  getPrioridad(idPrioridad: number): string {
    const p = this.prioridades.find(x => x.id === idPrioridad);
    return p?.nombre ?? 'Media';
  }

  getColorPrioridad(idPrioridad: number): string {
    const colores: any = { 1: '#DC3545', 2: '#FFC107', 3: '#28A745' };
    return colores[idPrioridad] ?? '#FFC107';
  }

  // ===== DRAG & DROP =====
  onDragStart(tarea: any, columna: any): void {
    this.draggingTarea = tarea;
    this.draggingFromCol = columna;
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(columnaDestino: any): void {
    if (!this.draggingTarea || this.draggingFromCol === columnaDestino) return;

    // Actualizar en el backend
    this.http.put(`${this.tareasUrl}/${this.draggingTarea.id}`, {
      nombre: this.draggingTarea.titulo,
      descripcion: this.draggingTarea.descripcion,
      fLimiteTerm: this.draggingTarea.fecha,
      presupuestoAsignado: 0,
      idPrioridad: 1,
      idEstado: columnaDestino.idEstado,
      proyectoId: this.proyectoSeleccionado.idProyecto
    }).subscribe({
      next: () => {
        // Mover visualmente
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

  // ===== MODAL PROYECTO =====
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

  // ===== MODAL TAREA =====
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

    if (this.nuevaTarea.presupuestoAsignado < 0 || !Number.isInteger (this.nuevaTarea.presupuestoAsignado)) {
      this.error ='El presupuesto deber ser número entero positivo.';
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