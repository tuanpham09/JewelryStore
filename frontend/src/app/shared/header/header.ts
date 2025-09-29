import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatSelectModule } from '@angular/material/select';
import { AuthService } from '../../auth.service';
import { CategoryService, Category } from '../../services/category.service';
import { SearchService, ProductSearchDto } from '../../services/search.service';
import { Subject, debounceTime, distinctUntilChanged, switchMap } from 'rxjs';

@Component({
    selector: 'app-header',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        RouterModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatBadgeModule,
        MatMenuModule,
        MatDividerModule,
        MatSelectModule,
    ],
    templateUrl: './header.html',
    styleUrl: './header.css'
})
export class Header implements OnInit, OnDestroy {
    @Input() showBackButton: boolean = false;
    @Input() backButtonText: string = 'Quay lại';
    @Input() cartItemCount: number = 0;
    @Input() wishlistCount: number = 0;

    currentUser: any = null;
    categories: Category[] = [];
    selectedSearchCategory: string = 'all';
    searchKeyword: string = '';
    recentlyViewedProducts: any[] = [];

    // Search properties
    isSearching: boolean = false;
    showSuggestions: boolean = false;
    searchSuggestions: string[] = [];
    quickSearchTags: string[] = [
        'Nhẫn vàng', 'Bông tai kim cương', 'Vòng tay charm', 
        'Dây chuyền bạc', 'Lắc tay vàng', 'Nhẫn cưới'
    ];

    // Realtime search
    private searchSubject = new Subject<string>();

    constructor(
        public router: Router,
        private authService: AuthService,
        private categoryService: CategoryService,
        private searchService: SearchService
    ) {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });
    }

    ngOnInit() {
        this.loadCategories();
        this.setupRealtimeSearch();
    }

    ngOnDestroy() {
        this.searchSubject.complete();
    }

    setupRealtimeSearch() {
        // Setup realtime search with debounce
        this.searchSubject.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            switchMap(searchTerm => {
                if (searchTerm.trim()) {
                    this.isSearching = true;
                    return this.performSearchRequest(searchTerm);
                } else {
                    return [];
                }
            })
        ).subscribe({
            next: (response) => {
                this.handleSearchResponse(response);
            },
            error: (error) => {
                console.error('Error in realtime search:', error);
                this.isSearching = false;
            }
        });
    }

    loadCategories() {
        this.categoryService.getAllCategories().subscribe({
            next: (response) => {
                if (response.success) {
                    this.categories = response.data.filter(cat => cat.isActive);
                }
            },
            error: (error) => {
                console.error('Error loading categories:', error);
            }
        });
    }

    getCategoryIcon(categoryName: string): string {
        const iconMap: { [key: string]: string } = {
            'Nhẫn': 'favorite',
            'Bông tai': 'radio_button_checked',
            'Vòng tay': 'watch',
            'Dây chuyền': 'cable',
            'Lắc tay': 'watch',
            'Nhẫn cưới': 'favorite',
            'Trang sức nam': 'male',
            'Trang sức nữ': 'female'
        };
        return iconMap[categoryName] || 'diamond';
    }

    selectCategory(category: Category) {
        this.router.navigate(['/shop'], { 
            queryParams: { category: category.id } 
        });
    }

    onSearchInput() {
        // Trigger realtime search with debounce
        this.searchSubject.next(this.searchKeyword);
        
        // Handle suggestions
        if (this.searchKeyword.length > 2) {
            this.showSuggestions = true;
            this.generateSuggestions();
        } else {
            this.showSuggestions = false;
        }
    }

    performSearch() {
        if (this.searchKeyword.trim()) {
            this.router.navigate(['/search'], {
                queryParams: {
                    q: this.searchKeyword,
                    category: this.selectedSearchCategory
                }
            });
        }
    }

    // Helper methods for reactive search
    private performSearchRequest(searchTerm: string) {
        const searchDto: ProductSearchDto = {
            query: searchTerm,
            page: 0,
            size: 10,
            sortBy: 'createdAt',
            sortOrder: 'desc'
        };

        return this.searchService.searchProducts(searchDto);
    }

    private handleSearchResponse(response: any) {
        if (response && response.success) {
            // Update suggestions based on search results
            this.updateSuggestionsFromResults(response.data.content);
        }
        this.isSearching = false;
    }

    private updateSuggestionsFromResults(products: any[]) {
        const suggestions = products.slice(0, 5).map(product => product.name);
        this.searchSuggestions = [...new Set(suggestions)];
    }

    generateSuggestions() {
        // Generate suggestions based on categories and popular searches
        const allSuggestions = [
            ...this.categories.map(cat => cat.name),
            ...this.quickSearchTags,
            'Trang sức cao cấp', 'Phụ kiện nữ'
        ];
        
        this.searchSuggestions = allSuggestions
            .filter(suggestion => 
                suggestion.toLowerCase().includes(this.searchKeyword.toLowerCase())
            )
            .slice(0, 5);
    }

    selectSuggestion(suggestion: string) {
        this.searchKeyword = suggestion;
        this.showSuggestions = false;
        this.performSearch();
    }

    searchByTag(tag: string) {
        this.searchKeyword = tag;
        this.performSearch();
    }

    hideSuggestions() {
        setTimeout(() => {
            this.showSuggestions = false;
        }, 200);
    }

    toggleRecentlyViewed() {
        // Implement recently viewed products dropdown
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
        console.log('Header: navigateToCart called');
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

    isAdmin(): boolean {
        if (!this.currentUser) return false;
        
        // Kiểm tra nếu user có roles array và chứa ROLE_ADMIN
        if (this.currentUser.roles && Array.isArray(this.currentUser.roles)) {
            return this.currentUser.roles.some((role: any) => 
                role.name === 'ROLE_ADMIN' || role === 'ROLE_ADMIN'
            );
        }
        
        // Kiểm tra nếu user có role trực tiếp
        if (this.currentUser.role === 'ROLE_ADMIN') {
            return true;
        }
        
        return false;
    }
}
