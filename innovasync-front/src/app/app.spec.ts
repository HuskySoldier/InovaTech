import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app';
import { provideRouter, Router } from '@angular/router';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideRouter([])
      ]
    }).compileComponents();
  });

  // Esta es la única prueba que necesitas aquí por ahora: 
  // Asegura que el componente principal levante sin errores.
  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('debería detectar cambios de visibilidad y estado de navegación', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    // Simulamos el cambio de visibilidad
    document.dispatchEvent(new Event('visibilitychange'));
    // Simulamos el evento popstate (navegación del historial)
    window.dispatchEvent(new PopStateEvent('popstate'));

    // Aquí podrías agregar expectativas específicas si tu componente hace algo concreto en esos eventos
    // Por ejemplo, si tienes un método que se llama en onVisibilityChange, podrías espiar ese método y verificar que se llamó.
  });

  
});

