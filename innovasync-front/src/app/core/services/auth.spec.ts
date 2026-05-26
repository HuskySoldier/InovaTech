import { TestBed } from '@angular/core/testing';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth';
import { environment } from '../../../environments/environment';
import { PLATFORM_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';

describe('AuthService', () => {
  let service: AuthService;
  let httpTesting: HttpTestingController;

  // Mock para location
  const locationMock = { href: '' };

  beforeEach(() => {
    // 1. Resetear el mock de location
    locationMock.href = '';
    vi.stubGlobal('location', locationMock);

    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(withInterceptors([])),
        provideHttpClientTesting(),
        // Agregamos una ruta dummy para que el router no falle
        provideRouter([{ path: 'login', component: class {} }]),
        { provide: PLATFORM_ID, useValue: 'browser' }
      ]
    });
    
    service = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpTesting.verify();
    vi.clearAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('login debería realizar la cadena de peticiones POST y luego GET', () => {
    const mockLoginRes = { token: 'fake-token', correo: 'test@innovatech.cl' };
    const mockUsersRes = [{ email: 'test@innovatech.cl', nombreCompleto: 'Iván', nombreCargo: 'Dev', idUser: 1, run: '123' }];
    
    service.login('test@innovatech.cl', 'password123').subscribe();

    const loginReq = httpTesting.expectOne(`${environment.apiUrl}/auth/login`);
    expect(loginReq.request.method).toBe('POST');
    expect(loginReq.request.body).toEqual({ correo: 'test@innovatech.cl', clave: 'password123' });
    loginReq.flush(mockLoginRes);

    const usersReq = httpTesting.expectOne(`${environment.apiUrl}/usuarios`);
    expect(usersReq.request.method).toBe('GET');
    usersReq.flush(mockUsersRes);
  });


  it('obtenerNombre debería retornar el valor desde localStorage', () => {
    localStorage.setItem('nombreCompleto', 'Iván Hernández');
    expect(service.obtenerNombre()).toBe('Iván Hernández');
  });

  it('obtenerCargo debería retornar el valor desde localStorage', () => {
    localStorage.setItem('cargo', 'Ingeniero');
    expect(service.obtenerCargo()).toBe('Ingeniero');
  });

  it('obtenerRol debería retornar el valor desde localStorage', () => {
    localStorage.setItem('rol', '1');
    expect(service.obtenerRol()).toBe('1');
  });
});