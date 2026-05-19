import { Component, Inject, PLATFORM_ID } from '@angular/core';
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
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  login() {
    // Validar que no estén vacíos
    if (!this.email || !this.password) {
      this.errorMsg = 'Por favor ingresa tu correo y contraseña';
      return;
    }

    this.cargando = true;
    this.errorMsg = '';

    // Llama al MS Auth via API Gateway
    this.authService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        // Guarda el token JWT
        if (isPlatformBrowser(this.platformId)) {
          this.authService.guardarToken(response.token);
        }
        this.cargando = false;
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMsg = 'Correo o contraseña incorrectos';
        this.cargando = false;
      }
    });
  }
}