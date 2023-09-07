import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Products } from '../products';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  private apiUrl : string = "http://localhost:8080/api/products";

  getProducts() : Observable<Products[]> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.get<Products[]>(this.apiUrl, {headers : headers});
  }

  getProductById(id: string) : Observable<Products> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.get<Products>(`${this.apiUrl}/${id}`, {headers : headers});
  }
}
