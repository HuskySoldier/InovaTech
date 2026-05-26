import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Projects } from './projects';
import { ProyectosService } from '../../core/services/projects';
import { AuthService } from '../../core/services/auth';
import { HttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { of } from 'rxjs';
import { vi } from 'vitest';

describe('Projects Component', () => {
  let component: Projects;
  let fixture: ComponentFixture<Projects>;

  let mockProyectosService: any;
  let mockAuthService: any;
  let mockHttpClient: any;

  beforeEach(async () => {
    mockProyectosService = {
      obtenerTodos: vi.fn().mockReturnValue(of([
        { idProyecto: 1, nombre: 'Proyecto Test', idEstado: 4, fechaInicio: '2026-01-01' }
      ])),
      crear: vi.fn().mockReturnValue(of({ mensaje: 'Creado con éxito' }))
    };

    mockAuthService = {
      obtenerRol: vi.fn().mockReturnValue('1'),
      obtenerIdUser: vi.fn().mockReturnValue(100)
    };

    mockHttpClient = {
      get: vi.fn().mockImplementation((url: string) => {
        if (url.includes('/prioridades')) {
          return of([{ id: 1, nombre: 'Alta' }, { id: 2, nombre: 'Media' }]);
        }
        if (url.includes('/tareas/proyecto/')) {
          return of([{ id: 1, nombre: 'Tarea Test', idEstado: 8, idPrioridad: 1 }]);
        }
        if (url.includes('/equipos')) {
          return of([]); 
        }
        return of([]);
      }),
      post: vi.fn().mockReturnValue(of({})),
      put: vi.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [Projects],
      providers: [
        provideRouter([]), 
        { provide: ProyectosService, useValue: mockProyectosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: HttpClient, useValue: mockHttpClient },
        { provide: PLATFORM_ID, useValue: 'browser' } 
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Projects);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    // Restaurar los timers reales después de cada prueba por precaución
    vi.useRealTimers();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  // 👇 AQUÍ ESTÁ LA MAGIA DE VITEST 👇
  it('debería inicializar roles y cargar datos después de los setTimeout', () => {
    // 1. Tomamos el control del tiempo en JavaScript
    vi.useFakeTimers(); 
    
    // 2. Ejecutamos el componente
    component.ngOnInit();
    
    // 3. Adelantamos el tiempo 400ms instantáneamente (100ms + 300ms de tus setTimeout)
    vi.advanceTimersByTime(400);

    // 4. Hacemos las aserciones
    expect(component.esAdmin).toBe(true);
    expect(component.puedeCrear).toBe(true);
    expect(mockProyectosService.obtenerTodos).toHaveBeenCalledTimes(1);
    expect(component.proyectos.length).toBe(1);
    expect(component.cargando).toBe(false);
  });

  it('debería seleccionar un proyecto y cargar sus tareas en las columnas correspondientes', () => {
    const proyectoMock = { idProyecto: 1, nombre: 'Proyecto Test' };
    
    component.seleccionarProyecto(proyectoMock);

    expect(component.proyectoSeleccionado).toEqual(proyectoMock);
    expect(mockHttpClient.get).toHaveBeenCalledWith(expect.stringContaining('/tareas/proyecto/1'));
    
    const colPendiente = component.columnas.find(c => c.idEstado === 8);
    expect(colPendiente?.tareas.length).toBe(1);
    expect(colPendiente?.tareas[0].titulo).toBe('Tarea Test');
  });

  it('debería bloquear la creación de un proyecto si faltan campos obligatorios', () => {
    component.abrirModalProyecto();
    
    component.nuevoProyecto = { 
      nombre: '', descripcion: '', fechaInicio: '', 
      fechaTerminoEsti: '', presuEstimado: 100, idEstado: 4 
    };
    
    component.crearProyecto();
    
    expect(component.error).toBe('Por favor completa los campos obligatorios.');
    expect(mockProyectosService.crear).not.toHaveBeenCalled(); 
  });

  it('debería crear un proyecto exitosamente si los datos son correctos', () => {
    component.abrirModalProyecto();
    
    component.nuevoProyecto = { 
      nombre: 'Nuevo Sistema', 
      descripcion: 'Prueba', 
      fechaInicio: '2026-05-25', 
      fechaTerminoEsti: '2026-12-31', 
      presuEstimado: 500000, 
      idEstado: 4 
    };
    
    component.crearProyecto();
    
    expect(mockProyectosService.crear).toHaveBeenCalled();
    expect(component.exito).toBe('¡Proyecto creado correctamente!');
    expect(component.mostrarModalProyecto).toBe(false);
    expect(component.guardando).toBe(false);
  });

  it('debería actualizar el proyecto seleccionado después de editarlo', () => {
    const proyectoMock = { idProyecto: 1, nombre: 'Proyecto Test', descripcion: 'Original', fechaInicio: '2026-01-01', fechaTerminoEsti: '2026-12-31', presuEstimado: 100000, idEstado: 4 };
    component.proyectoSeleccionado = proyectoMock;
    
    const proyectoEditado = { ...proyectoMock, nombre: 'Proyecto Editado', descripcion: 'Actualizado' };
    component.proyectoSeleccionado = proyectoEditado;
  });
});