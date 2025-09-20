import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatSliderModule } from '@angular/material/slider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';
import { AuthService } from '../auth.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface Product {
    id: string;
    name: string;
    price: number;
    originalPrice?: number;
    discount?: number;
    image: string;
    category: string;
    description: string;
    rating: number;
    reviewCount: number;
    inStock: boolean;
    materials: string[];
    colors: string[];
    sizes: string[];
    tags: string[];
}

interface FilterOptions {
    categories: string[];
    materials: string[];
    colors: string[];
    sizes: string[];
    priceRange: [number, number];
    rating: number;
    inStock: boolean;
    onSale: boolean;
}

interface SortOption {
    value: string;
    label: string;
}

@Component({
    selector: 'app-search',
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        MatChipsModule,
        MatSliderModule,
        MatCheckboxModule,
        MatDividerModule,
        MatBadgeModule,
        Header,
        Footer
    ],
    templateUrl: './search.html',
    styleUrl: './search.css'
})
export class Search implements OnInit {
    currentUser: any = null;
    searchQuery: string = '';
    sortBy: string = 'relevance';
    viewMode: 'grid' | 'list' = 'grid';

    // Filter options
    filters: FilterOptions = {
        categories: [],
        materials: [],
        colors: [],
        sizes: [],
        priceRange: [0, 5000000],
        rating: 0,
        inStock: false,
        onSale: false
    };

    // Available options
    availableCategories = [
        { value: 'earrings', label: 'Bông tai' },
        { value: 'rings', label: 'Nhẫn' },
        { value: 'bracelets', label: 'Vòng tay' },
        { value: 'necklaces', label: 'Dây chuyền' },
        { value: 'watches', label: 'Đồng hồ' }
    ];

    availableMaterials = [
        { value: 'gold', label: 'Vàng' },
        { value: 'silver', label: 'Bạc' },
        { value: 'platinum', label: 'Bạch kim' },
        { value: 'diamond', label: 'Kim cương' },
        { value: 'pearl', label: 'Ngọc trai' },
        { value: 'crystal', label: 'Pha lê' }
    ];

    availableColors = [
        { value: 'gold', label: 'Vàng' },
        { value: 'silver', label: 'Bạc' },
        { value: 'rose-gold', label: 'Vàng hồng' },
        { value: 'white-gold', label: 'Vàng trắng' },
        { value: 'black', label: 'Đen' },
        { value: 'multi', label: 'Nhiều màu' }
    ];

    availableSizes = [
        { value: 'XS', label: 'XS' },
        { value: 'S', label: 'S' },
        { value: 'M', label: 'M' },
        { value: 'L', label: 'L' },
        { value: 'XL', label: 'XL' }
    ];

    sortOptions: SortOption[] = [
        { value: 'relevance', label: 'Liên quan nhất' },
        { value: 'price-low', label: 'Giá thấp đến cao' },
        { value: 'price-high', label: 'Giá cao đến thấp' },
        { value: 'rating', label: 'Đánh giá cao nhất' },
        { value: 'newest', label: 'Mới nhất' },
        { value: 'popular', label: 'Phổ biến nhất' }
    ];

    // Products data
    allProducts: Product[] = [];
    filteredProducts: Product[] = [];
    totalResults: number = 0;
    showFilters: boolean = false;

    constructor(
        public router: Router,
        private route: ActivatedRoute,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        // Get search query from route
        this.route.queryParams.subscribe(params => {
            this.searchQuery = params['q'] || '';
            this.performSearch();
        });

        this.loadProducts();
    }

    loadProducts() {
        // Mock data - trong thực tế sẽ gọi API
        this.allProducts = [
            {
                id: '1',
                name: 'Vòng tay ngọc trai Corrine - vàng mờ',
                price: 990000,
                originalPrice: 1200000,
                discount: 18,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                category: 'bracelets',
                description: 'Vòng tay thanh lịch với ngọc trai tự nhiên và vàng mờ sang trọng',
                rating: 4.5,
                reviewCount: 128,
                inStock: true,
                materials: ['pearl', 'gold'],
                colors: ['gold'],
                sizes: ['S', 'M', 'L'],
                tags: ['elegant', 'pearl', 'gold']
            },
            {
                id: '2',
                name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
                price: 550000,
                image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=300&h=300&fit=crop&crop=center',
                category: 'rings',
                description: 'Bộ nhẫn đôi với thiết kế liên kết tinh tế và ngọc trai',
                rating: 4.8,
                reviewCount: 95,
                inStock: true,
                materials: ['pearl', 'rose-gold'],
                colors: ['rose-gold'],
                sizes: ['S', 'M'],
                tags: ['couple', 'pearl', 'rose-gold']
            },
            {
                id: '3',
                name: 'Bông tai hoa pha lê Felicity - vàng hồng',
                price: 1050000,
                image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=300&h=300&fit=crop&crop=center',
                category: 'earrings',
                description: 'Bông tai với thiết kế hoa tinh tế và pha lê Swarovski',
                rating: 4.7,
                reviewCount: 203,
                inStock: true,
                materials: ['crystal', 'rose-gold'],
                colors: ['rose-gold'],
                sizes: ['R'],
                tags: ['crystal', 'flower', 'rose-gold']
            },
            {
                id: '4',
                name: 'Dây chuyền kim cương - bạch kim',
                price: 2500000,
                originalPrice: 3000000,
                discount: 17,
                image: 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=300&h=300&fit=crop&crop=center',
                category: 'necklaces',
                description: 'Dây chuyền cao cấp với kim cương và bạch kim',
                rating: 4.9,
                reviewCount: 67,
                inStock: true,
                materials: ['diamond', 'platinum'],
                colors: ['silver'],
                sizes: ['M', 'L'],
                tags: ['diamond', 'luxury', 'platinum']
            },
            {
                id: '5',
                name: 'Đồng hồ nữ sang trọng - vàng trắng',
                price: 3500000,
                image: 'https://images.unsplash.com/photo-1523170335258-f5c6c6f0ff4e?w=300&h=300&fit=crop&crop=center',
                category: 'watches',
                description: 'Đồng hồ nữ với thiết kế sang trọng và vàng trắng',
                rating: 4.6,
                reviewCount: 89,
                inStock: false,
                materials: ['white-gold'],
                colors: ['white-gold'],
                sizes: ['S', 'M'],
                tags: ['watch', 'luxury', 'white-gold']
            }
        ];

        this.performSearch();
    }

