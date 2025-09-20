import { Component, Input, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../auth.service';

@Component({
    selector: 'app-header',
    imports: [
        CommonModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatBadgeModule,
        MatMenuModule,
        MatDividerModule
    ],
    templateUrl: './header.html',
    styleUrl: './header.css'
})
export class Header {
    @Input() showBackButton: boolean = false;
    @Input() backButtonText: string = 'Quay lại';
    @Input() cartItemCount: number = 0;

    currentUser: any = null;

    constructor(
        public router: Router,
        private authService: AuthService
    ) {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });
    }

    goBack() {
        this.router.navigate(['/']);
    }

    navigateToHome() {
        this.router.navigate(['/']);
    }

    navigateToLogin() {
        this.router.navigate(['/login']);
    }

    navigateToCart() {
        this.router.navigate(['/cart']);
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }

    navigateToProfile() {
        this.router.navigate(['/profile']);
    }

    navigateToOrderHistory() {
        this.router.navigate(['/order-history']);
    }

    navigateToWishlist() {
        this.router.navigate(['/wishlist']);
    }

    navigateToSearch() {
        this.router.navigate(['/search']);
    }

    navigateToAdmin() {
        this.router.navigate(['/admin']);
    }

    getInitials(): string {
        if (this.currentUser && this.currentUser.fullName) {
            return this.currentUser.fullName
                .split(' ')
                .map((name: string) => name.charAt(0))
                .join('')
                .toUpperCase()
                .substring(0, 2);
        }
        return 'U';
    }
}
