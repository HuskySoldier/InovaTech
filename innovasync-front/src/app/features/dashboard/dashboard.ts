import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';  
import { Chart,ChartData, ChartOptions, registerables } from 'chart.js';
import { AuthService } from '../../core/services/auth';

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

  kpis = [
    { titulo: 'Proyectos Activos', valor: '12', color: '#1A2B4C' },
    { titulo: 'Utilización de Recursos', valor: '85%', color: '#00A8E8' },
    { titulo: 'Tareas Críticas', valor: '3', color: '#DC3545' },
    { titulo: 'Presupuesto Restante', valor: '40%', color: '#28A745' }
  ];

  proyectos = [
    { nombre: 'Proyecto 1', gestor: 'Alejandro M.', fecha: '09/07/2023', estado: 'En Progreso', badge: 'amarillo' },
    { nombre: 'Proyecto 2', gestor: 'Alejandro Martin', fecha: '29/03/2023', estado: 'Finalizado', badge: 'verde' },
    { nombre: 'Proyecto 3', gestor: 'Alejandro Mara', fecha: '30/01/2023', estado: 'En Riesgo', badge: 'rojo' }
  ];

  // ===== GRÁFICO DE LÍNEAS =====
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

  // ===== GRÁFICO DE TORTA =====
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

  constructor(private router: Router,private authService: AuthService) {}
  ngOnInit(): void {
    this.nombreUsuario = this.authService.obtenerNombre();
    this.rol = this.authService.obtenerCargo();
  }

  cerrarSesion() {
    this.router.navigate(['/login']);
  }
}

