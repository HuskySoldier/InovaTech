import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Sidebar } from './sidebar';
import { AuthService } from '../../core/services/auth';
import { ActivatedRoute } from '@angular/router';
import { vi } from 'vitest';

describe('Sidebar Component', () => {
  let component: Sidebar;
  let fixture: ComponentFixture<Sidebar>;
  let mockAuthService: any;

  beforeEach(async () => {
    // 1. Creamos mocks usando Vitest (vi.fn())
    mockAuthService = {
      obtenerNombre: vi.fn().mockReturnValue('Iván Hernández'),
      obtenerCargo: vi.fn().mockReturnValue('Ingeniero de Software'),
      cerrarSesion: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [Sidebar],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: ActivatedRoute, useValue: {} } 
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Sidebar);
    component = fixture.componentInstance;
    fixture.detectChanges(); 
  });

  it('debería crear el componente correctamente', () => {
    expect(component).toBeTruthy();
  });

  it('debería inicializar los datos del usuario en ngOnInit y calcular iniciales', () => {
    expect(component.nombreCompleto).toBe('Iván Hernández');
    expect(component.cargo).toBe('Ingeniero de Software');
    expect(component.iniciales).toBe('IH'); 
  });

  it('debería llamar a authService.cerrarSesion() al ejecutar el método cerrarSesion()', () => {
    component.cerrarSesion();
    expect(mockAuthService.cerrarSesion).toHaveBeenCalledTimes(1);
  });
});