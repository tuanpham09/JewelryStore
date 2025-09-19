import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { Home } from './home/home';
import { ProductDetail } from './product-detail/product-detail';
import { Cart } from './cart/cart';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: Home },
  { path: 'product/:id', component: ProductDetail },
  { path: 'cart', component: Cart },
  // Wildcard route để xử lý tất cả các routes không khớp
  { path: '**', redirectTo: 'home' }
];
