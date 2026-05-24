import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap, switchMap } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private url = `${environment.apiUrl}/auth`;
  private usuariosUrl = `${environment.apiUrl}/usuarios`;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  login(correo: string, clave: string) {
    return this.http.post<any>(`${this.url}/login`, { correo, clave }).pipe(
      tap(res => {
        if (this.isBrowser()) {
          localStorage.setItem('token', res.token);
          localStorage.setItem('correo', res.correo);
          localStorage.setItem('rol', res.rol);
        }
      }),
      switchMap(res => {
        return this.http.get<any[]>(this.usuariosUrl).pipe(
          tap(usuarios => {
            const usuario = usuarios.find(u => u.email === res.correo);
            if (usuario && this.isBrowser()) {
              localStorage.setItem('nombreCompleto', usuario.nombreCompleto);
              localStorage.setItem('cargo', usuario.nombreCargo);
              localStorage.setItem('idUser', String(usuario.idUser));
              localStorage.setItem('run', usuario.run ?? '');
            }
          })
        );
      })
    );
  }

  obtenerToken(): string | null {
    return this.isBrowser() ? localStorage.getItem('token') : null;
  }

  obtenerIdUser(): number {
  return this.isBrowser() ? Number(localStorage.getItem('idUser') ?? 0) : 0;
  }

  obtenerNombre(): string {
    return this.isBrowser() ? (localStorage.getItem('nombreCompleto') ?? 'Usuario') : 'Usuario';
  }

  obtenerCargo(): string {
    return this.isBrowser() ? (localStorage.getItem('cargo') ?? '') : '';
  }

  obtenerRol(): string {
    return this.isBrowser() ? (localStorage.getItem('rol') ?? '') : '';
  }

  

  estaLogueado(): boolean {
    return this.isBrowser() ? !!localStorage.getItem('token') : false;
  }

  cerrarSesion() {
    if (this.isBrowser()) localStorage.clear();
    this.router.navigate(['/login']);
  }
}