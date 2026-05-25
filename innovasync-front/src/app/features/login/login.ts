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

    this.cargando = true;
    this.errorMsg = '';

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        this.cargando = false;
        this.cdr.detectChanges(); // Opcional, pero buena práctica
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.log('El componente Login capturó el error:', err);
        
        // Verificamos si el error viene de Java (400) o si es otro error (como el 404 o 504)
        if (err.status === 400 || err.status === 401) {
          this.errorMsg = 'Correo o contraseña incorrectos';
        } else {
          this.errorMsg = 'No hay conexión con el servidor. Inténtalo más tarde.';
        }
        
        this.cargando = false;
        this.cdr.detectChanges(); // <-- ¡ESTO fuerza al HTML a borrar el spinner!
      }
    });
  }
}