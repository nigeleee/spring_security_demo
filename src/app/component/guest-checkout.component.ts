import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CartService } from '../service/cart.service';
import { Cart } from '../cart';
import { NgZone } from '@angular/core';


declare let paypal : any;

@Component({
  selector: 'app-guest-checkout',
  templateUrl: './guest-checkout.component.html',
  styleUrls: ['./guest-checkout.component.css']
})
export class GuestCheckoutComponent implements OnInit {
  constructor(private fb: FormBuilder, private router : Router, private cartService : CartService, private zone: NgZone) {}

  detailsForm! : FormGroup;
  sub$! : Subscription;
  cartItems : Cart[] = [];
  totalAmount!: number;

  ngOnInit(): void {
    this.initializeForm();
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
              this.guestCheckout();
              this.cartService.clearGuestCart().subscribe({
                next: (response) => {
                  console.log('Guest cart cleared');
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

  initializeForm() {
    this.detailsForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.maxLength]],
      address: ['', [Validators.required]]
    });
  }

  guestCheckout() {

    this.sub$ = this.cartService.guestCheckout(this.cartItems, this.detailsForm.value).subscribe({
      next : (response) => {
        console.log('Checking out by guest user');
        alert('Transaction completed');
        this.router.navigateByUrl('/home');
      },

      error : (err) => {
        console.log(err);
      }
    })
  }

  calculateTotalAmount(cartItems : Cart[]) {
    let totalAmount=0;
    for(const item of cartItems) {
      totalAmount += item.totalPrice
    }
    return totalAmount;
  }

  ngOnDestroy() {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }
}
