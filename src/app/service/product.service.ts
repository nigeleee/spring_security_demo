import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Products } from '../products';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  private apiProductsUrl: string = 'https://miniprojectdemo-production.up.railway.app/api/products';

  // private apiUrl : string = "http://localhost:8080/api/products";
  // private apiProductsUrl = environment.apiProductsUrl;

  getProducts() : Observable<Products[]> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    // return this.http.get<Products[]>(this.apiUrl, {headers : headers});
    return this.http.get<Products[]>(this.apiProductsUrl, {headers : headers});
  }

  getProductById(id: string) : Observable<Products> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    // return this.http.get<Products>(`${this.apiUrl}/${id}`, {headers : headers});
    return this.http.get<Products>(`${this.apiProductsUrl}/${id}`, {headers : headers});
  }
}
