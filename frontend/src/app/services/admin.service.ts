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
}
