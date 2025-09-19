import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { AuthService } from '../../auth.service';

@Component({
    selector: 'app-header',
    imports: [
        CommonModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatBadgeModule
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
}
