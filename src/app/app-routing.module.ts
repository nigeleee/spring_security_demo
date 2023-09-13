import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home.component';
import { LoginComponent } from './component/login.component';
import { ProductsComponent } from './component/products.component';
import { SignupComponent } from './component/signup.component';
import { ProductbyidComponent } from './component/productbyid.component';
import { AboutComponent } from './component/about.component';
import { CartComponent } from './component/cart.component';
import { CheckoutComponent } from './component/checkout.component';
import { GuestCheckoutComponent } from './component/guest-checkout.component';
// import { VerifydetailsComponent } from './component/verifydetails.component';
// import { CompleteProfileComponent } from './component/complete-profile.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // redirect to `home` route
  { path: 'home', component: HomeComponent, title: 'Home' },
  { path: 'signup', component: SignupComponent, title: 'Sign Up' },
  { path: 'login', component: LoginComponent, title: 'Login' },
  { path: 'products', component: ProductsComponent, title: 'Products' },
  { path: 'product/:id', component: ProductbyidComponent},
  { path: 'cart', component: CartComponent},
  { path: 'checkout', component: CheckoutComponent},
  { path: 'about', component: AboutComponent},
  { path: 'guest-checkout', component: GuestCheckoutComponent},
  // { path: 'verify-details', component: VerifydetailsComponent},
  // { path: 'complete-profile', component: CompleteProfileComponent},



];

@NgModule({
  // imports: [RouterModule.forRoot(routes, { useHash: true })],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
