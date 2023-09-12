import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from '../users';
// import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }


  // private  apiRegisterUrl: string = 'https://miniprojectdemo-production.up.railway.app/api/register'
  private apiRegisterUrl : string = "http://localhost:8080/api/register";
  // private apiRegisterUrl = environment.apiRegisterUrl;

  create(users: Users) : Observable<Users> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    // return this.http.post<Users>(this.apiUrl, users, {headers : headers});
    return this.http.post<Users>(this.apiRegisterUrl, users, {headers : headers});
  }
}
