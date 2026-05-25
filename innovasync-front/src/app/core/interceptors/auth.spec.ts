import { TestBed } from '@angular/core/testing';
import { HttpRequest, HttpHandlerFn, HttpResponse } from '@angular/common/http';
import { PLATFORM_ID } from '@angular/core';
import { authInterceptor } from './auth.interceptor'; // Ajusta la ruta a tu archivo
import { of } from 'rxjs';
import { vi } from 'vitest';

describe('authInterceptor', () => {
  // Mock del manejador 'next' que requiere el interceptor
  const next: HttpHandlerFn = vi.fn().mockReturnValue(of(new HttpResponse()));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: PLATFORM_ID, useValue: 'browser' } // Simulamos que estamos en el navegador
      ]
    });
    localStorage.clear();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('debería añadir el token al header Authorization si existe en localStorage', () => {
    const token = 'fake-jwt-token';
    localStorage.setItem('token', token);
    
    const req = new HttpRequest('GET', '/api/test');
    
    // Ejecutamos el interceptor en el contexto de inyección de Angular
    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next);
    });

    // Verificamos que el interceptor llamó al siguiente handler (next)
    // y que la petición clonada tiene el header correcto
    expect(next).toHaveBeenCalled();
    const interceptedReq = (next as any).mock.calls[0][0];
    expect(interceptedReq.headers.get('Authorization')).toBe(`Bearer ${token}`);
  });

  it('no debería añadir el header Authorization si no hay token', () => {
    localStorage.removeItem('token');
    
    const req = new HttpRequest('GET', '/api/test');
    
    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next);
    });

    expect(next).toHaveBeenCalled();
    const interceptedReq = (next as any).mock.calls[0][0];
    expect(interceptedReq.headers.has('Authorization')).toBe(false);
  });
});