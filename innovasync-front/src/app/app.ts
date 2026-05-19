import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { Sidebar } from './shared/sidebar/sidebar';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Sidebar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {

  constructor(@Inject(PLATFORM_ID) private platformId: Object, private router: Router) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const tema = localStorage.getItem('tema');
      if (tema === 'oscuro') {
        document.body.classList.add('tema-oscuro');
      }
    }
  }

  // Oculta el sidebar en la página de login
  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}