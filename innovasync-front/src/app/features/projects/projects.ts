import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProyectosService } from '../../core/services/projects';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './projects.html',
  styleUrl: './projects.css'
})
export class Projects implements OnInit {

  mostrarModal = false;
  cargando = false;
  guardando = false;
  error = '';
  exito = '';

  nuevoProyecto = {
    nombre: '',
    descripcion: '',
    fInicio: '',
    fTerminoEsti: '',
    presuEstimado: 0,
    idEstado: 1
  };

  columnas = [
    { titulo: 'Por Hacer',   color: '#6c757d', idEstado: 1, tareas: [] as any[] },
    { titulo: 'En Progreso', color: '#FFC107', idEstado: 2, tareas: [] as any[] },
    { titulo: 'En Revisión', color: '#00A8E8', idEstado: 3, tareas: [] as any[] },
    { titulo: 'Terminado',   color: '#28A745', idEstado: 4, tareas: [] as any[] }
  ];

  constructor(private proyectosService: ProyectosService) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos(): void {
    this.cargando = true;
    this.proyectosService.obtenerTodos().subscribe({
      next: (data: any[]) => {
        this.columnas.forEach(col => col.tareas = []);
        data.forEach(p => {
          const col = this.columnas.find(c => c.idEstado === p.idEstado);
          if (col) {
            col.tareas.push({
              titulo: p.nombre,
              descripcion: p.descripcion,
              fecha: p.fTerminoEsti ?? '—',
              responsable: '—',
              prioridad: 'Media',
              colorPrioridad: '#FFC107',
              idProyecto: p.idProyecto
            });
          }
        });
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los proyectos.';
        this.cargando = false;
      }
    });
  }

  abrirModal(): void {
    this.mostrarModal = true;
    this.exito = '';
    this.error = '';
    this.nuevoProyecto = {
      nombre: '',
      descripcion: '',
      fInicio: '',
      fTerminoEsti: '',
      presuEstimado: 0,
      idEstado: 1
    };
  }

  cerrarModal(): void {
    this.mostrarModal = false;
  }

  crearProyecto(): void {
    if (!this.nuevoProyecto.nombre || !this.nuevoProyecto.fInicio || !this.nuevoProyecto.fTerminoEsti) {
      this.error = 'Por favor completa los campos obligatorios.';
      return;
    }

    this.guardando = true;
    this.error = '';

    this.proyectosService.crear(this.nuevoProyecto).subscribe({
      next: () => {
        this.guardando = false;
        this.exito = '¡Proyecto creado correctamente!';
        this.cerrarModal();
        this.cargarProyectos();
      },
      error: () => {
        this.error = 'Error al crear el proyecto. Intenta de nuevo.';
        this.guardando = false;
      }
    });
  }
}