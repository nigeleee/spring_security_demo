import { OrderDetails } from './../orderDetails';
import { Subscription } from 'rxjs';
import { Component } from '@angular/core';
import { Cart } from '../cart';
import { CartService } from '../service/cart.service';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  cartItems: Cart[] = [];
  productId! : number;
  quantity! : number;
  orderDetails!: OrderDetails;
  totalAmount!: number;

  constructor(private cartService: CartService, private authService: AuthenticationService, private route : Router) { }

  ngOnInit(): void {
    this.getCartItems();

  }

  getCartItems() {
    this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cartItems = data;
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  // removeGuestCartItem(productId: number) {
  //   this.cartService.removeGuestCartItem(productId).subscribe(() => {
  //     console.log("Removing product with ID:", productId);
  //     this.getCartItems();
  //   });
  // }

  // removeCartItem(cartItemId: number) {
  //   this.cartService.removeCartItem(cartItemId).subscribe(() => {
  //     this.getCartItems();
  //   });
  // }

  //combine both guest and user login
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

  proceedToCheckout(): void {
    const loginMethod = localStorage.getItem('loginMethod');

    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.route.navigateByUrl('/checkout');

    } else {
      this.route.navigateByUrl('/guest-checkout');
   }
  }

  calculateTotalAmount(cartItems : Cart[]) {
    let totalAmount=0;
    for(const item of cartItems) {
      totalAmount += item.totalPrice
    }
    return totalAmount;
  }

}
