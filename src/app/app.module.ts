import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";
import { LoginComponent } from './component/login.component';
import { HomeComponent } from './component/home.component';
import { ProductsComponent } from './component/products.component';
import { SignupComponent } from './component/signup.component';
import { ProductbyidComponent } from './component/productbyid.component';
import { AboutComponent } from './component/about.component';
import { CartComponent } from './component/cart.component';
import { CheckoutComponent } from './component/checkout.component';
import { GuestCheckoutComponent } from './component/guest-checkout.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialsModule } from './materials/materials.module';
// import { VerifydetailsComponent } from './component/verifydetails.component';
// import { CompleteProfileComponent } from './component/complete-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProductsComponent,
    SignupComponent,
    ProductbyidComponent,
    AboutComponent,
    CartComponent,
    CheckoutComponent,
    GuestCheckoutComponent,
    // VerifydetailsComponent,
    // CompleteProfileComponent,
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
