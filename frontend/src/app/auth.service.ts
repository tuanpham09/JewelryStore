import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    
    // Check token on init only in browser environment
    if (this.isBrowser) {
      const token = localStorage.getItem('token');
      if (token) {
        this.loadCurrentUser();
      }
    }
  }

  login(credentials: {email: string, password: string}): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap({
        next: (response: any) => {
          if (response.data?.token && this.isBrowser) {
            localStorage.setItem('token', response.data.token);
            this.loadCurrentUser();
          }
        },
        error: (error) => {
          console.error('Login error:', error);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData).pipe(
      tap({
        next: (response: any) => {
          if (response.data?.token && this.isBrowser) {
            localStorage.setItem('token', response.data.token);
            this.loadCurrentUser();
          }
        },
        error: (error) => {
          console.error('Register error:', error);
        }
      })
    );
  }

  private loadCurrentUser() {
    if (!this.isBrowser) return;
    
    const token = localStorage.getItem('token');
    if (token) {
      this.http.get(`${this.apiUrl}/me`).subscribe({
        next: (response: any) => {
          this.currentUserSubject.next(response.data);
        },
        error: (error) => {
          console.error('getCurrentUser error:', error);
          this.logout();
        }
      });
    }
  }

  // Public method để load user từ bên ngoài
  loadUserFromToken(): void {
    this.loadCurrentUser();
  }

  logout() {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      localStorage.removeItem('pending_cart_item');
      localStorage.removeItem('callback_url');
    }
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return this.isBrowser ? !!localStorage.getItem('token') : false;
  }

  getCurrentUser(): any {
    const user = this.currentUserSubject.value;
    console.log('AuthService.getCurrentUser() called, returning:', user);
    return user;
  }
}
