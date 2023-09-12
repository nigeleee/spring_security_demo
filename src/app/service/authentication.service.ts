import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Route, Router } from '@angular/router';
import { Observable, of, tap } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  // private apiUrl: string = 'https://miniprojectdemo-production.up.railway.app/api/';
  // private oauthUrl: string = 'https://miniprojectdemo-production.up.railway.app/oauth2/authorization/google';

  private apiUrl = 'http://localhost:8080/api/';
  private oauthUrl = 'http://localhost:8080/oauth2/authorization/google'
  // private apiUrl = environment.apiUrl;
  // private oauthUrl = environment.oauthUrl;

  constructor(private http: HttpClient, private router: Router) { }

  // login(email: string, password: string): Observable<any> {
  //   const payload = { email, password };
  //   return this.http.post(`${this.apiUrl}login`, payload);
  // }
  login(email: string, password: string): Observable<any> {
    const payload = { email, password };
    return this.http.post(`${this.apiUrl}login`, payload).pipe(
      tap((response: any) => {
        console.log('Response from backend:', response);  // Log the response to inspect it
        const token = response.jwt;
        if (token) {
          localStorage.setItem('jwtToken', token);
        } else {
          console.error('Token is undefined or not found in the response');
        }
      })
    );
  }

  // oauth2Login(token: string): Observable<any> {
  //   return this.http.post(`${this.apiUrl}login/oauth2`, { token }).pipe(
  //     tap((response: any) => {
  //       localStorage.setItem('oauthToken', response.oauthToken);
  //     })
  //   );
  // }

  OAuth2Login() {

    localStorage.setItem('loginMethod', 'oauth2');
    window.location.href = this.oauthUrl;

    // window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  logout(loginMethod: any): Observable<any> {
    if (loginMethod === 'jwt') {
      return this.http.post(`${this.apiUrl}logout`, {}).pipe(
        tap(() => {
          localStorage.removeItem('jwtToken');
          localStorage.removeItem('loginMethod');
        })
      );
    } else if (loginMethod === 'oauth2') {

      return this.http.post(`${this.apiUrl}logout/oauth2`, {}).pipe(
        tap(() => {
          // localStorage.removeItem('oauthToken');
          localStorage.removeItem('loginMethod');
        })
      );
    } else {
      return of(null); // Return an observable of null if no condition is met
    }
  }
}

