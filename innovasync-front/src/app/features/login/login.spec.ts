import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Login } from './login';
import { AuthService } from '../../core/services/auth';
import { Router } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { FormsModule } from '@angular/forms';

describe('Login Component', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  
  // Mocks de los servicios inyectados
  let mockAuthService: any;
  let mockRouter: any;

  beforeEach(async () => {
    // 1. Mock de AuthService
    mockAuthService = {
      login: vi.fn()
    };

    // 2. Mock de Router simulando que la navegación devuelve una Promesa exitosa
    mockRouter = {
      navigate: vi.fn().mockResolvedValue(true)
    };

    await TestBed.configureTestingModule({
      imports: [Login, FormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: PLATFORM_ID, useValue: 'browser' }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería mostrar error si los campos están vacíos al intentar iniciar sesión', () => {
    component.email = '';
    component.password = '';
    
    component.login();
    
    expect(component.errorMsg).toBe('Por favor ingresa tu correo y contraseña');
    expect(mockAuthService.login).not.toHaveBeenCalled(); 
  });

  // 👇 Hacemos el test async en lugar de usar fakeAsync
  it('debería iniciar sesión correctamente y redirigir al dashboard', async () => {
    mockAuthService.login.mockReturnValue(of({ token: 'fake-token' }));
    
    component.email = 'test@innovatech.com';
    component.password = 'secreta123';
    
    component.login();
    
    // Dejamos que el Event Loop de JavaScript resuelva las promesas pendientes (el .then del router)
    await Promise.resolve();

    expect(mockAuthService.login).toHaveBeenCalledWith('test@innovatech.com', 'secreta123');
    expect(component.cargando).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('debería mostrar error de credenciales incorrectas si el backend devuelve un 401', () => {
    mockAuthService.login.mockReturnValue(throwError(() => ({ status: 401 })));
    
    component.email = 'test@innovatech.com';
    component.password = 'mal_password';
    
    component.login();
    
    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.cargando).toBe(false);
    expect(component.errorMsg).toBe('Correo o contraseña incorrectos');
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('debería mostrar error de conexión si el backend devuelve un error distinto (ej: 500)', () => {
    mockAuthService.login.mockReturnValue(throwError(() => ({ status: 500 })));
    
    component.email = 'test@innovatech.com';
    component.password = 'secreta123';
    
    component.login();
    
    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.cargando).toBe(false);
    expect(component.errorMsg).toBe('No hay conexión con el servidor. Inténtalo más tarde.');
  });
});