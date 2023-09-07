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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProductsComponent,
    SignupComponent,
    ProductbyidComponent,
    AboutComponent,
    CartComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
