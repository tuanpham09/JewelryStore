import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../services/cart.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

@Component({
    selector: 'app-cart',
    imports: [
        CommonModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatBadgeModule,
        MatDividerModule,
        MatSnackBarModule,
        Header,
        Footer
    ],
    templateUrl: './cart.html',
    styleUrl: './cart.css'
})
export class Cart implements OnInit {
    cartItems: CartItem[] = [];
    cartItemCount = 0;
    cartTotal = 0;

    constructor(
        private cartService: CartService,
        private router: Router,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.cartService.cartItems$.subscribe(cartItems => {
            this.cartItems = cartItems;
            this.cartItemCount = this.cartService.getCartItemCount();
            this.cartTotal = this.cartService.getCartTotal();
        });
    }

    updateQuantity(itemId: string, quantity: number) {
        this.cartService.updateQuantity(itemId, quantity);
    }

    removeItem(itemId: string) {
        this.cartService.removeFromCart(itemId);
        this.snackBar.open('Đã xóa sản phẩm khỏi giỏ hàng', 'Đóng', {
            duration: 3000
        });
    }

    clearCart() {
        this.cartService.clearCart();
        this.snackBar.open('Đã xóa tất cả sản phẩm khỏi giỏ hàng', 'Đóng', {
            duration: 3000
        });
    }

    proceedToCheckout() {
        if (this.cartItems.length === 0) {
            this.snackBar.open('Giỏ hàng trống', 'Đóng', {
                duration: 3000
            });
            return;
        }
        this.router.navigate(['/checkout']);
    }

    continueShopping() {
        this.router.navigate(['/']);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    }
}