import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) { }

  private paymentUrl = 'http://localhost:8080/api/payment';

  makePayment(amount: number) : Observable<any>{
    return this.http.post(`${this.paymentUrl}`, amount);
  }
}
