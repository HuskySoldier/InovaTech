import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProyectosService {

  private url = `${environment.apiUrl}/proyectos`;

  constructor(private http: HttpClient) {}

  obtenerTodos() {
    return this.http.get(`${this.url}`);
  }

  obtenerPorId(id: number) {
    return this.http.get(`${this.url}/${id}`);
  }

  crear(proyecto: any) {
    return this.http.post(`${this.url}`, proyecto);
  }

  actualizar(id: number, proyecto: any) {
    return this.http.put(`${this.url}/${id}`, proyecto);
  }

  eliminar(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }
}