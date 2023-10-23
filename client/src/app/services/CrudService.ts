import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/User";

@Injectable({
  providedIn: 'root',
})
export class CrudService<T> {
  constructor(private http: HttpClient) {}

  listAll(endpoint: string, page: number, size: number, order: string, direction: string): Observable<T[]> {
    endpoint = `${endpoint}?page=${page}&size=${size}&sort=${order}&direction=${direction}`;
    return this.http.get<T[]>(endpoint);
  }

  listById(endpoint: string, id: number): Observable<T> {
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

  extend(endpoint: string, id: number): Observable<T> {
    return this.http.post<T>(`${endpoint}/${id}`, null);
  }

  return(endpoint: string, id: number): Observable<T> {
    return this.http.post<T>(`${endpoint}/${id}`, null);
  }
  login(endpoint: string, entity: T): Observable<T> {
    return this.http.post<T>(endpoint, entity);
  }

  sugestion(endpoint: string, id: number): Observable<T>{
    return this.http.get<T>(`${endpoint}/${id}`);
  }
}
