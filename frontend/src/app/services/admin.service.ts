import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ProductDto {
    id: number;
    name: string;
    slug?: string;
    description: string;
    shortDescription?: string;
    price: number;
    originalPrice?: number;
    salePrice?: number;
    categoryId: number;
    categoryName: string;
    material: string;
    color: string;
    size: string;
    sizes?: string[];
    weight?: number;
    dimensions?: string;
    sku?: string;
    barcode?: string;
    brand?: string;
    origin?: string;
    warrantyPeriod?: number;
    images: ProductImageDto[];
    thumbnail?: string;
    inStock: boolean;
    stock?: number;
    minStock?: number;
    maxStock?: number;
    isActive?: boolean;
    isFeatured?: boolean;
    isNew?: boolean;
    isBestseller?: boolean;
    rating: number;
    averageRating?: number;
    reviewCount: number;
    viewCount?: number;
    soldCount?: number;
    metaTitle?: string;
    metaDescription?: string;
    metaKeywords?: string;
    tags: string[];
    createdAt: string;
    updatedAt: string;
}

export interface ProductImageDto {
    id: number;
    productId: number;
    imageUrl: string;
    altText?: string;
    isPrimary: boolean;
    sortOrder: number;
}

export interface ProductUpsertDto {
    name: string;
    slug?: string;
    description: string;
    shortDescription?: string;
    price: number;
    originalPrice?: number;
    salePrice?: number;
    categoryId: number;
    material: string;
    color: string;
    size: string;
    sizes?: string[];
    weight?: number;
    dimensions?: string;
    sku?: string;
    barcode?: string;
    brand?: string;
    origin?: string;
    warrantyPeriod?: number;
    thumbnail?: string;
    inStock: boolean;
    stock?: number;
    minStock?: number;
    maxStock?: number;
    isActive?: boolean;
    isFeatured?: boolean;
    isNew?: boolean;
    isBestseller?: boolean;
    viewCount?: number;
    soldCount?: number;
    metaTitle?: string;
    metaDescription?: string;
    metaKeywords?: string;
    tags: string[];
}

export interface CategoryDto {
    id: number;
    name: string;
    description?: string;
    parentId?: number;
    isActive: boolean;
    createdAt: string;
    updatedAt: string;
    slug?: string;
    imageUrl?: string;
    sortOrder?: number;
    productCount?: number;
}

export interface AdminUserDto {
    id: number;
    email: string;
    fullName: string;
    phoneNumber?: string;
    address?: string;
    enabled: boolean;
    roles: string[];
    createdAt: string;
    updatedAt: string;
    lastLoginAt?: string;
    orderCount?: number;
    totalSpent?: number;
}

export interface AdminOrderDto {
    id: number;
    orderNumber: string;
    customerId: number;
    customerName: string;
    customerEmail: string;
    customerPhone: string;
    status: string;
    totalAmount: number;
    paymentMethod: string;
    paymentStatus: string;
    shippingAddress: string;
    notes?: string;
    createdAt: string;
    updatedAt: string;
    items: OrderItemDto[];
}

export interface OrderItemDto {
    id: number;
    productId: number;
    productName: string;
    productImage?: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
}

export interface PromotionDto {
    id: number;
    name: string;
    description: string;
    type: string;
    value: number;
    minOrderAmount: number;
    code: string;
    startDate: string;
    endDate: string;
    active: boolean;
    usageLimit: number;
    usedCount: number;
    applicableProducts: string;
    applicableCategories?: string;
    applicableProductIds?: string;
    createdAt: string;
    updatedAt: string;
}

export interface DashboardStatsDto {
    totalRevenue: number;
    todayRevenue: number;
    monthlyRevenue: number;
    totalOrders: number;
    todayOrders: number;
    monthlyOrders: number;
    totalCustomers: number;
    newCustomersToday: number;
    totalProducts: number;
    lowStockProducts: number;
    outOfStockProducts: number;
    pendingOrders: number;
    activePromotions: number;
    topSellingProducts: TopProductDto[];
    topCustomers: TopCustomerDto[];
    revenueChart: RevenueChartDto[];
    orderStatusStats: OrderStatusDto[];
}

