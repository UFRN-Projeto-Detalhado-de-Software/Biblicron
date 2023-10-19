import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CrudService<T> {
  constructor(private http: HttpClient) {}

  getAll(endpoint: string): Observable<T[]> {
    return this.http.get<T[]>(endpoint);
  }

  getOne(endpoint: string, id: number): Observable<T> {
    return this.http.get<T>(`${endpoint}/${id}`);
  }

  create(endpoint: string, entity: T): Observable<T> {
    return this.http.post<T>(endpoint, entity);
  }

  update(endpoint: string, id: number, entity: T): Observable<T> {
    return this.http.put<T>(`${endpoint}/${id}`, entity);
  }

  delete(endpoint: string, id: number): Observable<void> {
    return this.http.delete<void>(`${endpoint}/${id}`);
  }
}
