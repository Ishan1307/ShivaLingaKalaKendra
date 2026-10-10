import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../api/api.service';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  message: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = inject(ApiService);

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.api.post<LoginRequest, LoginResponse>('auth/login', request);
  }
}
