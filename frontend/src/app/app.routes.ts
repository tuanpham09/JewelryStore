import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { Home } from './home/home';
import { HomeMaia } from './home-maia/home-maia';
import { ProductDetail } from './product-detail/product-detail';
import { Cart } from './cart/cart';
import { Checkout } from './checkout/checkout';
import { CheckoutSuccessComponent } from './checkout/checkout-success/checkout-success.component';
import { CheckoutCancelComponent } from './checkout/checkout-cancel/checkout-cancel.component';
import { OrderHistory } from './order-history/order-history';
import { Profile } from './profile/profile';
import { Wishlist } from './wishlist/wishlist';
import { Search } from './search/search';
import { AdminComponent } from './admin/admin';
import { NotFoundComponent } from './not-found/not-found';

export const routes: Routes = [
  { path: '', component: HomeMaia },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeMaia },
  { path: 'product/:id', component: ProductDetail },
  { path: 'cart', component: Cart },
  { path: 'checkout', component: Checkout },
  { path: 'checkout/success', component: CheckoutSuccessComponent },
  { path: 'checkout/cancel', component: CheckoutCancelComponent },
  { path: 'order-history', component: OrderHistory },
  { path: 'profile', component: Profile },
  { path: 'wishlist', component: Wishlist },
  { path: 'search', component: Search },
  { path: 'admin', component: AdminComponent },
  { path: '404', component: NotFoundComponent },
  // Wildcard route để xử lý tất cả các routes không khớp
  { path: '**', redirectTo: '404' }
];
