import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-capacity',
  standalone: true,
  imports: [CommonModule, RouterModule ],
  templateUrl: './capacity.html',
  styleUrl: './capacity.css'
})
export class Capacity {

  // Semanas del período
  semanas = ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4', 'Semana 5'];

  // Profesionales con su capacidad
  profesionales = [
    {
      nombre: 'Alejandro M.',
      cargo: 'Directivo',
      avatar: 'AM',
      asignaciones: [
        { proyecto: '', horas: 0, color: '' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' }
      ]
    },
    {
      nombre: 'Alejandro Martin',
      cargo: 'Directivo',
      avatar: 'AM',
      asignaciones: [
        { proyecto: '', horas: 0, color: '' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: '', horas: 0, color: '' }
      ]
    },
    {
      nombre: 'Sofía Gómez',
      cargo: 'UX',
      avatar: 'SG',
      asignaciones: [
        { proyecto: '', horas: 0, color: '' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: 'Proyecto 1', horas: 40, color: '#28A745' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' }
      ]
    },
    {
      nombre: 'Diego Ruiz',
      cargo: 'DevOps',
      avatar: 'DR',
      asignaciones: [
        { proyecto: '', horas: 0, color: '' },
        { proyecto: 'Proyecto 1', horas: 50, color: '#FFC107' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' }
      ]
    },
    {
      nombre: 'Alejandro Mara',
      cargo: 'DevOps',
      avatar: 'AM',
      asignaciones: [
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' },
        { proyecto: '', horas: 0, color: '' }
      ]
    }
  ];

  // Calcula el porcentaje de utilización global
  utilizacionGlobal = 78;
}