import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth';


export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  if (!isPlatformBrowser(platformId)) return true;

  // VERIFICACIÓN DIRECTA Y AGRESIVA
  const token = localStorage.getItem('token');

  if (!token) {
    console.warn('[GUARDIA] ¡TOKEN NO ENCONTRADO! Bloqueando acceso.');
    // Limpieza total por si acaso
    localStorage.clear();
    return router.createUrlTree(['/login']);
  }

  return true;
};