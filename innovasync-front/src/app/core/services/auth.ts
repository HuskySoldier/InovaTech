import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url = `${environment.apiUrl}/api/auth`;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // Llama al MS Auth para hacer login
  login(correo: string, clave: string) {
    return this.http.post(`${this.url}/login`, { correo, clave });
  }

  // Guarda el token JWT en localStorage
  guardarToken(token: string) {
    localStorage.setItem('token', token);
  }

  // Obtiene el token guardado
  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  // Verifica si el usuario está logueado
  estaLogueado(): boolean {
    return !!localStorage.getItem('token');
  }

  // Cierra sesión
  cerrarSesion() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}