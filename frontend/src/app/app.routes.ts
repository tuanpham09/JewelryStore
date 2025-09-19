import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { Home } from './home/home';
import { ProductDetail } from './product-detail/product-detail';
import { Cart } from './cart/cart';
import { OrderHistory } from './order-history/order-history';
import { Profile } from './profile/profile';
import { Wishlist } from './wishlist/wishlist';
import { Search } from './search/search';
import { AdminComponent } from './admin/admin';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: Home },
  { path: 'product/:id', component: ProductDetail },
  { path: 'cart', component: Cart },
  { path: 'order-history', component: OrderHistory },
  { path: 'profile', component: Profile },
  { path: 'wishlist', component: Wishlist },
  { path: 'search', component: Search },
  { path: 'admin', component: AdminComponent },
  // Wildcard route để xử lý tất cả các routes không khớp
  { path: '**', redirectTo: 'home' }
];
