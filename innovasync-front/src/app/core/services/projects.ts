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
    return this.http.get<any[]>(`${this.url}`);
  }

  obtenerPorId(id: number) {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  crear(proyecto: any) {
    return this.http.post<any>(`${this.url}`, proyecto);
  }

  actualizar(id: number, proyecto: any) {
    return this.http.put<any>(`${this.url}/${id}`, proyecto);
  }

  eliminar(id: number) {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}