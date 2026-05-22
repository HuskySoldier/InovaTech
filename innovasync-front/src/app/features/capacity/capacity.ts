import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserService } from '../../core/services/users';
import {NgZone} from '@angular/core';

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
  utilizacionGlobal = 0;
  cargando = false;
  error = '';
  exito = '';

  mostrarModal = false;
  guardando = false;
  errorModal = '';

  nuevoEquipo = {
    nombre: '',
    idProyecto: null as number | null
  };

  private equiposUrl = `${environment.apiUrl}/equipos`;
  private proyectosUrl = `${environment.apiUrl}/proyectos`;

  constructor(
  private userService: UserService,
  private http: HttpClient,
  private cdr: ChangeDetectorRef,
  private ngZone: NgZone,
  @Inject(PLATFORM_ID) private platformId: Object
) {}

  ngOnInit(): void {
  setTimeout(() => {
    this.cargarUsuarios();
    this.cargarProyectos();
  });
}

  cargarUsuarios(): void {
  this.cargando = true;
  this.userService.obtenerTodos().subscribe({
    next: (data: any[]) => {
      this.ngZone.run(() => {
        this.profesionales = data.map(u => ({
          nombre: u.nombreCompleto,
          cargo: u.nombreCargo,
          avatar: this.getIniciales(u.nombreCompleto),
          asignaciones: Array(5).fill({ proyecto: '', horas: 0, color: '' })
        }));
        this.utilizacionGlobal = this.calcularUtilizacion();
        this.cargando = false;
        
      });
    },
    error: () => {
      this.ngZone.run(() => {
        this.error = 'No se pudieron cargar los usuarios.';
        this.cargando = false;
      });
    }
  });
}

  cargarProyectos(): void {
    this.http.get<any[]>(this.proyectosUrl).subscribe({
      next: (data) => {
        this.proyectos = data;
        
      },
      error: () => this.proyectos = []
    });
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
    this.nuevoEquipo = { nombre: '', idProyecto: null };
    
  });
}

  cerrarModal(): void {
    this.mostrarModal = false;
  }

  crearEquipo(): void {
    if (!this.nuevoEquipo.nombre || !this.nuevoEquipo.idProyecto) {
      this.errorModal = 'Por favor completa todos los campos.';
      return;
    }

    this.guardando = true;
    this.errorModal = '';

    this.http.post(this.equiposUrl, this.nuevoEquipo).subscribe({
      next: () => {
        this.guardando = false;
        this.exito = '¡Equipo creado correctamente!';
        this.cerrarModal();
        
      },
      error: () => {
        this.errorModal = 'Error al crear el equipo. Intenta de nuevo.';
        this.guardando = false;
        
      }
    });
  }
}