import { GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient, private authService: SocialAuthService) { }

  login(email: string, password: string): Observable<any> {
    const payload = { email, password };
    return this.http.post(`${this.apiUrl}login`, payload);
  }

  // oauth2Login() {
  //   this.authService.signIn(GoogleLoginProvider.PROVIDER_ID)
  //     .then(user => {
  //       this.user = user;
  //       // Handle user data and logic here
  //     })
  //     .catch(error => {
  //       console.error('Error signing in:', error);
  //     });
  // }
}
