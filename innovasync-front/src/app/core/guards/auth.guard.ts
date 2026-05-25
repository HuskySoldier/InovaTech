import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificamos si está logueado usando tu método existente
  if (authService.estaLogueado()) {
    return true; // Permite el acceso
  } else {
    // Si no, lo mandamos al login
    router.navigate(['/login']);
    return false; // Bloquea el acceso
  }
};