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
import { AuthService } from '../auth.service'; 
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';
import { BreadcrumbComponent, BreadcrumbItem } from '../shared/breadcrumb/breadcrumb';

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
        Footer,
        BreadcrumbComponent
    ],
    templateUrl: './cart.html',
    styleUrl: './cart.css'
})
export class Cart implements OnInit {
    cartItems: CartItem[] = [];
    cartItemCount = 0;
    cartTotal = 0;

    // Breadcrumb items
    breadcrumbItems: BreadcrumbItem[] = [
        { label: 'Trang chủ', url: '/home' },
        { label: 'Giỏ hàng', active: true }
    ];

    constructor(
        private cartService: CartService,
        private authService: AuthService,
        private router: Router,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        
        // Check if user is logged in (for debugging)
        const currentUser = this.authService.getCurrentUser();
        
        this.cartService.cartItems$.subscribe(cartItems => {
            this.cartItems = cartItems;
            this.cartItemCount = this.cartService.getCartItemCount();
            this.cartTotal = this.cartService.getCartTotal();
        });
    }

    updateQuantity(itemId: string, quantity: number) {
        this.cartService.updateQuantity(itemId, quantity).subscribe(success => {
            if (!success) {
                this.snackBar.open('Có lỗi xảy ra khi cập nhật số lượng', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    removeItem(itemId: string) {
        this.cartService.removeFromCart(itemId).subscribe(success => {
            if (success) {
                this.snackBar.open('Đã xóa sản phẩm khỏi giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            } else {
                this.snackBar.open('Có lỗi xảy ra khi xóa sản phẩm', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    clearCart() {
        this.cartService.clearCart().subscribe(success => {
            if (success) {
                this.snackBar.open('Đã xóa tất cả sản phẩm khỏi giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            } else {
                this.snackBar.open('Có lỗi xảy ra khi xóa giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    proceedToCheckout() {
        if (this.cartItems.length === 0) {
            this.snackBar.open('Giỏ hàng trống', 'Đóng', {
                duration: 3000
            });
            return;
        }

        // Kiểm tra đăng nhập
        const currentUser = this.authService.getCurrentUser();
        if (!currentUser) {
            this.snackBar.open('Vui lòng đăng nhập để thanh toán', 'Đóng', {
                duration: 3000
            });
            
            // Lưu callback URL để quay lại sau khi đăng nhập
            localStorage.setItem('callback_url', '/checkout');
            
            // Chuyển đến trang đăng nhập
            this.router.navigate(['/login']);
            return;
        }

        this.router.navigate(['/checkout']);
    }

    continueShopping() {
        this.router.navigate(['/']);
    }

    formatPrice(price: number): string {
        const correctPrice = this.getCorrectPrice(price);
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(correctPrice);
    }

    // Fix: Nếu giá quá lớn (có thể bị nhân với 1000), chia cho 1000
    getCorrectPrice(price: number): number {
        return price > 100000000 ? price / 1000 : price;
    }
}