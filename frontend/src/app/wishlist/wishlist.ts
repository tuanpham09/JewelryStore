import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AuthService } from '../auth.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface WishlistItem {
    id: string;
    productId: string;
    name: string;
    price: number;
    originalPrice?: number;
    discount?: number;
    image: string;
    category: string;
    description: string;
    inStock: boolean;
    stockQuantity: number;
    addedDate: string;
    size?: string;
    color?: string;
}

@Component({
    selector: 'app-wishlist',
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatChipsModule,
        MatDividerModule,
        MatBadgeModule,
        MatCheckboxModule,
        Header,
        Footer
    ],
    templateUrl: './wishlist.html',
    styleUrl: './wishlist.css'
})
export class Wishlist implements OnInit {
    currentUser: any = null;
    wishlistItems: WishlistItem[] = [];
    selectedItems: string[] = [];
    allSelected: boolean = false;

    constructor(
        public router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        this.loadWishlist();
    }

    loadWishlist() {
        // Mock data - trong thực tế sẽ gọi API
        this.wishlistItems = [
            {
                id: '1',
                productId: '1',
                name: 'Vòng tay ngọc trai Corrine - vàng mờ',
                price: 990000,
                originalPrice: 1200000,
                discount: 18,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                category: 'bracelets',
                description: 'Vòng tay thanh lịch với ngọc trai tự nhiên và vàng mờ sang trọng',
                inStock: true,
                stockQuantity: 5,
                addedDate: '2024-01-15',
                size: 'M',
                color: 'Vàng mờ'
            },
            {
                id: '2',
                productId: '4',
                name: 'Bông tai hoa pha lê Felicity - vàng hồng',
                price: 1050000,
                image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=300&h=300&fit=crop&crop=center',
                category: 'earrings',
                description: 'Bông tai với thiết kế hoa tinh tế và pha lê Swarovski',
                inStock: true,
                stockQuantity: 3,
                addedDate: '2024-01-18',
                size: 'R',
                color: 'Vàng hồng'
            },
            {
                id: '3',
                productId: '7',
                name: 'Dây chuyền hoa pha lê Felicity - vàng hồng',
                price: 1200000,
                originalPrice: 1500000,
                discount: 20,
                image: 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=300&h=300&fit=crop&crop=center',
                category: 'necklaces',
                description: 'Dây chuyền với thiết kế hoa và pha lê, tạo vẻ đẹp quyến rũ',
                inStock: false,
                stockQuantity: 0,
                addedDate: '2024-01-20',
                size: 'L',
                color: 'Vàng hồng'
            },
            {
                id: '4',
                productId: '9',
                name: 'Vòng tay charm hoa hồng - vàng hồng',
                price: 750000,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                category: 'bracelets',
                description: 'Vòng tay với charm hoa hồng tinh tế, phù hợp cho mọi dịp',
                inStock: true,
                stockQuantity: 8,
                addedDate: '2024-01-22',
                size: 'M',
                color: 'Vàng hồng'
            }
        ];
    }

    toggleSelectAll() {
        this.allSelected = !this.allSelected;
        if (this.allSelected) {
            this.selectedItems = this.wishlistItems.map(item => item.id);
        } else {
            this.selectedItems = [];
        }
    }

    toggleItemSelection(itemId: string) {
        const index = this.selectedItems.indexOf(itemId);
        if (index > -1) {
            this.selectedItems.splice(index, 1);
        } else {
            this.selectedItems.push(itemId);
        }
        this.updateAllSelected();
    }

    updateAllSelected() {
        this.allSelected = this.selectedItems.length === this.wishlistItems.length;
    }

    removeFromWishlist(itemId: string) {
        this.wishlistItems = this.wishlistItems.filter(item => item.id !== itemId);
        this.selectedItems = this.selectedItems.filter(id => id !== itemId);
        this.updateAllSelected();
    }

    removeSelectedItems() {
        this.wishlistItems = this.wishlistItems.filter(item => !this.selectedItems.includes(item.id));
        this.selectedItems = [];
        this.allSelected = false;
    }

    addToCart(item: WishlistItem) {
        if (!item.inStock) {
            alert('Sản phẩm hiện tại đã hết hàng!');
            return;
        }
        console.log('Adding to cart:', item);
        // Logic thêm vào giỏ hàng
    }

    addSelectedToCart() {
        const selectedItems = this.wishlistItems.filter(item =>
            this.selectedItems.includes(item.id) && item.inStock
        );

        if (selectedItems.length === 0) {
            alert('Không có sản phẩm nào có thể thêm vào giỏ hàng!');
            return;
        }

        console.log('Adding selected items to cart:', selectedItems);
        // Logic thêm tất cả vào giỏ hàng
    }

    navigateToProduct(productId: string) {
        this.router.navigate(['/product', productId]);
    }

    goBack() {
        this.router.navigate(['/']);
    }

    getWishlistCount(): number {
        return this.wishlistItems.length;
    }

    getSelectedCount(): number {
        return this.selectedItems.length;
    }

    getInStockCount(): number {
        return this.wishlistItems.filter(item => item.inStock).length;
    }

    getTotalValue(): number {
        return this.wishlistItems.reduce((total, item) => total + item.price, 0);
    }

    getTotalSavings(): number {
        return this.wishlistItems.reduce((total, item) => {
            if (item.originalPrice) {
                return total + (item.originalPrice - item.price);
            }
            return total;
        }, 0);
    }
}
