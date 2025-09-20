import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';
import { ProductService, Product } from '../services/product.service';
import { CategoryService, Category } from '../services/category.service';

// Sử dụng Category interface từ service

// Sử dụng Product interface từ service

interface ViewOption {
  value: number;
  label: string;
}

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    Header,
    Footer
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  currentUser: any = null;
  selectedCategory: string = 'all';
  sortBy: string = 'featured';
  viewMode: number = 4;
  totalProducts: number = 0;
  isLoading: boolean = true;
  error: string | null = null;

  categories: Category[] = [];
  isLoadingCategories: boolean = true;

  viewOptions: ViewOption[] = [
    { value: 3, label: '3' },
    { value: 4, label: '4' },
    { value: 6, label: '6' }
  ];

  products: Product[] = [];

  get filteredProducts(): Product[] {
    let filtered = this.products;

    if (this.selectedCategory !== 'all' && this.selectedCategory !== '0') {
      // Tìm category được chọn
      const selectedCat = this.categories.find(cat => cat.id.toString() === this.selectedCategory);
      if (selectedCat) {
        filtered = filtered.filter(product => 
          product.categoryName?.toLowerCase().includes(selectedCat.name.toLowerCase())
        );
      }
    }

    // Sort products
    switch (this.sortBy) {
      case 'price-low':
        filtered = filtered.sort((a, b) => a.price - b.price);
        break;
      case 'price-high':
        filtered = filtered.sort((a, b) => b.price - a.price);
        break;
      case 'newest':
        // Sort by newest (assuming products with isNew = true are newest)
        filtered = filtered.sort((a, b) => {
          if (a.isNew && !b.isNew) return -1;
          if (!a.isNew && b.isNew) return 1;
          return 0;
        });
        break;
      default:
        // Keep original order for 'featured'
        break;
    }

    return filtered;
  }

  constructor(
    private authService: AuthService,
    public router: Router,
    private productService: ProductService,
    private categoryService: CategoryService
  ) { }

  ngOnInit() {
    this.authService.loadUserFromToken();

    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    this.loadProducts();
    this.loadCategories();
  }

  loadProducts() {
    this.isLoading = true;
    this.error = null;

    this.productService.getAllProducts().subscribe({
      next: (response) => {
        if (response.success) {
          this.products = response.data;
          this.totalProducts = this.products.length;
        } else {
          this.error = response.message || 'Không thể tải danh sách sản phẩm';
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Có lỗi xảy ra khi tải danh sách sản phẩm';
        this.isLoading = false;
      }
    });
  }

  loadCategories() {
    this.isLoadingCategories = true;

    this.categoryService.getAllCategories().subscribe({
      next: (response) => {
        if (response.success) {
          // Thêm "Tất cả" vào đầu danh sách
          this.categories = [
            { id: 0, name: 'Tất cả', slug: 'all', isActive: true },
            ...response.data.filter(cat => cat.isActive)
          ];
        }
        this.isLoadingCategories = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        // Fallback to default categories if API fails
        this.categories = [
          { id: 0, name: 'Tất cả', slug: 'all', isActive: true },
          { id: 1, name: 'Nhẫn', slug: 'rings', isActive: true },
          { id: 2, name: 'Bông tai', slug: 'earrings', isActive: true },
          { id: 3, name: 'Vòng tay', slug: 'bracelets', isActive: true },
          { id: 4, name: 'Dây chuyền', slug: 'necklaces', isActive: true }
        ];
        this.isLoadingCategories = false;
      }
    });
  }

  selectCategory(categoryId: string | number) {
    this.selectedCategory = categoryId.toString();
  }

  setViewMode(mode: number) {
    this.viewMode = mode;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateToProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }

  addToCart(product: Product) {
    // Logic thêm vào giỏ hàng
    console.log('Added to cart:', product);
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }
}