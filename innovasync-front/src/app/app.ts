import { Component, OnInit, PLATFORM_ID, Inject, HostListener } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { Sidebar } from './shared/sidebar/sidebar';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from './core/services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Sidebar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object, 
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      // 1. Lógica del tema visual
      const tema = localStorage.getItem('tema');
      if (tema === 'oscuro') {
        document.body.classList.add('tema-oscuro');
      }

      // 2. Interceptor de bfcache (Back/Forward Cache) del navegador
      // Detecta si la página se recupera de la memoria al presionar "Atrás"
      window.addEventListener('pageshow', (event) => {
        if (event.persisted) {
          this.checkSession();
        }
      });
    }
  }

  // Escudo adicional: Verifica sesión cuando la pestaña se vuelve a enfocar
  @HostListener('window:visibilitychange')
  onVisibilityChange() {
    this.checkSession();
  }

  // Escudo adicional: Verifica sesión en eventos ordinarios de navegación en el historial
  @HostListener('window:popstate')
  onPopState() {
    this.checkSession();
  }

  private checkSession() {
    if (isPlatformBrowser(this.platformId) && 
        !this.authService.estaLogueado() && 
        this.router.url !== '/login') {
      // Reemplaza la URL actual por /login sin generar una entrada extra inválida en el historial
      window.location.replace('/login');
    }
  }

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}