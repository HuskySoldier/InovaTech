import { TestBed } from '@angular/core/testing';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { authGuard } from './auth.guard';
import { vi } from 'vitest';

describe('AuthGuard', () => {
  let routerSpy: any;

  const dummyRoute = {} as ActivatedRouteSnapshot;
  const dummyState = {} as RouterStateSnapshot;

  beforeEach(() => {
    // Espía de Vitest para el Router
    routerSpy = {
      createUrlTree: vi.fn().mockReturnValue('dummyUrlTree')
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: PLATFORM_ID, useValue: 'browser' } 
      ]
    });
    
    localStorage.clear();
  });

  it('debería permitir el acceso (retornar true) si existe un token en localStorage', () => {
    localStorage.setItem('token', 'token-super-seguro-123');
    const resultado = TestBed.runInInjectionContext(() => authGuard(dummyRoute, dummyState));
    expect(resultado).toBe(true);
  });

  it('debería bloquear el acceso, limpiar localStorage y redirigir a /login si NO hay token', () => {
    localStorage.removeItem('token');
    localStorage.setItem('basura', 'datos-viejos');
    
    TestBed.runInInjectionContext(() => authGuard(dummyRoute, dummyState));
    
    expect(routerSpy.createUrlTree).toHaveBeenCalledWith(['/login']);
    expect(localStorage.getItem('basura')).toBeNull();
  });
});