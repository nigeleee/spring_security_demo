import { Component, NgZone, OnInit} from '@angular/core';
import { render } from "creditcardpayments/creditCardPayments";
import { CartService } from '../service/cart.service';
import { Cart } from '../cart';
import { Router } from '@angular/router';

declare let paypal: any;

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  constructor(private cartService : CartService, private route : Router, private zone: NgZone) {}

  totalAmount: number = 0;
  cartItems: Cart[] = [];

  ngOnInit(): void {
    this.getCartItems();

    paypal.Buttons({
      style: {
        layout: 'vertical',
        size: 'small',
        shape: 'pill',
        color: 'gold'
      },
      createOrder: (data: any, actions: any) => {
        return actions.order.create({
          purchase_units: [{
            amount: {
              currency_code: 'SGD',
              value: this.totalAmount.toString()
            }
          }]
        });
      },
      onApprove: (data: any, actions: any) => {
        return actions.order.capture().then((details : any) => {
          // this.snackBar.open(`Transaction completed by ${details.payer.name.given_name}`, 'Close', {
          //   duration: 3000,
          // });
          this.zone.run(() => {
            this.checkout();
            this.cartService.clearUserCart().subscribe({
              next: (response) => {
                console.log('User cart cleared');
                this.getCartItems();
              },
              error: (err) => {
                console.log(err);
              }
            });
          });
        });
      }
    }).render('#paypal-button-container');
  }

  getCartItems() {
    this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cartItems = data;
        this.totalAmount = this.calculateTotalAmount(this.cartItems);
        console.log("Total amount: " + this.totalAmount);
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  checkout(): void {
    const loginMethod = localStorage.getItem('loginMethod');

    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.cartService.userCheckout(this.cartItems).subscribe({
        next : (response) => {
          console.log("Checking out with registered user:" + response);
          alert('Transaction completed');
          this.route.navigateByUrl('/home');
        },
        error : (err) => {
          console.log(err);
        }
      })
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
