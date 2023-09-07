import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ProductService } from '../service/product.service';
import { Products } from '../products';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {

sub$! : Subscription;
products: Products[] = [];

constructor(private http: HttpClient, private router: Router, private authService: AuthenticationService, private productService: ProductService){}

ngOnInit(): void {
  this.sub$ = this.productService.getProducts().subscribe({
    next : (data) => {
      console.log(data);
      this.products = data;
    },
    error: (err) => {
      console.log(err);
    }
  })
}

logout() {
  console.log('Logout function called')

  const loginMethod = localStorage.getItem('loginMethod');
  console.log(`Login Method: ${loginMethod}`);

  if (loginMethod) {
    this.authService.logout(loginMethod).subscribe({
      next: () => {
        console.log("Logout Successful");
        alert('Successfully Logged Out');
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error(`${loginMethod} Logout failed`, error);
      },
    });
  }
}

ngOnDestroy(): void {
  if (this.sub$) {
    this.sub$.unsubscribe();
  }
}

}
