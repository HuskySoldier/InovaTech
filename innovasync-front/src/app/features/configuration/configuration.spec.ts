import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Configuration } from './configuration';
import { AuthService } from '../../core/services/auth';
import { HttpClient } from '@angular/common/http';
import { PLATFORM_ID } from '@angular/core';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { FormsModule } from '@angular/forms';

describe('Configuracion', () => {
  let component: Configuration;
  let fixture: ComponentFixture<Configuration>;
  let mockAuthService: any;
  let mockHttpClient: any;

  beforeEach(async () => {
    // Mocks
    mockAuthService = {
      obtenerNombre: vi.fn().mockReturnValue('Iván Hernández'),
      obtenerCargo: vi.fn().mockReturnValue('Ingeniero'),
      obtenerRol: vi.fn().mockReturnValue('1'),
      obtenerIdUser: vi.fn().mockReturnValue(100)
    };

    mockHttpClient = {
      get: vi.fn().mockImplementation((url: string) => {
        if (url.includes('/cargos')) return of([{ idCargo: 1, nombreCargo: 'Desarrollador' }]);
        if (url.includes('/roles')) return of([{ idRol: 1, nombre: 'Admin' }]);
        if (url.includes('/usuarios')) return of([{ run: '123', nombre: 'Juan' }]);
        return of([]);
      }),
      post: vi.fn().mockReturnValue(of({})),
      put: vi.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [Configuration, FormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: HttpClient, useValue: mockHttpClient },
        { provide: PLATFORM_ID, useValue: 'browser' }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Configuration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('debería inicializar los datos desde el servicio al iniciar', () => {
    expect(component.nombre).toBe('Iván Hernández');
    expect(component.rolUsuario).toBe('1');
    expect(mockHttpClient.get).toHaveBeenCalledWith(expect.stringContaining('/cargos'));
  });

  it('debería validar que los campos obligatorios estén presentes al crear usuario', () => {
    component.nuevoUsuario = { run: '', nombre: '', apellido: '', clave: '', idCargo: null, idRol: null, idEstado: 1 };
    component.crearUsuario();
    expect(component.errorUsuario).toBe('Por favor completa todos los campos obligatorios.');
  });

  it('debería crear un usuario exitosamente', () => {
    component.nuevoUsuario = { 
      run: '12345678-9', nombre: 'Juan', apellido: 'Perez', 
      clave: '123456', idCargo: 1, idRol: 1, idEstado: 1 
    };

    component.crearUsuario();

    expect(mockHttpClient.post).toHaveBeenCalled();
    expect(component.guardandoUsuario).toBe(false);
  });

  it('debería guardar cambios en localStorage al presionar guardar', () => {
    component.notifEmail = false;
    component.guardarCambios();
    
    expect(localStorage.getItem('notifEmail')).toBe('false');
    expect(mockHttpClient.put).toHaveBeenCalled();
  });

  
});