import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  if (!isPlatformBrowser(platformId)) {
    return true; 
  }

  const logueado = authService.estaLogueado();
  console.log('🛡️ [GUARDIA] Intentando entrar a la ruta:', state.url);
  console.log('🛡️ [GUARDIA] ¿Tiene Token guardado en localStorage?', logueado);

  if (logueado) {
    console.log('🛡️ [GUARDIA] ¡Acceso Permitido!');
    return true;
  } else {
    console.warn('🛡️ [GUARDIA] Acceso Denegado. Expulsando al Login...');
    router.navigate(['/login'], { replaceUrl: true });
    return false;
  }
};