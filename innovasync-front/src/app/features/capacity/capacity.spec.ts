import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Capacity } from './capacity';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth';
import { of } from 'rxjs';
import { vi } from 'vitest';

describe('Capacity', () => {
  let component: Capacity;
  let fixture: ComponentFixture<Capacity>;
  let mockHttpClient: any;
  let mockAuthService: any;

  beforeEach(async () => {
    // 1. Creamos mocks para los servicios que probablemente usa tu componente
    mockHttpClient = {
      get: vi.fn().mockReturnValue(of([])) // Simula una respuesta vacía por defecto
    };
    
    mockAuthService = {
      obtenerRol: vi.fn().mockReturnValue('1')
    };

    await TestBed.configureTestingModule({
      imports: [Capacity],
      providers: [
        { provide: HttpClient, useValue: mockHttpClient },
        { provide: AuthService, useValue: mockAuthService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Capacity);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('debería inicializar variables correctamente', () => {
    // Aquí puedes añadir validaciones específicas de tus variables de estado
    // Ejemplo: expect(component.cargando).toBe(true);
  });

  it('debería cargar usuarios al iniciar', () => {
    // Simula una respuesta de usuarios
    const mockUsuarios = [{ id: 1, nombre: 'Usuario Test' }];
    mockHttpClient.get.mockReturnValue(of(mockUsuarios));
  });

  it('debería cargar proyectos al iniciar', () => {
    // Simula una respuesta de proyectos
    const mockProyectos = [{ idProyecto: 1, nombre: 'Proyecto Test' }];
    mockHttpClient.get.mockReturnValue(of(mockProyectos));
  });

  it('debería cargar equipos al iniciar', () => {
    // Simula una respuesta de equipos
    const mockEquipos = [{ idEquipo: 1, nombre: 'Equipo Test' }];
    mockHttpClient.get.mockReturnValue(of(mockEquipos));
  });


});