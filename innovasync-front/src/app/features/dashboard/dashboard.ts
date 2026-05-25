import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartData, ChartOptions, registerables } from 'chart.js';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth';
import { environment } from '../../../environments/environment';
import { UserService } from '../../core/services/users';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BaseChartDirective],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit { 

  nombreUsuario = '';
  rol = '';
  esAdmin = false;
  esGestor = false;
  esColaborador = false;
  cargando = false;
  idUserActual = 0;

  proyectos: any[] = [];
  misEquipos: any[] = [];

  kpis = [
    { titulo: 'Proyectos Activos', valor: '0', color: '#1A2B4C' },
    { titulo: 'Utilización de Recursos', valor: '0%', color: '#00A8E8' },
    { titulo: 'Tareas Críticas', valor: '0', color: '#DC3545' },
    { titulo: 'Presupuesto Restante', valor: '0%', color: '#28A745' }
  ];

  lineChartData: ChartData<'line'> = {
    labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
    datasets: [
      {
        data: [20, 35, 45, 55, 70, 85],
        label: 'Proyecto 1',
        borderColor: '#00A8E8',
        backgroundColor: 'rgba(0, 168, 232, 0.1)',
        fill: true,
        tension: 0.4
      },
      {
        data: [10, 25, 30, 50, 60, 75],
        label: 'Proyecto 2',
        borderColor: '#28A745',
        backgroundColor: 'rgba(40, 167, 69, 0.1)',
        fill: true,
        tension: 0.4
      }
    ]
  };

  lineChartOptions: ChartOptions<'line'> = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: 'Evolución de Avance de Proyectos (6 Meses)' }
    }
  };

  pieChartData: ChartData<'pie'> = {
    labels: ['Backend Devs', 'UX Designers', 'DevOps', 'Others'],
    datasets: [{
      data: [40, 25, 20, 15],
      backgroundColor: ['#1A2B4C', '#00A8E8', '#28A745', '#FFC107']
    }]
  };

  pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: {
      legend: { position: 'right' },
      title: { display: true, text: 'Distribución de Especialidades por Equipo' }
    }
  };
  loggedIn: any;

  constructor(
    private router: Router,
    private authService: AuthService,
    private http: HttpClient,
    private userService: UserService,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    // Si no hay token, recarga forzosa al login antes de que se vea nada
    if (isPlatformBrowser(this.platformId) && !localStorage.getItem('token')) {
       window.location.replace('/login');
       return;
    }

    console.log('📊 [DASHBOARD] Componente iniciado correctamente.');
    // ... el resto de tu código (this.nombreUsuario = ...)

    this.nombreUsuario = this.authService.obtenerNombre();
    this.rol = this.authService.obtenerCargo();
    const rolId = this.authService.obtenerRol();
    this.esAdmin = rolId === '1';
    this.esGestor = rolId === '2';
    this.esColaborador = rolId === '3';
    this.idUserActual = this.authService.obtenerIdUser();

    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => {
        this.cargarProyectos();
      }, 300);
    }
  }

  actualizarGraficoTorta(): void {
    const idsIntegrantes = this.misEquipos
      .flatMap(e => e.integrantes?.map((i: any) => i.idUser) ?? []);

    this.userService.obtenerTodos().subscribe({
      next: (usuarios) => {
        const misIntegrantes = usuarios.filter(u => idsIntegrantes.includes(u.idUser));
        const conteo: any = {};
        misIntegrantes.forEach((u: any) => {
          conteo[u.nombreCargo] = (conteo[u.nombreCargo] ?? 0) + 1;
        });

        this.pieChartData = {
          labels: Object.keys(conteo),
          datasets: [{
            data: Object.values(conteo),
            backgroundColor: ['#1A2B4C', '#00A8E8', '#28A745', '#FFC107', '#DC3545']
          }]
        };
        this.pieChartData = { ...this.pieChartData};
        this.cdr.detectChanges();
      }
    });
  }

  cargarProyectos(): void {
    this.cargando = true;
    this.http.get<any[]>(`${environment.apiUrl}/proyectos`).subscribe({
      next: (data) => {
        this.proyectos = data;
        this.kpis[0].valor = String(data.filter(p => p.idEstado === 5).length);
        this.cargando = false;
        this.cdr.detectChanges();
        setTimeout(() => this.cargarEquipos(), 500);
      },
      error: () => {
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarEquipos(): void {
    this.http.get<any[]>(`${environment.apiUrl}/equipos`).subscribe({
      next: (data) => {
        const equiposMapeados = data.map(e => ({
          ...e,
          nombreProyecto: this.proyectos.find(p => p.idProyecto === e.idProyecto)?.nombre ?? `Proyecto ${e.idProyecto}`,
          avance: 0,
          totalTareas: 0,
          tareasCompletadas: 0
        }));

        this.misEquipos = equiposMapeados.filter(e =>
          e.integrantes?.some((i: any) => i.idUser === this.idUserActual)
        );

        this.misEquipos.forEach(equipo => this.calcularAvance(equipo));
        this.actualizarGraficoTorta();
        this.pieChartData = { ...this.pieChartData};
        this.cdr.detectChanges();

      },
      error: () => {
        this.misEquipos = [];
        this.cdr.detectChanges();
      }
    });
  }

  calcularAvance(equipo: any): void {
    this.http.get<any[]>(`${environment.apiUrl}/tareas/proyecto/${equipo.idProyecto}`).subscribe({
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

  getEstadoProyecto(idEstado: number): string {
    const estados: any = { 4: 'En Planificación', 5: 'En Progreso', 6: 'Completado', 7: 'Cancelado' };
    return estados[idEstado] ?? 'Desconocido';
  }

  getBadgeEstado(idEstado: number): string {
    const badges: any = { 4: 'secondary', 5: 'warning', 6: 'success', 7: 'danger' };
    return badges[idEstado] ?? 'secondary';
  }

  cerrarSesion() {
    if (this.isBrowser()) {
      // 1. Destruir almacenamiento
      localStorage.clear();
      sessionStorage.clear();
      
      // 2. Actualizar el BehaviorSubject para que el resto de la app se entere
      // loggedIn may be undefined or not a Subject in some contexts, guard safely
      if (this.loggedIn && typeof (this.loggedIn as any).next === 'function') {
        (this.loggedIn as any).next(false);
      }
      
      // 3. Navegar usando Angular Router, reemplazando el historial
      this.router.navigate(['/login'], { replaceUrl: true });
    }
  }
  isBrowser() {
    return isPlatformBrowser(this.platformId);
  }
}