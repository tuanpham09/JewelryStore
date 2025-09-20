import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ProductSize {
  id: number;
  size: string;
  stock: number;
  isActive: boolean;
}

export interface ProductImage {
  id: number;
  imageUrl: string;
  altText?: string;
  isPrimary: boolean;
  sortOrder: number;
}

export interface ProductReview {
  id: string;
  userName: string;
  rating: number;
  comment: string;
  date: string;
  verified: boolean;
}

export interface Product {
  id: number;
  name: string;
  slug: string;
  description: string;
  shortDescription?: string;
  price: number;
  originalPrice?: number;
  salePrice?: number;
  currentPrice?: number;
  stock: number;
  minStock?: number;
  maxStock?: number;
  sku?: string;
  barcode?: string;
  weight?: number;
  dimensions?: string;
  material?: string;
  color?: string;
  brand?: string;
  origin?: string;
  warrantyPeriod?: number;
  isActive: boolean;
  isFeatured?: boolean;
  isNew?: boolean;
  isBestseller?: boolean;
  isOnSale?: boolean;
  discountPercentage?: number;
  isLowStock?: boolean;
  isOutOfStock?: boolean;
  viewCount?: number;
  soldCount?: number;
  thumbnail: string;
  primaryImageUrl?: string;
  categoryId?: number;
  categoryName: string;
  averageRating?: number;
  reviewCount?: number;
  totalReviews?: number;
  isFavorite?: boolean;
  images?: ProductImage[];
  sizes?: ProductSize[];
  reviews?: ProductReview[];
}

export interface ProductResponse {
  success: boolean;
  message: string;
  data: Product | Product[];
}

export interface SearchResultDto<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
  hasNext: boolean;
  hasPrevious: boolean;
  totalCount: number;
  searchTime: string;
}

export interface SearchResponse {
  success: boolean;
  message: string;
  data: SearchResultDto<Product>;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Lấy tất cả sản phẩm với pagination
  getAllProducts(page: number = 0, size: number = 12, sortBy: string = 'createdAt', sortOrder: string = 'desc'): Observable<SearchResponse> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      sortBy: sortBy,
      sortOrder: sortOrder
    };
    return this.http.get<SearchResponse>(`${this.apiUrl}/products`, { params });
  }

  // Lấy sản phẩm theo ID
  getProductById(id: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products/${id}`);
  }

  // Lấy sản phẩm theo danh mục
  getProductsByCategory(categoryId: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products/category/${categoryId}`);
  }

  // Lấy sản phẩm nổi bật
  getFeaturedProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/featured?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm mới
  getNewProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/new?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm bán chạy
  getBestsellerProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/bestsellers?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm đang giảm giá
  getOnSaleProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/on-sale?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm liên quan
  getRelatedProducts(productId: number, limit: number = 4): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products/${productId}/related?limit=${limit}`);
  }

  // Tăng view count
  incrementViewCount(productId: number): Observable<ProductResponse> {
    return this.http.post<ProductResponse>(`${this.apiUrl}/products/${productId}/view`, {});
  }
}
