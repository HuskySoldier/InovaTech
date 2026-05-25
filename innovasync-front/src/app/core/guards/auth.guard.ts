import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // ¡CLAVE! Si se ejecuta en el servidor (SSR), dejamos pasar temporalmente
  // porque el servidor no puede leer el localStorage.
  if (!isPlatformBrowser(platformId)) {
    return true; 
  }

  // Validación real en el navegador
  if (authService.estaLogueado()) {
    return true;
  } else {
    router.navigate(['/login'], { replaceUrl: true });
    return false;
  }
};