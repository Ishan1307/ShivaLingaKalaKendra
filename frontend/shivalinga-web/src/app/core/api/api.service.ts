import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = inject(API_BASE_URL);

  post<TRequest, TResponse>(endpoint: string, body: TRequest): Observable<TResponse> {
    const baseUrl = this.baseUrl.replace(/\/+$/, '');
    const path = endpoint.replace(/^\/+/, '');
    return this.http.post<TResponse>(`${baseUrl}/${path}`, body);
  }
}
