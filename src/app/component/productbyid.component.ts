import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../service/product.service';
import { Products } from '../products';
import { Subscription } from 'rxjs';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-productbyid',
  templateUrl: './productbyid.component.html',
  styleUrls: ['./productbyid.component.css']
})
export class ProductbyidComponent implements OnInit{

  product!: Products;
  id: string = '';
  quantity: number = 1;


  constructor(private route: ActivatedRoute, private service: ProductService, private title : Title) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.service.getProductById(this.id).subscribe({
      next: (data) => {
        console.log(data);
        this.product = data;
        this.title.setTitle(this.product.name);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
  add() {
    this.quantity++;
  }

  minus() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart() {
    // Implement add to cart functionality here
    console.log(`Added ${this.quantity} of ${this.product.name} to cart.`);
  }

  buyNow() {
    // Implement checkout functionality here
    console.log(`Proceeding to checkout with ${this.quantity} of ${this.product.name}.`);
  }
}
