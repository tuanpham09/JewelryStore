import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
import { ProductService, Product, SearchResponse } from '../services/product.service';
import { CategoryService, Category } from '../services/category.service';
import { SearchService, ProductSearchDto } from '../services/search.service';

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
    FormsModule,
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
  ];

  products: Product[] = [];

  // Search properties
  searchKeyword: string = '';
  isSearching: boolean = false;
  showSuggestions: boolean = false;
  searchSuggestions: string[] = [];
  quickSearchTags: string[] = [
    'Nhẫn vàng', 'Bông tai kim cương', 'Vòng tay charm', 
    'Dây chuyền bạc', 'Lắc tay vàng', 'Nhẫn cưới'
  ];
  isSearchMode: boolean = false;

  // Product lists
  newProducts: Product[] = [];
  onSaleProducts: Product[] = [];
  isLoadingNew: boolean = false;
  isLoadingOnSale: boolean = false;

  // Pagination
  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;
  hasNextPage: boolean = false;
  hasPreviousPage: boolean = false;

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
    private categoryService: CategoryService,
    private searchService: SearchService
  ) { }

  ngOnInit() {
    this.authService.loadUserFromToken();

    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    this.loadProducts();
    this.loadCategories();
    this.loadNewProducts();
    this.loadOnSaleProducts();
  }

  loadProducts(page: number = 0) {
    this.isLoading = true;
    this.error = null;
    this.currentPage = page;

    // Use search API according to API_DOCUMENTATION.md
    const searchDto: ProductSearchDto = {
      page: page,
      size: this.pageSize,
      sortBy: this.getSortByForSearch(),
      sortOrder: this.getSortOrderForSearch()
    };

    // Add category filter if selected
    if (this.selectedCategory !== 'all' && this.selectedCategory !== '0') {
      const selectedCat = this.categories.find(cat => cat.id.toString() === this.selectedCategory);
      if (selectedCat) {
        searchDto.categoryId = selectedCat.id;
      }
    }

    console.log('Loading products with searchDto:', searchDto);

    this.searchService.searchProducts(searchDto).subscribe({
      next: (response) => {
        console.log('Products response:', response);
        if (response.success) {
          this.products = response.data.content;
          this.totalProducts = response.data.totalElements;
          this.totalPages = response.data.totalPages;
          this.hasNextPage = response.data.hasNext;
          this.hasPreviousPage = response.data.hasPrevious;
          console.log('Products loaded:', this.products.length, 'of', this.totalProducts);
        } else {
          this.error = response.message || 'Không thể tải sản phẩm';
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Có lỗi xảy ra khi tải sản phẩm';
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
    this.currentPage = 0;
    this.loadProducts();
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

  // Search methods
  onSearchInput() {
    if (this.searchKeyword.length > 2) {
      this.showSuggestions = true;
      this.generateSuggestions();
    } else {
      this.showSuggestions = false;
    }
  }

  generateSuggestions() {
    // Generate suggestions based on categories and popular searches
    const allSuggestions = [
      ...this.categories.map(cat => cat.name),
      'Nhẫn vàng', 'Bông tai kim cương', 'Vòng tay charm',
      'Dây chuyền bạc', 'Lắc tay vàng', 'Nhẫn cưới',
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

  performSearch(page: number = 0) {
    if (!this.searchKeyword.trim()) {
      this.currentPage = 0;
      this.loadProducts(); // Load all products if search is empty
      this.isSearchMode = false;
      return;
    }

    this.isSearching = true;
    this.isSearchMode = true;
    this.showSuggestions = false;
    this.currentPage = page;

    // Use search API according to API_DOCUMENTATION.md
    const searchDto: ProductSearchDto = {
      query: this.searchKeyword.trim(),
      page: page,
      size: this.pageSize,
      sortBy: this.getSortByForSearch(),
      sortOrder: this.getSortOrderForSearch()
    };

    // Add category filter if selected
    if (this.selectedCategory !== 'all' && this.selectedCategory !== '0') {
      const selectedCat = this.categories.find(cat => cat.id.toString() === this.selectedCategory);
      if (selectedCat) {
        searchDto.categoryId = selectedCat.id;
      }
    }

    console.log('Performing search with searchDto:', searchDto);

    this.searchService.searchProducts(searchDto).subscribe({
      next: (response) => {
        console.log('Search response:', response);
        if (response.success) {
          this.products = response.data.content;
          this.totalProducts = response.data.totalElements;
          this.totalPages = response.data.totalPages;
          this.hasNextPage = response.data.hasNext;
          this.hasPreviousPage = response.data.hasPrevious;
          console.log('Search results loaded:', this.products.length, 'of', this.totalProducts);
        } else {
          this.error = response.message || 'Không thể tìm kiếm sản phẩm';
          this.products = [];
        }
        this.isSearching = false;
      },
      error: (error) => {
        console.error('Error searching products:', error);
        this.error = 'Có lỗi xảy ra khi tìm kiếm sản phẩm';
        this.products = [];
        this.isSearching = false;
      }
    });
  }

  private getSortByForSearch(): string {
    switch (this.sortBy) {
      case 'price-low':
      case 'price-high':
        return 'price';
      case 'newest':
        return 'createdAt';
      case 'featured':
        return 'isFeatured';
      default:
        return 'createdAt';
    }
  }

  private getSortOrderForSearch(): string {
    switch (this.sortBy) {
      case 'price-low':
        return 'asc';
      case 'price-high':
      case 'newest':
      case 'featured':
      default:
        return 'desc';
    }
  }

  private getSortByForProducts(): string {
    switch (this.sortBy) {
      case 'price-low':
      case 'price-high':
        return 'price';
      case 'newest':
        return 'createdAt';
      case 'featured':
        return 'isFeatured';
      default:
        return 'createdAt';
    }
  }

  private getSortOrderForProducts(): string {
    switch (this.sortBy) {
      case 'price-low':
        return 'asc';
      case 'price-high':
      case 'newest':
      case 'featured':
      default:
        return 'desc';
    }
  }

  clearSearch() {
    this.searchKeyword = '';
    this.isSearchMode = false;
    this.showSuggestions = false;
    this.currentPage = 0;
    this.loadProducts();
  }

  // Pagination methods
  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      if (this.isSearchMode) {
        this.performSearch(page);
      } else {
        this.loadProducts(page);
      }
    }
  }

  goToNextPage() {
    if (this.hasNextPage) {
      this.goToPage(this.currentPage + 1);
    }
  }

  goToPreviousPage() {
    if (this.hasPreviousPage) {
      this.goToPage(this.currentPage - 1);
    }
  }

  goToFirstPage() {
    this.goToPage(0);
  }

  goToLastPage() {
    this.goToPage(this.totalPages - 1);
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, this.currentPage + 2);
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  }


  // Load new products (for sidebar)
  loadNewProducts() {
    this.isLoadingNew = true;
    this.productService.getNewProducts(0, 5).subscribe({
      next: (response: SearchResponse) => {
        console.log('New products response:', response);
        if (response.success) {
          this.newProducts = response.data.content;
          console.log('New products loaded:', this.newProducts);
        }
        this.isLoadingNew = false;
      },
      error: (error) => {
        console.error('Error loading new products:', error);
        this.isLoadingNew = false;
      }
    });
  }

  // Load on-sale products
  loadOnSaleProducts() {
    this.isLoadingOnSale = true;
    this.productService.getOnSaleProducts(0, 8).subscribe({
      next: (response: SearchResponse) => {
        console.log('On sale products response:', response);
        if (response.success) {
          this.onSaleProducts = response.data.content;
          console.log('On sale products loaded:', this.onSaleProducts);
        }
        this.isLoadingOnSale = false;
      },
      error: (error) => {
        console.error('Error loading on-sale products:', error);
        this.isLoadingOnSale = false;
      }
    });
  }
}