export interface TopProductDto {
    productId: number;
    productName: string;
    productThumbnail?: string;
    totalSold: number;
    totalRevenue: number;
}

export interface TopCustomerDto {
    customerId: number;
    customerName: string;
    customerEmail: string;
    totalOrders: number;
    totalSpent: number;
}

export interface RevenueChartDto {
    date: string;
    revenue: number;
    orders: number;
}

export interface OrderStatusDto {
    status: string;
    count: number;
    totalValue: number;
}

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    private apiUrl = `${environment.apiUrl}/admin`;

    constructor(private http: HttpClient) { }

    // ========== PRODUCT MANAGEMENT ==========
    
    // Get all products
    getProducts(): Observable<ProductDto[]> {
        return this.http.get<ProductDto[]>(`${this.apiUrl}/products`);
    }

    // Get product by ID
    getProductById(id: number): Observable<ProductDto> {
        return this.http.get<ProductDto>(`${this.apiUrl}/products/${id}`);
    }

    // Create new product
    createProduct(product: ProductUpsertDto): Observable<ProductDto> {
        return this.http.post<ProductDto>(`${this.apiUrl}/products`, product);
    }

    // Update product
    updateProduct(id: number, product: ProductUpsertDto): Observable<ProductDto> {
        return this.http.put<ProductDto>(`${this.apiUrl}/products/${id}`, product);
    }

    // Delete product
    deleteProduct(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/products/${id}`);
    }

    // ========== PRODUCT IMAGE MANAGEMENT ==========
    
    // Get product images
    getProductImages(productId: number): Observable<ProductImageDto[]> {
        return this.http.get<ProductImageDto[]>(`${this.apiUrl}/products/${productId}/images`);
    }

    // Upload product image
    uploadProductImage(productId: number, file: File): Observable<ProductDto> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post<ProductDto>(`${this.apiUrl}/products/${productId}/images`, formData);
    }

    // Add product image by URL
    addProductImage(productId: number, imageUrl: string, altText?: string): Observable<ProductImageDto> {
        const params = new URLSearchParams();
        params.append('imageUrl', imageUrl);
        if (altText) {
            params.append('altText', altText);
        }
        return this.http.post<ProductImageDto>(`${this.apiUrl}/products/${productId}/images/url?${params.toString()}`, {});
    }

    // Update product image
    updateProductImage(imageId: number, altText?: string, sortOrder?: number): Observable<ProductImageDto> {
        const params = new URLSearchParams();
        if (altText) {
            params.append('altText', altText);
        }
        if (sortOrder !== undefined) {
            params.append('sortOrder', sortOrder.toString());
        }
        return this.http.put<ProductImageDto>(`${this.apiUrl}/products/images/${imageId}?${params.toString()}`, {});
    }

    // Delete product image
    deleteProductImage(productId: number, imageUrl: string): Observable<void> {
        const params = new URLSearchParams();
        params.append('imageUrl', imageUrl);
        return this.http.delete<void>(`${this.apiUrl}/products/${productId}/images?${params.toString()}`);
    }

    // Delete product image by ID
    deleteProductImageById(imageId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/products/images/${imageId}`);
    }

    // Set primary image
    setPrimaryImage(productId: number, imageId: number): Observable<ProductImageDto> {
        return this.http.put<ProductImageDto>(`${this.apiUrl}/products/${productId}/images/${imageId}/primary`, {});
    }

    // Reorder product images
    reorderProductImages(productId: number, imageIds: number[]): Observable<ProductImageDto> {
        return this.http.put<ProductImageDto>(`${this.apiUrl}/products/${productId}/images/reorder`, imageIds);
    }

    // ========== CATEGORY MANAGEMENT ==========
    
    // Get all categories
    getCategories(): Observable<CategoryDto[]> {
        return this.http.get<CategoryDto[]>(`${this.apiUrl}/categories`);
    }

    // Create category
    createCategory(category: CategoryDto): Observable<CategoryDto> {
        return this.http.post<CategoryDto>(`${this.apiUrl}/categories`, category);
    }

    // Update category
    updateCategory(id: number, category: CategoryDto): Observable<CategoryDto> {
        return this.http.put<CategoryDto>(`${this.apiUrl}/categories/${id}`, category);
    }

    // Delete category
    deleteCategory(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/categories/${id}`);
    }

    // ========== USER MANAGEMENT ==========
    
    // Get all users
    getUsers(page: number = 0, size: number = 20): Observable<AdminUserDto[]> {
        return this.http.get<AdminUserDto[]>(`${this.apiUrl}/users?page=${page}&size=${size}`);
    }

    // Get user by ID
    getUserById(id: number): Observable<AdminUserDto> {
        return this.http.get<AdminUserDto>(`${this.apiUrl}/users/${id}`);
    }

    // Update user status
    updateUserStatus(id: number, enabled: boolean): Observable<AdminUserDto> {
        return this.http.put<AdminUserDto>(`${this.apiUrl}/users/${id}/status?enabled=${enabled}`, {});
    }

    // Update user roles
    updateUserRoles(id: number, roles: string[]): Observable<AdminUserDto> {
        return this.http.put<AdminUserDto>(`${this.apiUrl}/users/${id}/roles`, roles);
    }

    // Delete user
    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/users/${id}`);
    }

    // ========== ORDER MANAGEMENT ==========
    
    // Get all orders
    getOrders(page: number = 0, size: number = 20): Observable<AdminOrderDto[]> {
        return this.http.get<AdminOrderDto[]>(`${this.apiUrl}/orders?page=${page}&size=${size}`);
    }

    // Get order by ID
    getOrderById(id: number): Observable<AdminOrderDto> {
        return this.http.get<AdminOrderDto>(`${this.apiUrl}/orders/${id}`);
    }

    // Update order status
    updateOrderStatus(id: number, status: string): Observable<AdminOrderDto> {
        return this.http.put<AdminOrderDto>(`${this.apiUrl}/orders/${id}/status?status=${status}`, {});
    }

    // Cancel order
    cancelOrder(id: number, reason: string): Observable<AdminOrderDto> {
        return this.http.put<AdminOrderDto>(`${this.apiUrl}/orders/${id}/cancel?reason=${encodeURIComponent(reason)}`, {});
    }

    // Get orders by status
    getOrdersByStatus(status: string, page: number = 0, size: number = 20): Observable<AdminOrderDto[]> {
        return this.http.get<AdminOrderDto[]>(`${this.apiUrl}/orders/status/${status}?page=${page}&size=${size}`);
    }

    // ========== PROMOTION MANAGEMENT ==========
    
    // Get all promotions
    getPromotions(): Observable<PromotionDto[]> {
        return this.http.get<PromotionDto[]>(`${this.apiUrl}/promotions`);
    }

    // Create promotion
    createPromotion(promotion: PromotionDto): Observable<PromotionDto> {
        return this.http.post<PromotionDto>(`${this.apiUrl}/promotions`, promotion);
    }

    // Update promotion
    updatePromotion(id: number, promotion: PromotionDto): Observable<PromotionDto> {
        return this.http.put<PromotionDto>(`${this.apiUrl}/promotions/${id}`, promotion);
    }

    // Delete promotion
    deletePromotion(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/promotions/${id}`);
    }

    // Toggle promotion status
    togglePromotionStatus(id: number): Observable<PromotionDto> {
        return this.http.put<PromotionDto>(`${this.apiUrl}/promotions/${id}/toggle`, {});
    }

    // ========== DASHBOARD & STATISTICS ==========
    
    // Get dashboard statistics
    getDashboardStats(): Observable<DashboardStatsDto> {
        return this.http.get<DashboardStatsDto>(`${this.apiUrl}/dashboard/stats`);
    }

    // Get revenue report
    getRevenueReport(): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/reports/revenue`);
    }

    // Get revenue report by date range
    getRevenueReportByDateRange(startDate: string, endDate: string): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/reports/revenue/date-range?startDate=${startDate}&endDate=${endDate}`);
    }
}
