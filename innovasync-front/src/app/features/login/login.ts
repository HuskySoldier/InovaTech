import { Component, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core'; // <-- Importa ChangeDetectorRef
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  email = '';
  password = '';
  errorMsg = '';
  cargando = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private cdr: ChangeDetectorRef, // <-- Inyéctalo en el constructor
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

login() {
    if (!this.email || !this.password) {
      this.errorMsg = 'Por favor ingresa tu correo y contraseña';
      return;
    }

    console.log('🚀 [LOGIN] Botón presionado. Validando en el backend...');
    this.cargando = true;
    this.errorMsg = '';

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        console.log('🚀 [LOGIN] ¡Backend dio el OK! Viajando al Dashboard...');
        this.cargando = false;
        this.cdr.detectChanges(); 
        // Reemplaza el router.navigate por esto:
        this.router.navigate(['/dashboard']).then(success => {
          if (success) {
            console.log('✅ [ROUTER] Navegación completada al Dashboard.');
          } else {
            console.warn('❌ [ROUTER] Angular CANCELÓ la navegación silenciosamente.');
          }
        }).catch(err => {
          console.error('🔥 [ROUTER] Error crítico al navegar:', err);
        });
      },
      error: (err) => {
        console.error('🚀 [LOGIN] El Backend rechazó las credenciales:', err);
        if (err.status === 400 || err.status === 401) {
          this.errorMsg = 'Correo o contraseña incorrectos';
        } else {
          this.errorMsg = 'No hay conexión con el servidor. Inténtalo más tarde.';
        }
        this.cargando = false;
        this.cdr.detectChanges(); 
      }
    });
  }
}