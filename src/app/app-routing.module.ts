import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home.component';
import { LoginComponent } from './component/login.component';
import { ProductsComponent } from './component/products.component';
import { SignupComponent } from './component/signup.component';
import { ProductbyidComponent } from './component/productbyid.component';
import { AboutComponent } from './component/about.component';
import { CartComponent } from './component/cart.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // redirect to `home` route
  { path: 'home', component: HomeComponent, title: 'Home' },
  { path: 'signup', component: SignupComponent, title: 'Sign Up' },
  { path: 'login', component: LoginComponent, title: 'Login' },
  { path: 'products', component: ProductsComponent, title: 'Products' },
  { path: 'product/:id', component: ProductbyidComponent},
  { path: 'cart', component: CartComponent},
  { path: 'about', component: AboutComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
