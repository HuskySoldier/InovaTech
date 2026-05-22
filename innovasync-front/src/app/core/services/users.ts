import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserService {

  private url = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  obtenerTodos() {
    return this.http.get<any[]>(`${this.url}`);
  }

  obtenerPorId(id: number) {
    return this.http.get<any>(`${this.url}/${id}`);
    }
}