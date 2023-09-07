import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from '../users';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }

  private apiUrl : string = "http://localhost:8080/api/register";

  create(users: Users) : Observable<Users> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.post<Users>(this.apiUrl, users, {headers : headers});
  }
}
