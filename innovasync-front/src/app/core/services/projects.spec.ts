import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ProyectosService } from './projects';
import { environment } from '../../../environments/environment';

describe('ProyectosService', () => {
  let service: ProyectosService;
  let httpTesting: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/proyectos`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProyectosService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ProyectosService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verifica que no haya peticiones pendientes
    httpTesting.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('obtenerTodos debería realizar una petición GET', () => {
    service.obtenerTodos().subscribe(res => {
      expect(res).toEqual([]);
    });

    const req = httpTesting.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('obtenerPorId debería realizar una petición GET con el ID', () => {
    const id = 1;
    service.obtenerPorId(id).subscribe(res => {
      expect(res.id).toBe(id);
    });

    const req = httpTesting.expectOne(`${baseUrl}/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush({ id: 1 });
  });

  it('crear debería realizar una petición POST', () => {
    const nuevoProyecto = { nombre: 'Nuevo Proyecto' };
    
    service.crear(nuevoProyecto).subscribe();

    const req = httpTesting.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nuevoProyecto);
    req.flush({});
  });

  it('actualizar debería realizar una petición PUT', () => {
    const id = 1;
    const datos = { nombre: 'Proyecto Actualizado' };

    service.actualizar(id, datos).subscribe();

    const req = httpTesting.expectOne(`${baseUrl}/${id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(datos);
    req.flush({});
  });

  
});