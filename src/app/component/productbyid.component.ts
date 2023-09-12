import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../service/product.service';
import { Products } from '../products';
import { Title } from '@angular/platform-browser';
import { CartService } from '../service/cart.service';
import { Cart } from '../cart';

@Component({
  selector: 'app-productbyid',
  templateUrl: './productbyid.component.html',
  styleUrls: ['./productbyid.component.css']
})
export class ProductbyidComponent implements OnInit{

  product!: Products;
  productId! :number;
  quantity: number = 1;
  showCart = false;
  cart : Cart[] = [];


  constructor(private router : Router, private route: ActivatedRoute, private service: ProductService, private cartService : CartService, private title : Title) {}

  ngOnInit(): void {
    this.productId = this.route.snapshot.params['id'];

    this.service.getProductById(this.productId).subscribe({
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
    this.cartService.addToCart(this.productId, this.quantity).subscribe({
      next : (data) => {
        console.log(`Added ${this.quantity} of ${this.product.name} to cart.`);
        this.showCartNotification();
      }
    })
  }

  goToCart() {
    this.router.navigateByUrl('/cart')
    console.log(`Proceeding to checkout with ${this.quantity} of ${this.product.name}.`);
  }

  showCartNotification() {
    this.showCart = true;
    this.getCartItems();
  }

  hideCart() {
    this.showCart = false;
  }

  getCartItems() {
    this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cart = data;
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  removeCartItem(item : Cart) {
    console.log('Trying to remove cart item:', item);  // Debug line

    const loginMethod = localStorage.getItem('loginMethod');
    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.cartService.removeUserCartItem(item.cartId).subscribe({
        next: (response) => {
          console.log('Item removed for User')
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      })
    } else {
      this.cartService.removeGuestCartItem(item.productId).subscribe({
        next : (response) => {
          console.log('Item removed for Guest')
          this.getCartItems();
        },
        error : (err) => {
          console.log(err);
        }
      })
    }
  }

  clearCart() {
    const loginMethod = localStorage.getItem('loginMethod');

    if (loginMethod === 'jwt' || loginMethod === 'oauth2') {
      this.cartService.clearUserCart().subscribe({
        next: (response) => {
          console.log('User cart cleared');
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      });
    } else {
      this.cartService.clearGuestCart().subscribe({
        next: (response) => {
          console.log('Guest cart cleared');
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }

}
