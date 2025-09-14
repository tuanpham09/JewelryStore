import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface ApiResponse<T> {
  message: string;
  data: T;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<ApiResponse<{ token: string }>> {
    return this.http.post<ApiResponse<{ token: string }>>('/api/auth/login', credentials);
  }

  register(payload: { fullName?: string; email: string; password: string }): Observable<ApiResponse<unknown>> {
    return this.http.post<ApiResponse<unknown>>('/api/auth/register', payload);
  }
}
