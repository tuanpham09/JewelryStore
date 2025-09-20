import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';
import { MatChipsModule } from '@angular/material/chips';
import { AuthService } from '../auth.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface CartItem {
    id: string;
    productId: string;
    name: string;
    price: number;
    originalPrice?: number;
    image: string;
    quantity: number;
    selected: boolean;
    size?: string;
    color?: string;
    inStock: boolean;
    stockQuantity: number;
}

@Component({
    selector: 'app-cart',
    imports: [
        CommonModule,
        FormsModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatCheckboxModule,
        MatDividerModule,
        MatBadgeModule,
        MatChipsModule,
        Header,
        Footer
    ],
    templateUrl: './cart.html',
    styleUrl: './cart.css'
})
export class Cart implements OnInit {
    currentUser: any = null;
    cartItems: CartItem[] = [];
    allSelected: boolean = false;
    selectedItems: CartItem[] = [];

    constructor(
        public router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        this.loadCartItems();
    }

    loadCartItems() {
        // Mock data - trong thực tế sẽ lấy từ service hoặc localStorage
        this.cartItems = [
            {
                id: '1',
                productId: '1',
                name: 'Vòng tay ngọc trai Corrine - vàng mờ',
                price: 990000,
                originalPrice: 1200000,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                quantity: 2,
                selected: true,
                size: 'M',
                color: 'Vàng mờ',
                inStock: true,
                stockQuantity: 15
            },
            {
                id: '2',
                productId: '2',
                name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
                price: 550000,
                image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=300&h=300&fit=crop&crop=center',
                quantity: 1,
                selected: true,
                size: 'S',
                color: 'Vàng hồng',
                inStock: true,
                stockQuantity: 8
            },
            {
                id: '3',
                productId: '4',
                name: 'Bông tai hoa pha lê Felicity - vàng hồng',
                price: 1050000,
                image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=300&h=300&fit=crop&crop=center',
                quantity: 1,
                selected: false,
                size: 'R',
                color: 'Vàng hồng',
                inStock: true,
                stockQuantity: 12
            },
            {
                id: '4',
                productId: '9',
                name: 'Vòng tay charm hoa hồng - vàng hồng',
                price: 750000,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                quantity: 1,
                selected: true,
                size: 'M',
                color: 'Vàng hồng',
                inStock: false,
                stockQuantity: 0
            }
        ];

        this.updateSelectedItems();
        this.updateAllSelected();
    }

    toggleSelectAll() {
        this.allSelected = !this.allSelected;
        this.cartItems.forEach(item => {
            if (item.inStock) {
                item.selected = this.allSelected;
            }
        });
        this.updateSelectedItems();
    }

    toggleSelectItem(item: CartItem) {
        if (item.inStock) {
            item.selected = !item.selected;
            this.updateSelectedItems();
            this.updateAllSelected();
        }
    }

    updateSelectedItems() {
        this.selectedItems = this.cartItems.filter(item => item.selected);
    }

    updateAllSelected() {
        const selectableItems = this.cartItems.filter(item => item.inStock);
        this.allSelected = selectableItems.length > 0 && selectableItems.every(item => item.selected);
    }

    increaseQuantity(item: CartItem) {
        if (item.quantity < item.stockQuantity) {
            item.quantity++;
        }
    }

    decreaseQuantity(item: CartItem) {
        if (item.quantity > 1) {
            item.quantity--;
        }
    }

    removeItem(item: CartItem) {
        const index = this.cartItems.findIndex(cartItem => cartItem.id === item.id);
        if (index > -1) {
            this.cartItems.splice(index, 1);
            this.updateSelectedItems();
            this.updateAllSelected();
        }
    }

    removeSelectedItems() {
        this.cartItems = this.cartItems.filter(item => !item.selected);
        this.updateSelectedItems();
        this.updateAllSelected();
    }

    getSubtotal(): number {
        return this.selectedItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    }

    getOriginalTotal(): number {
        return this.selectedItems.reduce((total, item) => {
            const originalPrice = item.originalPrice || item.price;
            return total + (originalPrice * item.quantity);
        }, 0);
    }

    getDiscount(): number {
        return this.getOriginalTotal() - this.getSubtotal();
    }

    getShipping(): number {
        // Free shipping for orders over 1,000,000 VND
        return this.getSubtotal() >= 1000000 ? 0 : 50000;
    }

    getTotal(): number {
        return this.getSubtotal() + this.getShipping();
    }

    getTotalItems(): number {
        return this.selectedItems.reduce((total, item) => total + item.quantity, 0);
    }

    getSelectableItemsCount(): number {
        return this.cartItems.filter(item => item.inStock).length;
    }

    proceedToCheckout() {
        if (this.selectedItems.length === 0) {
            alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
            return;
        }

        // Logic chuyển đến trang thanh toán
        console.log('Proceeding to checkout with items:', this.selectedItems);
        // this.router.navigate(['/checkout']);
    }

    continueShopping() {
        this.router.navigate(['/']);
    }

    goBack() {
        this.router.navigate(['/']);
    }

    navigateToProduct(productId: string) {
        this.router.navigate(['/product', productId]);
    }
}
