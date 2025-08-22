import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, map, throwError } from 'rxjs';
import { Observable } from 'rxjs';

const API_URL_BASE = 'http://localhost:8080';

export interface UserData {
  name: string;
  lastName: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  private getAuthHeaders(isJson: boolean = true): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    let headers = new HttpHeaders();

    if (token && token !== 'null') {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    if (isJson) {
      headers = headers.set('Content-Type', 'application/json');
    }

    return headers;
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = `Erro HTTP ${error.status}`;
    if (error.error) {
      errorMessage =
        error.error.message || error.error.error || error.error.msg || JSON.stringify(error.error);
    }
    console.error('API error:', error);
    return throwError(() => new Error(errorMessage));
  }

  login(email: string, password: string): Observable<any> {
    return this.http
      .post(`${API_URL_BASE}/auth/login`, { email, password }, { headers: this.getAuthHeaders() })
      .pipe(
        map((data: any) => {
          if (data.token) {
            localStorage.setItem('auth_token', data.token);
            localStorage.setItem('customer_data', JSON.stringify(data.data));
            return data;
          } else {
            throw new Error('Token not received when starting session');
          }
        }),
        catchError(this.handleError)
      );
  }

  register(userData: UserData): Observable<any> {
    return this.http
      .post(`${API_URL_BASE}/auth/register`, userData, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }
}