    performSearch() {
        let results = [...this.allProducts];

        // Text search
        if (this.searchQuery.trim()) {
            const query = this.searchQuery.toLowerCase();
            results = results.filter(product =>
                product.name.toLowerCase().includes(query) ||
                product.description.toLowerCase().includes(query) ||
                product.tags.some(tag => tag.toLowerCase().includes(query))
            );
        }

        // Apply filters
        results = this.applyFilters(results);

        // Apply sorting
        results = this.applySorting(results);

        this.filteredProducts = results;
        this.totalResults = results.length;
    }

    applyFilters(products: Product[]): Product[] {
        return products.filter(product => {
            // Category filter
            if (this.filters.categories.length > 0 && !this.filters.categories.includes(product.category)) {
                return false;
            }

            // Material filter
            if (this.filters.materials.length > 0 && !this.filters.materials.some(material => product.materials.includes(material))) {
                return false;
            }

            // Color filter
            if (this.filters.colors.length > 0 && !this.filters.colors.some(color => product.colors.includes(color))) {
                return false;
            }

            // Size filter
            if (this.filters.sizes.length > 0 && !this.filters.sizes.some(size => product.sizes.includes(size))) {
                return false;
            }

            // Price range filter
            if (product.price < this.filters.priceRange[0] || product.price > this.filters.priceRange[1]) {
                return false;
            }

            // Rating filter
            if (this.filters.rating > 0 && product.rating < this.filters.rating) {
                return false;
            }

            // In stock filter
            if (this.filters.inStock && !product.inStock) {
                return false;
            }

            // On sale filter
            if (this.filters.onSale && !product.originalPrice) {
                return false;
            }

            return true;
        });
    }

    applySorting(products: Product[]): Product[] {
        switch (this.sortBy) {
            case 'price-low':
                return products.sort((a, b) => a.price - b.price);
            case 'price-high':
                return products.sort((a, b) => b.price - a.price);
            case 'rating':
                return products.sort((a, b) => b.rating - a.rating);
            case 'newest':
                return products.sort((a, b) => b.id.localeCompare(a.id));
            case 'popular':
                return products.sort((a, b) => b.reviewCount - a.reviewCount);
            default:
                return products;
        }
    }

    // Filter methods
    toggleCategory(category: string) {
        const index = this.filters.categories.indexOf(category);
        if (index > -1) {
            this.filters.categories.splice(index, 1);
        } else {
            this.filters.categories.push(category);
        }
        this.performSearch();
    }

    toggleMaterial(material: string) {
        const index = this.filters.materials.indexOf(material);
        if (index > -1) {
            this.filters.materials.splice(index, 1);
        } else {
            this.filters.materials.push(material);
        }
        this.performSearch();
    }

    toggleColor(color: string) {
        const index = this.filters.colors.indexOf(color);
        if (index > -1) {
            this.filters.colors.splice(index, 1);
        } else {
            this.filters.colors.push(color);
        }
        this.performSearch();
    }

    toggleSize(size: string) {
        const index = this.filters.sizes.indexOf(size);
        if (index > -1) {
            this.filters.sizes.splice(index, 1);
        } else {
            this.filters.sizes.push(size);
        }
        this.performSearch();
    }

    onPriceRangeChange() {
        this.performSearch();
    }

    onRatingChange() {
        this.performSearch();
    }

    onInStockChange() {
        this.performSearch();
    }

    onOnSaleChange() {
        this.performSearch();
    }

    clearAllFilters() {
        this.filters = {
            categories: [],
            materials: [],
            colors: [],
            sizes: [],
            priceRange: [0, 5000000],
            rating: 0,
            inStock: false,
            onSale: false
        };
        this.performSearch();
    }

    getActiveFiltersCount(): number {
        return this.filters.categories.length +
            this.filters.materials.length +
            this.filters.colors.length +
            this.filters.sizes.length +
            (this.filters.rating > 0 ? 1 : 0) +
            (this.filters.inStock ? 1 : 0) +
            (this.filters.onSale ? 1 : 0);
    }

    // Navigation methods
    goBack() {
        this.router.navigate(['/']);
    }

    navigateToProduct(productId: string) {
        this.router.navigate(['/product', productId]);
    }

    addToCart(product: Product) {
        if (!product.inStock) {
            alert('Sản phẩm hiện tại đã hết hàng!');
            return;
        }
        console.log('Adding to cart:', product);
    }

    addToWishlist(product: Product) {
        console.log('Adding to wishlist:', product);
    }

    getStarArray(rating: number): number[] {
        return Array(5).fill(0).map((_, i) => i < Math.floor(rating) ? 1 : 0);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    }
}
