import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './projects.html',
  styleUrl: './projects.css'
})
export class Projects {

  columnas = [
    {
      titulo: 'Por Hacer',
      color: '#6c757d',
      tareas: [
        { titulo: 'Diseñar base de datos', fecha: '30/04/2024', responsable: 'A.M', prioridad: 'Alta', colorPrioridad: '#DC3545' },
        { titulo: 'Configurar API Gateway', fecha: '02/05/2024', responsable: 'I.H', prioridad: 'Media', colorPrioridad: '#FFC107' }
      ]
    },
    {
      titulo: 'En Progreso',
      color: '#FFC107',
      tareas: [
        { titulo: 'Desarrollar MS Auth', fecha: '28/04/2024', responsable: 'B.G', prioridad: 'Alta', colorPrioridad: '#DC3545' },
        { titulo: 'Crear componentes Angular', fecha: '29/04/2024', responsable: 'J.N', prioridad: 'Alta', colorPrioridad: '#DC3545' }
      ]
    },
    {
      titulo: 'En Revisión',
      color: '#00A8E8',
      tareas: [
        { titulo: 'Modelo entidad relación', fecha: '25/04/2024', responsable: 'A.M', prioridad: 'Media', colorPrioridad: '#FFC107' }
      ]
    },
    {
      titulo: 'Terminado',
      color: '#28A745',
      tareas: [
        { titulo: 'Definir arquitectura', fecha: '20/04/2024', responsable: 'B.G', prioridad: 'Baja', colorPrioridad: '#28A745' },
        { titulo: 'Documento de requerimientos', fecha: '18/04/2024', responsable: 'J.N', prioridad: 'Baja', colorPrioridad: '#28A745' }
      ]
    }
  ];
}